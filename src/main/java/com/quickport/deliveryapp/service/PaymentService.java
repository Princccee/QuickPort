package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.RazorpayConfig;
import com.razorpay.Order;
import com.razorpay.Utils;
import com.razorpay.RazorpayClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    private final RazorpayClient client;
    private final String keySecret; // keep secret for signature verification

    public PaymentService(
            @Value("${razorpay.key.id}") String keyId,
            @Value("${razorpay.key.secret}") String keySecret
    ) throws Exception {
        this.client = new RazorpayClient(keyId, keySecret);
        this.keySecret = keySecret;
        log.info("✅ Razorpay client initialized with keyId={}", keyId);
    }

    public Order createOrder(int amount, String currency, String receipt) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Razorpay expects paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        log.info("💳 Creating payment order: {}", orderRequest);
        return client.orders.create(orderRequest);
    }

    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;
            return Utils.verifySignature(data, signature, keySecret);
        } catch (Exception e) {
            log.error("❌ Signature verification failed", e);
            return false;
        }
    }
}