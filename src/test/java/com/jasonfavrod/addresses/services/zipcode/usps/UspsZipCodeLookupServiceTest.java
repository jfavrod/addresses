package com.jasonfavrod.addresses.services.zipcode.usps;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;

@SpringBootTest
public class UspsZipCodeLookupServiceTest {
    @Test
    void basicLookup() throws IOException {
        var svc = new UspsZipCodeService();

        var address = new Address();
        address.setAddress1("6500 South Avalon AVE");
        address.setAddress2("313");
        address.setCity("Sioux Falls");
        address.setState("SD");

        var zip = svc.getByAddress(address);
        Assert.state(zip.equals("57108"), "zip must be correct");
    }
}
