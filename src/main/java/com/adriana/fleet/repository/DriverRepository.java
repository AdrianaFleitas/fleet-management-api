package com.adriana.fleet.repository;

import com.adriana.fleet.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByLicenseNumberAndDeletedAtIsNull(String licenseNumber);

    List<Driver> findAllByDeletedAtIsNull();

    List<Driver> findAllByDeletedAtIsNotNull();

    Optional<Driver> findByIdAndDeletedAtIsNull(Long id);
}