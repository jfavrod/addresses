package com.jasonfavrod.addresses.services.zipcode.usps;

import java.util.ArrayList;

public class LookupRequestXml {
    private ArrayList<Address> addresses;
    private String userId;

    public LookupRequestXml(String userId) {
        addresses = new ArrayList<>();
        this.userId = userId;
    }

    public void addAddress(Address address) {
        address.setId(addresses.size() + 1);
        addresses.add(address);
    }

    public String toXml() {
        var str = new StringBuilder();

        str.append(String.format("<ZipCodeLookupRequest USERID=\"%s\">", userId));
        addresses.forEach(addr -> str.append(addr.toXml()));
        str.append("</ZipCodeLookupRequest>");

        return str.toString();
    }
}
