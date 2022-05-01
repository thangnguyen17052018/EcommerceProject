package com.nminhthang.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.product.Product;
import com.nminhthang.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {
	
	@Autowired private CartItemRepository cartRepository;
	@Autowired private ProductRepository productRepository;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		System.out.println(quantity);

		Product product = new Product(productId);
		Product productInDB = productRepository.findById(productId).get();

		CartItem cartItem =  cartRepository.findByCustomerAndProduct(customer, product);
		
		if(cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			System.out.println(updatedQuantity);
			System.out.println(productInDB.getQuantityInStock());
			if(quantity > productInDB.getQuantityInStock()) {
				throw new ShoppingCartException("Could not add more " + quantity + " item(s)"
						+ " because there's are already " + cartItem.getQuantity() + " item(s)"
								+ " in your shopping cart. Quantity in stock is " + productInDB.getQuantityInStock());
			}
		}
		else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updatedQuantity);
		productInDB.setQuantityInStock(productInDB.getQuantityInStock() - quantity);
		productRepository.save(productInDB);
		cartRepository.save(cartItem);
		
		return quantity;
	}
	
	
	public List<CartItem> listCartItem(Customer customer){
		
		return cartRepository.findByCustomer(customer);
	}
	
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		
		cartRepository.updateQuantity(quantity, customer.getId(), productId);
		
		Product product = productRepository.findById(productId).get();
		float subtotal = (product.getDiscountPrice() * quantity);

		System.out.println(subtotal);
		return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		Product product = productRepository.findById(productId).get();

		cartRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}

	public void deleteByCustomer(Customer customer) {
		cartRepository.deleteByCustomer(customer.getId());
	}

	public CartItem findByProductIdAndCustomer(Product product, Customer customer) {
		return cartRepository.findByCustomerAndProduct(customer, product);
	}

}
