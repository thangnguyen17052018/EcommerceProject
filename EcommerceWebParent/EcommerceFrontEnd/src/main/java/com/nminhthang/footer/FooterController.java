package com.nminhthang.footer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooterController {
	

    
    @GetMapping("PrivacyPolicy")
    public String viewPrivacyPage(Model model) {

        return "privacy";
    }
    
    
    @GetMapping("AffiliateProgram")
    public String viewAffiliateProgramPage(Model model) {

        return "affiliate";
    }
    
    @GetMapping("CareAndMaintenance")
    public String viewCareAndMaintenancePage(Model model) {
        return "care";
    }
    
    @GetMapping("FAQ")
    public String viewFAQPage(Model model) {

        return "frequentlyaskedquestion";
    }
    
    @GetMapping("Returns")
    public String viewReturnsPage(Model model) {
        return "returns";
    }
    
    @GetMapping("DiversityAndInclusion")
    public String viewDiversityAndInclusionPage(Model model) {
        return "diversity";
    }
    

}
