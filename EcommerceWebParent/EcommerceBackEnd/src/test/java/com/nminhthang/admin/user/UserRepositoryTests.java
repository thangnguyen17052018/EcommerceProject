package com.nminhthang.admin.user;


import com.nminhthang.common.entity.Role;
import com.nminhthang.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private static void accept(User user) {
        System.out.println();
    }

    @Test
    public void testCreateNewUserWithOneRole(){
        Role adminRole = entityManager.find(Role.class, 1);
        User userNMinhThang = new User("nminhthang@tma.com.vn", "123456", "Thang", "Nguyen Minh");
        userNMinhThang.addRole(adminRole);

        User savedUser = userRepository.save(userNMinhThang);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles(){
        User userDTrungKien = new User("dtkien@gmail.com", "123456", "Kien", "Dang Trung");
        Role editorRole = new Role(3);
        Role assistantRole = new Role(5);

        userDTrungKien.addRole(editorRole);
        userDTrungKien.addRole(assistantRole);

        User savedUser = userRepository.save(userDTrungKien);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> userList = userRepository.findAll();
        userList.forEach(System.out::println);
    }

    @Test
    public void testGetUserById(){
        User userNMinhThang = userRepository.findById(1).get();

        assertThat(userNMinhThang).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userNMinhThang = userRepository.findById(1).get();

        userNMinhThang.setEnabled(true);
        userNMinhThang.setEmail("nminhthang1705@tma.com.vn");

        userRepository.save(userNMinhThang);
    }

    @Test
    public void testUpdateUserRoles(){
        User userDTKien = userRepository.findById(2).get();

        Role editorRole = new Role(3);
        Role salesRole = new Role(2);

        userDTKien.getRoles().remove(editorRole);
        userDTKien.addRole(salesRole);

        userRepository.save(userDTKien);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

}
