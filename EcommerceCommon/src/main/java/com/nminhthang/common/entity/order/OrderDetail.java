package com.nminhthang.common.entity.order;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nminhthang.common.entity.Country;
import com.nminhthang.common.entity.IdBasedEntity;
import com.nminhthang.common.entity.ShippingRate;
import com.nminhthang.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetail extends IdBasedEntity {
	
	  
	    private int quantity;
	    private float productCost;
	    private float shippingCost;
	    private float unitPrice;
	    private float subtotal;
	  
	  
		@ManyToOne
		@JoinColumn(name = "product_id")
		private Product product;
		
		@ManyToOne
		@JoinColumn(name = "order_id")
		private Order order;

		public OrderDetail() {

		}
		
		
		
}
