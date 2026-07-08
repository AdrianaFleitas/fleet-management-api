package com.adriana.fleet.repository;

import com.adriana.fleet.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlateNumber(String plateNumber);

}