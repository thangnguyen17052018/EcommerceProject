package com.nminhthang.checkout;

import com.nminhthang.Utility;
import com.nminhthang.address.AddressService;
import com.nminhthang.common.entity.Address;
import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.ShippingRate;
import com.nminhthang.customer.CustomerService;
import com.nminhthang.shipping.ShippingRateService;
import com.nminhthang.shoppingcart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired private CheckoutService checkoutService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private ShippingRateService shippingRateService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        } else {
            model.addAttribute("shippingAddress", customer.getAddress().toString());
            usePrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        if (shippingRate == null) {
            return "redirect:/cart";
        }

        List<CartItem> cartItems = shoppingCartService.listCartItem(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }



}
