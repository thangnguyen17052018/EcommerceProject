package com.nminhthang.security;

import com.nminhthang.security.oauth.CustomerOAuth2UserService;
import com.nminhthang.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;

	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/customer", "/cart", "/account_details", "/update_account_details", "/address_book/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                	.loginPage("/login")
                	.usernameParameter("email")
					.successHandler(databaseLoginSuccessHandler)
                	.permitAll()
                .and()
	                .oauth2Login()
	            	.loginPage("/login")
	            	.userInfoEndpoint()
	            	.userService(oAuth2UserService)
	            	.and()
	            	.successHandler(oAuth2LoginSuccessHandler)
	            .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                	.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                	.tokenValiditySeconds(14 * 24 * 60 * 60)
                .and()
                	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                ;
                
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**", "/css/**");
    }

}
