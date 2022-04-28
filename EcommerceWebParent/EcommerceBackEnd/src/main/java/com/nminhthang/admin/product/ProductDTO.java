package com.nminhthang.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class ProductDTO {
	private String name;
	private String imagePath;
	private double price;
	private double cost;


}
