package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.DeliveryRequestDTO;
import com.quickport.deliveryapp.dto.DeliveryResponse;
import com.quickport.deliveryapp.entity.Address;
import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.DeliveryStatus;
import com.quickport.deliveryapp.entity.User;
import com.quickport.deliveryapp.repository.AddressRepository;
import com.quickport.deliveryapp.repository.DeliveryRequestRepository;
import com.quickport.deliveryapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryRequestService {

    @Autowired private DeliveryRequestRepository deliveryRequestRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private UserRepository userRepository;

    // Create a new delivery order
    public DeliveryResponse createDeliveryRequest(DeliveryRequestDTO request, Long customerId){

        // get the pickup address
        Address pickup = addressRepository.findById(request.getPickupAddressId())
                .orElseThrow(() -> new RuntimeException("Pickup address not found"));

        // get the drop address
        Address drop = addressRepository.findById(request.getDropAddressId())
                .orElseThrow(() -> new RuntimeException("Drop address not found"));

        // Find the customer
        User customer = userRepository.findById(customerId)
                .orElseThrow(()-> new RuntimeException("Customer not found"));

        //Compute the fare
        double fare = 50 + 10 * request.getPackageWeight();

        // Create a fresh delivery request
        DeliveryRequest deliveryRequest = DeliveryRequest.builder()
                .packageDescription(request.getPackageDescription())
                .fare(fare)
                .status(DeliveryStatus.PENDING)
                .pickupTime(LocalDateTime.now().plusHours(1))
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .pickupAddress(pickup)
                .dropAddress(drop)
                .build();

        // Save the new record into the DB
        deliveryRequest = deliveryRequestRepository.save(deliveryRequest);

        return DeliveryResponse.builder()
                .deliveryId(deliveryRequest.getId())
                .packageDescription(deliveryRequest.getPackageDescription())
                .fare(deliveryRequest.getFare())
                .status(deliveryRequest.getStatus().name())
                .pickupTime(deliveryRequest.getPickupTime())
                .pickupStreet(pickup.getStreet())
                .pickupCity(pickup.getCity())
                .dropStreet(drop.getStreet())
                .dropCity(drop.getCity())
                .customerName(customer.getFullName())
                .customerPhone(customer.getPhone())
                .build();
    }

    // See all delivery order created by a customer
    public List<DeliveryResponse> getDeliveriesForCustomer(Long customerId) {
        return deliveryRequestRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get status of a delivery order
    public Optional<DeliveryResponse> getStatus(Long deliveryId) {
        return deliveryRequestRepository.findById(deliveryId)
                .map(this::mapToResponse);
    }

    private DeliveryResponse mapToResponse(DeliveryRequest d) {
        return DeliveryResponse.builder()
                .deliveryId(d.getId())
                .packageDescription(d.getPackageDescription())
                .status(String.valueOf(d.getStatus())) // delivery status
                .fare(d.getFare()) // fare
                .pickupTime(d.getPickupTime()) // pickup time
                .pickupStreet(d.getPickupAddress().getStreet()) // pickup street
                .pickupCity(d.getPickupAddress().getCity()) // pickup city
                .dropStreet(d.getDropAddress().getStreet()) // drop street
                .dropCity(d.getDropAddress().getCity()) // drop city
                .build();
    }

}
