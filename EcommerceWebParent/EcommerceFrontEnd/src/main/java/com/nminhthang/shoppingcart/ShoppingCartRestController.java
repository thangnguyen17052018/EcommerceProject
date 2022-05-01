package com.nminhthang.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.product.Product;
import com.nminhthang.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nminhthang.Utility;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.exception.CustomerNotFoundException;
import com.nminhthang.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
	
	@Autowired private ShoppingCartService shoppingCartService;
	@Autowired private CustomerService customerService;
	@Autowired private ProductService productService;

	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		try {			
			Customer customer  = getAuthenticatedCustomer(request);
			Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);

			return String.valueOf(updatedQuantity);
		}
		catch (CustomerNotFoundException ex){
			return "You must login to add this product to cart.";
		}
		catch(ShoppingCartException ex) {
			return ex.getMessage();
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		try {			
			Customer customer  = getAuthenticatedCustomer(request);
			Product product = productService.findById(productId);
			CartItem cartItem = shoppingCartService.findByProductIdAndCustomer(product, customer);
			System.out.println("Quantity: " + quantity);
			System.out.println("Cart item quantity: " + cartItem.getQuantity());
			System.out.println("In stock: " + product.getQuantityInStock());
			int numberProductTakeInStock = quantity - cartItem.getQuantity();

			float subtotal = shoppingCartService.updateQuantity(productId, quantity, customer);
			product.setQuantityInStock(product.getQuantityInStock() - numberProductTakeInStock);
			productService.save(product);
			return String.valueOf(subtotal);
		}
		catch (CustomerNotFoundException ex){
			return "You must login to change quantity of cart.";
		}
				
	 }
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request) {
		
		try {
			Customer customer  = getAuthenticatedCustomer(request);
            Product product = productService.findById(productId);
            CartItem cartItem = shoppingCartService.findByProductIdAndCustomer(product, customer);

            product.setQuantityInStock(product.getQuantityInStock() + cartItem.getQuantity());
            productService.save(product);
            shoppingCartService.removeProduct(productId, customer);
			return "The product has been removed from your shopping cart.";
		}
		catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
		
	}
	
	
}
