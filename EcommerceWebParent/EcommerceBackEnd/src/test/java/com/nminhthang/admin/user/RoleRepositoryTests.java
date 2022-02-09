package com.nminhthang.admin.user;

import com.nminhthang.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role adminRole = new Role("Admin", "manage everything");
        Role savedRoleAdmin = roleRepository.save(adminRole);
        assertThat(savedRoleAdmin.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role salesRole = new Role("Sales",
                "Manage product price, customers, shipping, orders and sales report");
        Role editorRole = new Role("Editor",
                "Manage categories, brands, products, articles and menus");
        Role shipperRole = new Role("Shipper",
                "View products, view orders, and update order status");
        Role assistantRole = new Role("Assistant", "Manage questions and reviews");

        roleRepository.saveAll(List.of(salesRole, editorRole, shipperRole, assistantRole));
    }

}
