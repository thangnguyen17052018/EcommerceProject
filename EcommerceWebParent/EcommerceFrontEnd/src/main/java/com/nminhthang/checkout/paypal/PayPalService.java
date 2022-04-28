package com.nminhthang.checkout.paypal;

import com.nminhthang.common.exception.PayPalApiException;
import com.nminhthang.setting.PaymentSettingBag;
import com.nminhthang.setting.SettingService;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class PayPalService {

    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AVJBjLNm6ktjunsX2Gy-3_itfE9ZCVa4MO1n3AagXg--iTqmZyi3VM7N25X2NKxMJNuisWi01FofILV0";
    private static final String CLIENT_SECRET = "EPeJQjApaoCZnLRnP5CZ0SAwCAdL3s4EM7v84Yt-hJV5bNkLKUoLzZc4JUf1rgX42Kasxv6lWtNrJrA5";

    @Autowired private SettingService settingService;
    @Autowired private RestTemplate restTemplate;

    public boolean validateOrder(String orderId) throws PayPalApiException {
        PayPalOrderResponse orderResponse = getOrderDetails(orderId);

        return orderResponse.validate(orderId);
    }

    private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalApiException {
        ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);

        HttpStatus statusCode = response.getStatusCode();

        throwExceptionForNonOKResponse(statusCode);

        PayPalOrderResponse orderResponse = response.getBody();
        return orderResponse;
    }

    private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String baseURL = paymentSettings.getURL();
        String requestURL = baseURL + GET_ORDER_API + orderId;
        String clientId = paymentSettings.getClientID();
        String clientSecret = paymentSettings.getClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValuedMap<String, String>> request = new HttpEntity<>(headers);
        System.out.println(requestURL);
        return restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
    }

    private void throwExceptionForNonOKResponse(HttpStatus statusCode) throws PayPalApiException {
        String message = null;

        if (!statusCode.is2xxSuccessful()) {
            switch (statusCode) {
                case NOT_FOUND:
                    message = "Order ID not found";
                case BAD_REQUEST:
                    message = "Bad Request to PayPal Checkout API";
                case INTERNAL_SERVER_ERROR:
                    message = "PayPal server error";
                default:
                    message = "PayPal returned non-OK status code";
                throw new PayPalApiException(message);
            }
        }
    }

}
