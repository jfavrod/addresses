package com.jasonfavrod.addresses.services.zipcode.usps;

import com.jasonfavrod.addresses.models.Address;
import com.jasonfavrod.addresses.services.cache.CacheService;
import com.jasonfavrod.addresses.services.zipcode.ZipCodeLookup;
import com.jasonfavrod.addresses.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

@Service
public class UspsZipCodeService implements ZipCodeLookup {
    private final String baseUrl;
    private final CacheService cacheService;
    private final HttpClient httpClient;
    private final String userId;
    private int secondsToExpireCache = 60 * 60 * 24;

    @Autowired
    public UspsZipCodeService(CacheService cacheService) throws IOException {
        var env = System.getenv();

        this.cacheService = cacheService;
        httpClient = HttpClient.newHttpClient();

        var res = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        var props = new Properties();

        props.load(res);
        baseUrl = props.getProperty("usps.baseurl") + "?API=ZipCodeLookup";
        userId = env.get("USPS_USERNAME");
    }

    public String getByAddress(Address address) {
        var zip = cacheService.get(getCacheKey(address));
        var lookupRequest = new LookupRequestXml(userId);
        lookupRequest.addAddress(new com.jasonfavrod.addresses.services.zipcode.usps.Address(address));

        if (!StringUtils.isEmptyOrNull(zip)) {
            return zip;
        }

        try {
            var xml = lookupRequest.toXml();
            var uri = new URI(baseUrl + "&xml=" +  URLEncoder.encode(xml));
            var httpReq = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();

            var x = httpClient.send(httpReq, HttpResponse.BodyHandlers.ofString());
            var xpath = XPathFactory.newInstance().newXPath();
            var docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            zip = xpath.compile("//Address/Zip5").evaluate(
                    docBuilder.parse(new InputSource(new StringReader(x.body())))
            );
        } catch (URISyntaxException e) {
            System.out.println("This should never happen.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        cacheService.set(getCacheKey(address), zip, secondsToExpireCache);
        return zip;
    }

    private String getCacheKey(Address address) {
        return String.format("zip-%s", address.hash());
    }
}
