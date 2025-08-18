package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
