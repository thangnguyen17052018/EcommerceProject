package com.nminhthang;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nminhthang.common.entity.Customer;
import com.nminhthang.customer.CustomerService;

@Component
public class ControllerHelper {
	@Autowired private CustomerService customerService;
	
	public Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}
}
