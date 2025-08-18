package com.quickport.deliveryapp;

import com.quickport.deliveryapp.dto.RazorpayConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class DeliveryappApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryappApplication.class, args);
	}

}
