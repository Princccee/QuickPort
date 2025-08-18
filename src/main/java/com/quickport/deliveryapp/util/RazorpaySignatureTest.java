package com.quickport.deliveryapp.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class RazorpaySignatureTest {

    public static String generateSignature(String orderId, String paymentId, String secret) throws Exception {
        String data = orderId + "|" + paymentId;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);

        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return Hex.encodeHexString(hash);
    }

    public static void main(String[] args) throws Exception {
        String orderId = "order_R5xtsVQdtCIeev";
        String paymentId = "pay_123456";
        String secret = "xWOvBeWjG5J9zEGUp8qGSxhw"; // your razorpay.key.secret

        String signature = generateSignature(orderId, paymentId, secret);
        System.out.println("✅ Valid signature: " + signature);
    }
}
