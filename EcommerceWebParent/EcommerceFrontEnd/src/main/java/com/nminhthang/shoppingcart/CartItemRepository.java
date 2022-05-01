package com.nminhthang.shoppingcart;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.product.Product;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
	
	List<CartItem> findByCustomer(Customer customer);
	
	CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	void updateQuantity(Integer quantity, Integer customerId, Integer productId);

	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	void deleteByCustomerAndProduct(Integer customerId, Integer productId);

	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1")
	void deleteByCustomer(Integer customerId);

}
