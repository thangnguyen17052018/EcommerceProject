package com.nminhthang.common.entity;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name="cart_item")
public class CartItem extends IdBasedEntity {
	
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	
	private int quantity;
	
	
	
	public CartItem() {
	}



	@Override
	public String toString() {
		return "CartItem [id=" + id + ", customer=" + customer.getFullName() + ", product=" + product.getShortName() + ", quantity=" + quantity
				+ "]";
	}
	
	
	@Transient
	public float getSubtotal() {
		return (float) (product.getDiscountPrice() * quantity);
	}
	
	
}
