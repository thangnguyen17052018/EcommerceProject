package com.nminhthang.common.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 256, nullable = false, unique = true)
    private String name;
    @Column(length = 256, nullable = false, unique = true)
    private String alias;
    @Column(length = 512, nullable = false, name = "short_description")
    private String shortDescription;
    @Column(length = 4096, nullable = false, name = "full_description")
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;
    @Column(name = "in_stock")
    private boolean inStock;

    private double cost;
    private double price;
    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    
    
    
    
    @Transient
    public String getImagePath() {
        return "/images/product-image.png";
    }

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    public void addDetail(String detailName, String detailValue) {
        this.details.add(new ProductDetail(detailName, detailValue, this));
    }

    public void addDetail(Integer id ,String detailName, String detailValue) {
        this.details.add(new ProductDetail(id, detailName, detailValue, this));
    }

    @Transient
    public String getMainImagePath() {
        if ("product-image.png".equals(mainImage) || mainImage == null || id == null || "".equals(mainImage)) return "/images/product-image.png";

        return "/product-images/" + this.id + "/" + this.mainImage;
    }

    @Transient
    public double getDiscountPrice() {
        if (discountPercent > 0) return price - (price * discountPercent/100);
        return price;
    }

    @Transient
    public String getShortName() {
        if (name.length() > 70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }

    public boolean containsImageName(String imageName) {
        Iterator<ProductImage> iterator = images.iterator();

        while (iterator.hasNext()) {
            ProductImage image = iterator.next();
            if (image.getName().equals(imageName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

	public Product(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", alias=" + alias + ", shortDescription=" + shortDescription
				+ ", fullDescription=" + fullDescription + ", createdTime=" + createdTime + ", updatedTime="
				+ updatedTime + ", enabled=" + enabled + ", inStock=" + inStock + ", cost=" + cost + ", price=" + price
				+ ", discountPercent=" + discountPercent + ", length=" + length + ", width=" + width + ", height="
				+ height + ", weight=" + weight + ", category=" + category + ", brand=" + brand + ", mainImage="
				+ mainImage + ", images=" + images + ", details=" + details + "]";
	}

	

}
