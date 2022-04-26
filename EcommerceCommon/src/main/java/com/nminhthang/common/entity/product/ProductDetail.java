package com.nminhthang.common.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetail  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

    @Column(nullable = true, length = 255)
    private String name;

    @Column(nullable = true, length = 255)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductDetail(String name, String value, Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }
}
