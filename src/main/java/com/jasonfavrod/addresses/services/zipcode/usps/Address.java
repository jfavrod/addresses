package com.jasonfavrod.addresses.services.zipcode.usps;

public class Address extends com.jasonfavrod.addresses.models.Address {
    private Integer id = 1;

    public Address() {}

    public Address(com.jasonfavrod.addresses.models.Address address) {
        this.address1 = address.getAddress1();
        this.address2 = address.getAddress2();
        this.city = address.getCity();
        this.state = address.getState();
        this.zip5 = address.getZip5();
        this.zip4 = address.getZip4();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toXml() {
        var str = new StringBuilder();

        str.append(String.format("<Address ID=\"%d\">", id));
        str.append(String.format("<Address1>%s</Address1>", address1));
        str.append(String.format("<Address2>%s</Address2>", address2));
        str.append(String.format("<City>%s</City>", city));
        str.append(String.format("<State>%s</State>", state));
        str.append(String.format("<Zip5>%s</Zip5>", zip5));
        str.append(String.format("<Zip4>%s</Zip4>", zip4));
        str.append("</Address>");

        return str.toString();
    }
}
