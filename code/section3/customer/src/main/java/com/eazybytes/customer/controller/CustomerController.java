package com.eazybytes.customer.controller;

import com.eazybytes.customer.constants.CustomerConstants;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.dto.ResponseDto;
import com.eazybytes.customer.service.ICustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private final ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService) {
        this.iCustomerService = iCustomerService;
    }

//    @GetMapping("/fetch")
//    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam("mobileNumber")
//    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
//    String mobileNumber) {
//        CustomerDto fetchedCustomer = iCustomerService.fetchCustomer(mobileNumber);
//        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(fetchedCustomer);
//    }

}
