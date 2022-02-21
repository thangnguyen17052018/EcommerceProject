package com.nminhthang.admin.user;

import com.nminhthang.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getUserById(Integer id);

    Long countById(Integer id);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1% OR u.id = ?1")
    Page<User> findAll(String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.enabled= :enabled WHERE u.id = :id")
    void updateEnabledStatus(Integer id, boolean enabled);

}
