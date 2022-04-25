package com.nminhthang.customer;

import com.nminhthang.Utility;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.exception.CustomerNotFoundException;
import com.nminhthang.setting.EmailSettingBag;
import com.nminhthang.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showRequestForm() {
        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        try {
            String updateResetPasswordToken = customerService.updateResetPasswordToken(email);
            String link = "http://localhost:8000" + Utility.getSiteURL(request) + "/reset_password?token=" + updateResetPasswordToken;
            sendEmail(email, link);

            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error", "Could not find any customer with the email: " + email);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Could not send email");
        }

        return "customer/forgot_password_form";
    }

    private void sendEmail(String email, String link) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password.</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made this request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model) {
        Customer customer = customerService.getByResetPasswordToken(token);

        if (customer != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String resetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        try {
            customerService.updatePassword(token, newPassword);
            model.addAttribute("pageTitle", "Reset Your Password");
            model.addAttribute("message", "You have successfully change your password.");
            model.addAttribute("title", "Reset your password");

            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", e.getMessage());
            return "message";
        }
    }

}
