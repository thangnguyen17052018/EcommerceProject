package com.nminhthang;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.nminhthang.category.CategoryService;
import com.nminhthang.common.entity.Category;

@Controller
public class MainController {

//    @GetMapping("")
//    public String viewHomePage(){
//        return "index";
//    }
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authencation = SecurityContextHolder.getContext().getAuthentication();
		if(authencation == null || authencation instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}


}
