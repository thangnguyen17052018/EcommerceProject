package com.nminhthang.admin.order;

import com.nminhthang.common.entity.order.OrderDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void testFindWithCategoryAndTimeBetween() throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse("2022-04-23");
        Date endTime = dateFormatter.parse("2022-04-29");

        List<OrderDetail> listOrderDetails = orderDetailRepository.findWithCategoryAndTimeBetween(startTime, endTime);

        assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail detail : listOrderDetails) {
            System.out.printf("%-30s | %d | %10.2f| %10.2f | %10.2f \n",
                    detail.getProduct().getCategory().getName(),
                    detail.getQuantity(), detail.getProductCost(),
                    detail.getShippingCost(), detail.getSubtotal());
        }
    }

    @Test
    public void testFindWithProductAndTimeBetween() throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormatter.parse("2022-04-23");
        Date endTime = dateFormatter.parse("2022-04-29");

        List<OrderDetail> listOrderDetails = orderDetailRepository.findWithProductAndTimeBetween(startTime, endTime);

        assertThat(listOrderDetails.size()).isGreaterThan(0);

        for (OrderDetail detail : listOrderDetails) {
            System.out.printf("%-70s | %d | %10.2f| %10.2f | %10.2f \n",
                    detail.getProduct().getShortName(),
                    detail.getQuantity(), detail.getProductCost(),
                    detail.getShippingCost(), detail.getSubtotal());
        }
    }

}
