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
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppinngCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		
		CartItem cartItem =  cartRepository.findByCustomerAndProduct(customer, product);
		
		if(cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			if(updatedQuantity > 5) {
				throw new ShoppinngCartException("Could not add more " + quantity + " item(s)"
						+ " because there's are already " + cartItem.getQuantity() + " item(s)"
								+ " in your shopping cart. Maximum allowed quantity is 5.");
			}
		}
		else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updatedQuantity);
		
		cartRepository.save(cartItem);
		
		return updatedQuantity;
	}
	
	
	public List<CartItem> listCartItem(Customer customer){
		
		return cartRepository.findByCustomer(customer);
	}
	
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		
		cartRepository.updateQuantity(quantity, customer.getId(), productId);
		
		Product product = productRepository.findById(productId).get();
		float subtotal = (float) (product.getDiscountPrice() * quantity);
		
		return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}

	public void deleteByCustomer(Customer customer) {
		cartRepository.deleteByCustomer(customer.getId());
	}
	
}
