package com.nminhthang.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import com.nminhthang.address.AddressService;
import com.nminhthang.common.entity.Address;
import com.nminhthang.common.entity.ShippingRate;
import com.nminhthang.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nminhthang.Utility;
import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.exception.CustomerNotFoundException;
import com.nminhthang.customer.CustomerService;

import java.util.List;

@Controller
public class ShoppingCartController {

	@Autowired private ShoppingCartService shoppingCartService;
	@Autowired private CustomerService customerService;

	@Autowired private ShippingRateService shippingRateService;

	@Autowired private AddressService addressService;

	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = shoppingCartService.listCartItem(customer);
		
		float estimatedTotal = 0.0F;
		for(CartItem item : cartItems) {
			estimatedTotal+=item.getSubtotal();
		}

		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;

		if (defaultAddress != null) {
			shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
		} else {
			usePrimaryAddressAsDefault = true;
			shippingRate = shippingRateService.getShippingRateForCustomer(customer);
		}

		boolean isShippingRateIsSupported = (shippingRate != null);

		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", isShippingRateIsSupported);
		model.addAttribute("cartItems",cartItems);
		model.addAttribute("estimatedTotal",estimatedTotal);
		
		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		return customerService.getCustomerByEmail(email);
	}
	
}
