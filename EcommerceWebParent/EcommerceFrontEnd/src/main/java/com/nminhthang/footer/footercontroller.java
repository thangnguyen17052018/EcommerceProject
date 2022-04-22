package com.nminhthang.footer;

import com.nminhthang.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class footercontroller {
	
    @GetMapping("/AboutUs")
    public String viewAboutUsPage(Model model) {

        return "aboutus";
    }
    
    @GetMapping("/Shipping")
    public String viewShippingPage(Model model) {

        return "shipping";
    }
    
    @GetMapping("/FAQ")
    public String viewFAQPage(Model model) {

        return "frequentlyaskedquestion";
    }
}
