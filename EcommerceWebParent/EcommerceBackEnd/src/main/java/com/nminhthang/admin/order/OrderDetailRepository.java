package com.nminhthang.admin.order;

import com.nminhthang.common.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderDetailRepository extends CrudRepository<OrderDetail, Integer> {

	@Query("SELECT NEW com.nminhthang.common.entity.order.OrderDetail(d.product.category.name, d.quantity,"
            + " d.productCost, d.shippingCost, d.subtotal, d.product)"
            + " FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
    List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);

    @Query("SELECT NEW com.nminhthang.common.entity.order.OrderDetail(d.quantity, d.product.name,"
            + " d.productCost, d.shippingCost, d.subtotal)"
            + " FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
    List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime);
}
