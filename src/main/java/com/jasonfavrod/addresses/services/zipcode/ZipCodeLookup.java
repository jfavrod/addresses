package com.jasonfavrod.addresses.services.zipcode;

import com.jasonfavrod.addresses.models.Address;

public interface ZipCodeLookup {
    String getByAddress(Address address);
}
