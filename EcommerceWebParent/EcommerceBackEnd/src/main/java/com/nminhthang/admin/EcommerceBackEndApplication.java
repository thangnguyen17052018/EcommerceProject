package com.nminhthang.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.nminhthang.common.entity", "com.nminhthang.admin.user"})
public class EcommerceBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceBackEndApplication.class, args);
    }

}
