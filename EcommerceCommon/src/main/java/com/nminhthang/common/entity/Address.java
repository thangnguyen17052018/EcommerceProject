package com.nminhthang.common.entity;


import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

	@Column(name = "first_name", nullable = false, length = 45)
	protected String firstName;
	
	@Column(name = "last_name", nullable = false, length = 45)
	protected String lastName;
	
	@Column(name = "phone_number", nullable = false, length = 15)
	protected String phoneNumber;
	
	@Column(name = "address_line_1", nullable = false, length = 64)
	protected String addressLine1;
	
	@Column(name = "address_line_2", length = 64)
	protected String addressLine2;
	
	@Column(nullable = false, length = 45)
	protected String city;
	
	@Column(nullable = false, length = 45)
	protected String state;
	
	@Column(name = "postal_code", nullable = false, length = 10)
	protected String postalCode;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(name = "default_address")
	private boolean defaultForShipping;
	
	

	
}
