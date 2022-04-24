package com.nminhthang.site.customer;

import com.nminhthang.common.entity.AuthenticationType;
import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.customer.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        Integer countryId = 234;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("David");
        customer.setLastName("Fountaine");
        customer.setEmail("DavidFountaine@gmail.com");
        customer.setPassword("password123");
        customer.setPhoneNumber("312-462-7518");
        customer.setAddressLine1("1927 West Drive");
        customer.setCity("Sacramento");
        customer.setState("California");
        customer.setPostalCode("95867");

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2() {
        Integer countryId = 106;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Sanya");
        customer.setLastName("Lad");
        customer.setEmail("SanyaLad@gmail.com");
        customer.setPassword("password456");
        customer.setPhoneNumber("147-462-0218");
        customer.setAddressLine1("A su ajsaf Nahar Indl.estate, Sumil Road");
        customer.setAddressLine2("Dhanraj Mill Compound, Lower Parel (west)");
        customer.setCity("Mumbai");
        customer.setState("Maharashtra");
        customer.setPostalCode("25874");
        customer.setCreateTime(new Date());

        Customer savedCustomer = customerRepository.save(customer);

        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers() {
        Iterable<Customer> customers = customerRepository.findAll();

        customers.forEach(System.out::println);

        Assertions.assertThat(customers).hasSizeGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer() {
        Integer customerId = 1;
        String lastName = "Stanfield";

        Customer customer = customerRepository.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(true);
        customer.setVerificationCode("code_123");

        Customer updatedCustomer = customerRepository.save(customer);
        Assertions.assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetCustomer() {
        Integer customerId = 2;
        Optional<Customer> findById = customerRepository.findById(customerId);

        Assertions.assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Integer customerId = 2;
        Optional<Customer> findById = customerRepository.findById(customerId);

        Assertions.assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testFindByEmail() {
        String email = "DavidFountaine@gmail.com";
        Customer customer = customerRepository.findByEmail(email);

        Assertions.assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode() {
        String code = "code_123";
        Customer customer = customerRepository.findByVerificationCode(code);

        Assertions.assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer() {
        Integer customerId = 1;
        customerRepository.enabled(customerId);

        Customer customer = customerRepository.findById(customerId).get();
        Assertions.assertThat(customer.isEnabled()).isTrue();
    }
    
    @Test
    public void testUpdateAuthenticationType() {
    	Integer id = 2;
    	customerRepository.updateAuthenticationType(id, AuthenticationType.FACEBOOK);
    	
    	Customer customer = customerRepository.findById(id).get();
    	
    	assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
    }
}
