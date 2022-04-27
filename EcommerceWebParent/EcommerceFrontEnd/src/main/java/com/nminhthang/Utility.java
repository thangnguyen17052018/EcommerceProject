package com.nminhthang;

import com.nminhthang.security.oauth.CustomerOAuth2User;
import com.nminhthang.setting.CurrencySettingBag;
import com.nminhthang.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    public static String formatCurrency(float amount, CurrencySettingBag currencySettings) {
        String symbol = currencySettings.getSymbol();
        String symbolPosition = currencySettings.getSymbolPosition();
        String decimalPointType = currencySettings.getDecimalPointType();
        String thousandPointType = currencySettings.getThousandPointType();
        int decimalDigits = currencySettings.getDecimalDigits();

        String pattern = symbolPosition.equals("before") ? symbol : "";
        pattern += "###,###";

        if (decimalDigits > 0) {
            pattern += ".";
            for (int count = 1; count <= decimalDigits; count++) {
                pattern += "#";
            }
        }

        pattern += symbolPosition.equals("after") ? symbol : "";

        char thousandSeparator = "POINT".equals(thousandPointType) ? '.' : ',';
        char decimalSeparator = "POINT".equals(decimalPointType) ? '.' : ',';

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);

        return formatter.format(amount);
    }

}
