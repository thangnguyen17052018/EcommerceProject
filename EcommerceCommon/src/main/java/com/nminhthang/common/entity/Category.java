package com.nminhthang.common.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private Set<Category> children = new HashSet<>();

    public static Category copyFull(Category category) {
        return Category.builder()
                        .name(category.getName())
                        .id(category.getId())
                        .alias(category.getAlias())
                        .image(category.getImage())
                        .parent(category.getParent())
                        .children(category.getChildren())
                        .enabled(category.isEnabled())
                        .hasChildren(category.getChildren().size() > 0)
                        .build();
    }

    public static Category copyFullWithFullName(Category category, String newName) {
        Category copyCategory = copyFull(category);
        copyCategory.setName(newName);

        return copyCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getImagePath() {
        System.out.println(image);
        if ("default.png".equals(image) || image == null) return "/images/default.png";
        return "/category-images/" + this.id + "/" + this.image;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Transient
    private boolean hasChildren;

}