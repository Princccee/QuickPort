package com.quickport.deliveryapp.service;

import com.razorpay.Order;
import com.razorpay.Utils;
import com.razorpay.RazorpayClient;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private RazorpayClient client;

    @PostConstruct
    public void init() throws Exception {
//        this.client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        this.client = new RazorpayClient("rzp_test_R5gX1mDzVZinAS", "xWOvBeWjG5J9zEGUp8qGSxhw");
    }

//    // Dependency injection via constructor
//    public PaymentService(@Value("${razorpay.key.id}") String keyId,
//                          @Value("${razorpay.key.secret}") String keySecret) throws Exception {
//        this.client = new RazorpayClient(keyId, keySecret);
//    }

    public Order createOrder(int amount, String currency, String receipt) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Razorpay expects paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        return client.orders.create(orderRequest);
    }

    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;
            return Utils.verifySignature(data, signature, razorpayKeySecret);
        } catch (Exception e) {
            return false;
        }
    }
}
