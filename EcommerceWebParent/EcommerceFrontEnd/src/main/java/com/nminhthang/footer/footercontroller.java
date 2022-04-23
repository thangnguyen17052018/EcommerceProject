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
    
    @GetMapping("/PrivacyPolicy")
    public String viewPrivacyPage(Model model) {

        return "privacy";
    }
    
    @GetMapping("/AffiliateProgram<")
    public String viewShippingPage(Model model) {

        return "affiliate";
    }
    
    @GetMapping("/CareAndMaintenance")
    public String viewCareAndMaintenancePage(Model model) {
        return "care";
    }
    
    @GetMapping("/Returns")
    public String viewReturnsPage(Model model) {
        return "returns";
    }
    
    @GetMapping("/DiversityAndInclusion")
    public String viewDiversityAndInclusionPage(Model model) {
        return "diversity";
    }
    

    
    @GetMapping("/FAQ")
    public String viewFAQPage(Model model) {

        return "frequentlyaskedquestion";
    }
}
