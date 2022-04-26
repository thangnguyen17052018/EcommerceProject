package com.nminhthang;

import com.nminhthang.security.oauth.CustomerOAuth2User;
import com.nminhthang.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.Properties;

public class Utility {

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURI().toString();
        System.out.println(siteURL);
        System.out.println(request.getServletPath());
        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }
    
    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
    	Object principal = request.getUserPrincipal();
    	if(principal == null) return null;
    	
    	String customerEmail = null;
    	
    	if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
    		customerEmail = request.getUserPrincipal().getName();
    	}
    	else if(principal instanceof OAuth2AuthenticationToken) {
    		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
    		CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
    		customerEmail = oauth2User.getEmail();
    	}
    	return customerEmail;
    }

    public static String formatCurrency(float amount) {
        String pattern = "$###,###.###";
        DecimalFormat formatter = new DecimalFormat(pattern);

        return formatter.format(amount);
    }

    public static void main(String[] args) {
        float amount = 678.995f;
        String fomattedCurrency = formatCurrency(amount);
        System.out.println(fomattedCurrency);
    }

}
