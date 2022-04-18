package com.nminhthang.admin.customer.controller;

import com.nminhthang.admin.customer.CustomerService;
import com.nminhthang.admin.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/customers/check_email")
    public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
        if (customerService.isEmailUnique(id, email)) {
            return "OK";
        } else {
            return "Duplicated";
        }
    }

}
