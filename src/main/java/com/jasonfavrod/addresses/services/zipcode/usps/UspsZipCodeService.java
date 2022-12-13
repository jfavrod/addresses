package com.jasonfavrod.addresses.services.zipcode.usps;

import com.jasonfavrod.addresses.models.Address;
import com.jasonfavrod.addresses.services.zipcode.ZipCodeLookup;
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
    private String baseUrl;
    private HttpClient httpClient;
    private String userId;

    public UspsZipCodeService() throws IOException {
        var env = System.getenv();

        httpClient = HttpClient.newHttpClient();

        var res = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        var props = new Properties();

        props.load(res);
        baseUrl = props.getProperty("usps.baseurl") + "?API=ZipCodeLookup";
        userId = env.get("USPS_USERNAME");
    }

    public String getByAddress(Address address) {
        var zip = "";
        var lookupRequest = new LookupRequestXml(userId);
        lookupRequest.addAddress(new com.jasonfavrod.addresses.services.zipcode.usps.Address(address));

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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return zip;
    }
}
