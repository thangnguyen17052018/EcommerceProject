package com.nminhthang.common.entity.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.IdBasedEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends IdBasedEntity {
	
		  
		  
		@Column(name = "first_name", nullable = false, length = 45)
		private String firstName;
		
		@Column(name = "last_name", nullable = false, length = 45)
		private String lastName;
		
		@Column(name = "phone_number", nullable = false, length = 15)
		private String phoneNumber;
		
		@Column(name = "address_line_1", nullable = false, length = 64)
		private String addressLine1;
		
		@Column(name = "address_line_2", length = 64)
		private String addressLine2;
		
		@Column(nullable = false, length = 45)
		private String city;
		
		@Column(nullable = false, length = 45)
		private String state;
		
		@Column(name = "postal_code", nullable = false, length = 10)
		private String postalCode;
		
		@Column(nullable = false, length = 45)
		private String country;
		
		private Date orderTime;
		
		private float shippingCost;
		private float productCost;
		private float subtotal;
		private float tax;
		private float total;
		
		private int deliverDays;
		private Date deliverDate;
		
		@Enumerated(EnumType.STRING)
		private PaymentMethod paymentMethod;
		
		@Enumerated(EnumType.STRING)
		private OrderStatus status;
		
		@ManyToOne
		@JoinColumn(name = "customer_id")
		private Customer customer;
		
		
		@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
		private Set<OrderDetail> orderDetails = new HashSet<>();

		
		@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
		@OrderBy("updatedTime ASC")
		private List<OrderTrack> orderTracks = new ArrayList<>();
		
		

		public Order() {
			
		}
		
		public void copyAddressFromCustomer() {
			setFirstName(customer.getFirstName());
			setLastName(customer.getLastName());
			setPhoneNumber(customer.getPhoneNumber());
			setAddressLine1(customer.getAddressLine1());
			setAddressLine2(customer.getAddressLine2());
			setCity(customer.getCity());
			setCountry(customer.getCountry().getName());
			setPostalCode(customer.getPostalCode());
			setState(customer.getState());		
		}

		@Override
		public String toString() {
			return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", status="
					+ status + ", customer=" + customer.getFullName() + "]";
		}
		
		
		@Transient
		public String getDestination() {
			String destination =  city + ", ";
			if (state != null && !state.isEmpty()) destination += state + ", ";
			destination += country;
			
			return destination;
		}
		
		@Transient
		public String getDeliverDateOnForm() {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormatter.format(this.deliverDate);
		}
		
}
