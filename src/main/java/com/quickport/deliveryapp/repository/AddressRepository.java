package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
