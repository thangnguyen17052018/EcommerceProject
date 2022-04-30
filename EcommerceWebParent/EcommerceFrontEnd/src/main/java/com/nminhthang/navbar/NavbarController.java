package com.nminhthang.navbar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavbarController {
	
	
	
    @GetMapping("AboutUs")
    public String viewAboutUsPage(Model model) {

        return "aboutus";
    }
    
    
    @GetMapping("Shipping&Delivery")
    public String viewShippingPage(Model model) {

        return "shipping";
    }
    
    @GetMapping("Payments")
    public String viewPaymentPage(Model model) {

        return "payment";
    }
    
}
