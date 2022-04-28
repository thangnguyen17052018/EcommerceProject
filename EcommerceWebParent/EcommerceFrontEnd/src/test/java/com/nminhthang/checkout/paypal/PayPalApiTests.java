package com.nminhthang.checkout.paypal;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class PayPalApiTests {

    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AVJBjLNm6ktjunsX2Gy-3_itfE9ZCVa4MO1n3AagXg--iTqmZyi3VM7N25X2NKxMJNuisWi01FofILV0";
    private static final String CLIENT_SECRET = "EPeJQjApaoCZnLRnP5CZ0SAwCAdL3s4EM7v84Yt-hJV5bNkLKUoLzZc4JUf1rgX42Kasxv6lWtNrJrA5";

    @Test
    public void testGetOrderDetails() {
        String orderId = "1XF03830NL357460W";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValuedMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println(orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));
    }

}
