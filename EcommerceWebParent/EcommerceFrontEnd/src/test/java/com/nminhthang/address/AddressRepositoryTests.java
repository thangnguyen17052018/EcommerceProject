package com.nminhthang.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.nminhthang.common.entity.Address;
import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {

	@Autowired private AddressRepository repo;
	
	@Test
	public void testAddNew() {
		Integer customerId = 1;
		Integer countryId = 242;
		
		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Hau");
		newAddress.setLastName("Nguyen Van");
		newAddress.setPhoneNumber("0399131313");
		newAddress.setAddressLine1("Ward 8, Long Phu District, Soc Trang Province");
		newAddress.setAddressLine2("");
		newAddress.setCity("Viet Nam");
		newAddress.setState("Soc Trang");
		newAddress.setPostalCode("123456");
		
		Address savedAddress = repo.save(newAddress);
		
		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 1;
		List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
		assertThat(listAddresses.size()).isGreaterThan(0);
		
		listAddresses.forEach(System.out::println);
	}
	
	@Test
	public void testFindByIdAndCustomer() {
		Integer addressId = 4;
		Integer customerId = 1;
		
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		
		assertThat(address).isNotNull();
		System.out.println(address);
	}
	
	@Test
	public void testUpdate() {
		Integer addressId = 4;
		String phoneNumber = "0399121111";
		
		Address address = repo.findById(addressId).get();
		address.setPhoneNumber(phoneNumber);

		Address updatedAddress = repo.save(address);
		assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumber);
	}
	
	@Test
	public void testDeleteByIdAndCustomer() {
		Integer addressId = 4;
		Integer customerId = 1;
		
		repo.deleteByIdAndCustomer(addressId, customerId);
		
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNull();
	}	
	
	@Test
	public void testSetDefault() {
		Integer addressId = 6;
		repo.setDefaultAddress(addressId);
		
		Address address = repo.findById(addressId).get();
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNonDefaultAddresses() {
		Integer addressId = 6;
		Integer customerId = 1;
		repo.setNonDefaultForOthers(addressId, customerId);			
	}
	
	@Test
	public void testGetDefault() {
		Integer customerId = 1;
		Address address = repo.findDefaultByCustomer(customerId);
		assertThat(address).isNotNull();
		System.out.println(address);
	}
}
