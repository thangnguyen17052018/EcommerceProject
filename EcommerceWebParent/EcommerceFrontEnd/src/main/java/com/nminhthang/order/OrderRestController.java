package com.nminhthang.order;

import com.nminhthang.Utility;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.exception.CustomerNotFoundException;
import com.nminhthang.common.exception.OrderNotFoundException;
import com.nminhthang.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;

    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest, HttpServletRequest request) {
        System.out.println("Order ID: " + returnRequest.getOrderId());
        System.out.println("Reason: " + returnRequest.getReason());
        System.out.println("Note: " + returnRequest.getNote());

        Customer customer = null;
        try {
            customer = getAuthenticatedCustomer(request);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>("Authentication required",HttpStatus.BAD_REQUEST);
        }

        try {
            orderService.setOrderReturnRequested(returnRequest,customer);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }

        return customerService.getCustomerByEmail(email);
    }
}
