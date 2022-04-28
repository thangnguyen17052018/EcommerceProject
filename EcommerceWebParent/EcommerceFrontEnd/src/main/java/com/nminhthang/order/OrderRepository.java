package com.nminhthang.order;

import com.nminhthang.common.entity.Customer;
import com.nminhthang.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    Page<Order> findAll(Integer customerId, Pageable pageable);

    Order findByIdAndCustomer(Integer id, Customer customer);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p " +
            "WHERE o.customer.id=?2 AND (p.name LIKE %?1% OR o.status LIKE %?1%)")
    Page<Order> findAll(String keyword, Integer customerId, Pageable pageable);

}
