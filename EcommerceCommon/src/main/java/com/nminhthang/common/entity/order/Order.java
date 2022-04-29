package com.nminhthang.common.entity.order;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import com.nminhthang.common.entity.*;

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

	public Order(Integer id, Date orderTime, float productCost, float subtotal, float total) {
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
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
	public String getShippingAddress() {
		String address = firstName;

		if(lastName != null && !lastName.isEmpty()) {
			address += " " + lastName;
		}

		if(!addressLine1.isEmpty()) {
			address += ", " + addressLine1;
		}

		if(addressLine2 != null && !addressLine2.isEmpty()) {
			address += ", " + addressLine2;
		}

		if(!city.isEmpty()) {
			address += ", " + city;
		}

		if(state != null && !state.isEmpty()) {
			address += ", " + state;
		}

		address += ", " + country;

		if(!postalCode.isEmpty()) {
			address += ". Postal Code : " + postalCode;
		}

		if(!phoneNumber.isEmpty()) {
			address += ". Phone Number : " + phoneNumber;
		}

		return address;
	}


	@Transient
	public String getDestination() {
		String destination =  city + ", ";
		if (state != null && !state.isEmpty()) destination += state + ", ";
		destination += country;

		return destination;
	}

	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		setState(address.getState());
	}

	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliverDate);
	}

	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			this.deliverDate = dateFormatter.parse(dateString);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	@Transient
	public String getRecipientName() {
		String name = firstName;

		if (lastName != null && !lastName.isEmpty()) name += " " + lastName;
		return name;
	}

	@Transient
	public String getRecipientAddress() {
		String address = addressLine1;

		if (addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;

		if (!city.isEmpty()) address += ", " + city;

		if (state != null && !state.isEmpty()) address += ", " + state;

		address += ", " + country;

		if (!postalCode.isEmpty()) address += ". " + postalCode;

		return address;
	}

	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}

	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}

	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}

	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}

	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}

	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}

	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}

	public boolean hasStatus(OrderStatus status) {
		for (OrderTrack aTrack : orderTracks) {
			if (aTrack.getStatus().equals(status)) {
				return true;
			}
		}

		return false;
	}

	@Transient
	public String getProductNames() {
		String productName = "";

		productName = "<ul>";

		for (OrderDetail detail : orderDetails) {
			productName += "<li>" + detail.getProduct().getShortName() + "</li>";
		}

		productName += "</ul>";

		return productName;
	}
}
