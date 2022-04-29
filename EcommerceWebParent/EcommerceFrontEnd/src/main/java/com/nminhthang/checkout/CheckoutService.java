package com.nminhthang.checkout;

import com.nminhthang.common.entity.CartItem;
import com.nminhthang.common.entity.product.Product;
import com.nminhthang.common.entity.ShippingRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {

    private static final int DIM_DIVISOR = 139;
    private static final float CONVERSION_FACTOR_METER_TO_INCH = 39.3700787f;
    private static final float CONVERSION_FACTOR_KILOGRAM_TO_POUND = 2.20462262f;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
        float paymentTotal = productTotal + shippingCostTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);

        return checkoutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            float productLength = product.getLength() * CONVERSION_FACTOR_METER_TO_INCH;
            float productWidth = product.getLength() * CONVERSION_FACTOR_METER_TO_INCH;
            float productHeight = product.getHeight() * CONVERSION_FACTOR_METER_TO_INCH;
            
            float dimWeight = (productLength * productWidth * productHeight) / DIM_DIVISOR;
            
            float productWeigth = product.getWeight() * CONVERSION_FACTOR_KILOGRAM_TO_POUND;
            float finalWeight = productWeigth > dimWeight ? productWeigth : dimWeight;
            
            float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();

            item.setShippingCost(shippingCost);

            shippingCostTotal += shippingCost;
        }

        return shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;

        for (CartItem item : cartItems) {
            cost += item.getQuantity() * item.getProduct().getCost();
        }

        return cost;
    }

}
