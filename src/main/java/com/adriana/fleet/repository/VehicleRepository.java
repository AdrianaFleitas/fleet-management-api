package com.adriana.fleet.repository;

import com.adriana.fleet.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlateNumberAndDeletedAtIsNull(String plateNumber);

    List<Vehicle> findAllByDeletedAtIsNull();

    List<Vehicle> findAllByDeletedAtIsNotNull();

    Optional<Vehicle> findByIdAndDeletedAtIsNull(Long id);

}