package com.nminhthang.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
