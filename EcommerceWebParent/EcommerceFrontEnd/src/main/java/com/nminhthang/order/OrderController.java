package com.nminhthang.order;

import com.nminhthang.Utility;
import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.order.Order;
import com.nminhthang.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;

    @GetMapping("/orders")
    public String listFirstPage(Model model, HttpServletRequest request) {
        return listOrderByPage(model, request, 1, "orderTime", "desc", null);
    }

    @GetMapping("/orders/page/{pageNum}")
    private String listOrderByPage(Model model, HttpServletRequest request,
                                   @PathVariable(name = "pageNum") int pageNum,
                                   String sortField, String sortDir, String orderKeyword) {
        Customer customer = getAuthenticatedCustomer(request);

        Page<Order> orderPage = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
        List<Order> listOrders = orderPage.getContent();

        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("currenPage", pageNum);
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("orderKeyword", orderKeyword);
        model.addAttribute("moduleURL", "/orders");
        model.addAttribute("reverseSortOrder", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("currentPage", pageNum);

        long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        model.addAttribute("startCount", startCount);

        long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
        if (endCount > orderPage.getTotalElements()) {
            endCount = orderPage.getTotalElements();
        }
        model.addAttribute("endCount", endCount);

        return "orders/orders_customer";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(Model model, @PathVariable(name = "id") Integer id, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        Order order = orderService.getOrder(id, customer);
        model.addAttribute("order", order);

        return "orders/order_details_modal";
    }

}
