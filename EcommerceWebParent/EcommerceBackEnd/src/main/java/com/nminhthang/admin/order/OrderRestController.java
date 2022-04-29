package com.nminhthang.admin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RestController
public class OrderRestController {

	@Autowired
	private OrderService service;

	@PostMapping("/orders_shipper/update/{id}/{status}")
	public Response updateOrderStatus(@PathVariable("id") Integer orderId, @PathVariable("status") String status) {
		service.updateStatus(orderId, status);
		return new Response(orderId, status);
	}
}

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
class Response {
	private Integer orderId;
	private String status;


}