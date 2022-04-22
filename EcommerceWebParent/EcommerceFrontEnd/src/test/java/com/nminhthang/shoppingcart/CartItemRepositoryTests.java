package com.nminhthang.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {
	
	@Autowired private CartItemRepository cartItemRepository;
	@Autowired private TestEntityManager testEntityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerId = 1;
		Integer productId = 3;
		
		Customer customer = testEntityManager.find(Customer.class, customerId);
		Product product = testEntityManager.find(Product.class, productId);
		
		CartItem newItem = new CartItem();
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		newItem.setQuantity(1);
		
		CartItem saveItem = cartItemRepository.save(newItem);
		
		assertThat(saveItem.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testSave2Items() {
		Integer customerId = 3;
		Integer productId = 4;
		
		Customer customer = testEntityManager.find(Customer.class, customerId);
		Product product = testEntityManager.find(Product.class, productId);
		
		CartItem item1 = new CartItem();
		item1.setCustomer(customer);
		item1.setProduct(product);
		item1.setQuantity(2);
		
		CartItem item2 = new CartItem();
		item2.setCustomer(new Customer(customerId));
		item2.setProduct(new Product(5));
		item2.setQuantity(3);
		
		Iterable<CartItem>	iterable = cartItemRepository.saveAll(List.of(item1,item2));
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 3;
		List<CartItem> listItems = cartItemRepository.findByCustomer(new Customer(customerId));
	
		listItems.forEach(System.out::println);
		
		assertThat(listItems.size()).isEqualTo(2);
	}
	
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 1;
		Integer productId = 3;
		
		CartItem item =	cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNotNull();
		
		System.err.println(item);
	}
	
	@Test
	public void testUpdateQuantity() {
		Integer customerId = 1;
		Integer productId = 3;
		Integer quantity = 10;
		
		cartItemRepository.updateQuantity(quantity, customerId, productId);
		
		CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
	
		assertThat(item.getQuantity()).isEqualTo(10);
		
	}
	
	@Test
	public void testDeleteByCustomerAndProduct() {
		Integer customerId = 3;
		Integer productId = 4;
		
		cartItemRepository.deleteByCustomerAndProduct(customerId, productId);
	
		CartItem item = cartItemRepository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNull();
	}
}
