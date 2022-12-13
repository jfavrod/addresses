package com.jasonfavrod.addresses.controllers;

import com.jasonfavrod.addresses.models.Address;
import com.jasonfavrod.addresses.services.zipcode.ZipCodeLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zip")
public class ZipCodeController {
    private ZipCodeLookup zipCodeLookup;

    @Autowired
    public ZipCodeController(ZipCodeLookup zipCodeLookup) {
        this.zipCodeLookup = zipCodeLookup;
    }

    @PostMapping("/lookup")
    public ResponseEntity<String> getZipCodeForAddress(@RequestBody Address address) {
        return new ResponseEntity<String>(zipCodeLookup.getByAddress(address), HttpStatus.OK);
    }
}
