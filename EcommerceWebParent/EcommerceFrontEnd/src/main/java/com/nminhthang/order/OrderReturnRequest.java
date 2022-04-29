package com.nminhthang.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturnRequest {

    private Integer orderId;
    private String reason;
    private String note;


}
