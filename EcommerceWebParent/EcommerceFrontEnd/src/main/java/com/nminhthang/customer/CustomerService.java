package com.nminhthang.customer;

import com.nminhthang.common.entity.AuthenticationType;
import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.setting.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

	@Autowired
    private CountryRepository countryRepository;

	@Autowired
    private CustomerRepository customerRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreateTime(new Date());

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
        System.out.println("Verification code: " + randomCode);
    }

    public Customer getCustomerByEmail(String email) {
    	return customerRepository.findByEmail(email);
    }
    
    
    public void encodePassword(Customer customer) {
        String encodePassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodePassword);
    }

    public boolean verify(String verificationCode) {
        Customer customer = customerRepository.findByVerificationCode(verificationCode);

        if ((customer == null) || customer.isEnabled()) {
            return false;
        } else {
            customerRepository.enabled(customer.getId());
            return true;
        }
    }
    
    public void updateAuthenticationType(Customer customer, AuthenticationType type) {
    	if(!customer.getAuthenticationType().equals(type)) {
    		customerRepository.updateAuthenticationType(customer.getId(), type);
    	}
    }
    
//    public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
//		Customer customer = new Customer();
//		customer.setEmail(email);
//		
//		setName(name, customer);
//		
//		customer.setEnabled(true);
//		customer.setCreateTime(new Date());
//		customer.setAuthenticationType(AuthenticationType.GOOLGE);
//		customer.setPassword("");
//		customer.setAddressLine1("");
//		customer.setCity("");
//		customer.setState("");
//		customer.setPhoneNumber("");
//		customer.setPostalCode("");
//		customer.setCountry(countryRepository.findByCode(countryCode));
//		
//		customerRepository.save(customer);
//	}
//    
//    private void setName(String name, Customer customer) {
//    	String[] nameArray = name.split(" ");
//    	if(nameArray.length < 2) {
//    		customer.setFirstName(name);
//    		customer.setLastName("");
//    	}
//    	else {
//    		String fisrtname = nameArray[0];
//    		customer.setFirstName(fisrtname);
//    		
//    		String lastName = name.replaceFirst(fisrtname, "");
//    		customer.setLastName(lastName);
//    	}
//    }
    

}
