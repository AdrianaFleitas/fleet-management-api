package com.adriana.fleet.service;

import com.adriana.fleet.dto.DriverRequest;
import com.adriana.fleet.dto.DriverResponse;
import com.adriana.fleet.entity.Driver;
import com.adriana.fleet.exception.DuplicateResourceException;
import com.adriana.fleet.exception.ResourceNotFoundException;
import com.adriana.fleet.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public DriverResponse createDriver(DriverRequest request) {
        if (driverRepository.existsByLicenseNumberAndDeletedAtIsNull(request.getLicenseNumber())) {
            throw new DuplicateResourceException("License number already exists");
        }

        Driver driver = new Driver(
                request.getFirstName(),
                request.getLastName(),
                request.getLicenseNumber(),
                request.getPhone(),
                request.getStatus()
        );

        Driver savedDriver = driverRepository.save(driver);

        return mapToResponse(savedDriver);
    }

    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<DriverResponse> getDeletedDrivers() {
        return driverRepository.findAllByDeletedAtIsNotNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        return mapToResponse(driver);
    }

    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        boolean licenseNumberChanged = !driver.getLicenseNumber().equals(request.getLicenseNumber());

        if (licenseNumberChanged &&
                driverRepository.existsByLicenseNumberAndDeletedAtIsNull(request.getLicenseNumber())) {
            throw new DuplicateResourceException("License number already exists");
        }

        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setPhone(request.getPhone());
        driver.setStatus(request.getStatus());

        Driver updatedDriver = driverRepository.save(driver);

        return mapToResponse(updatedDriver);
    }

    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        driver.setDeletedAt(LocalDateTime.now());

        driverRepository.save(driver);
    }

    public DriverResponse restoreDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if (driver.getDeletedAt() == null) {
            throw new DuplicateResourceException("Driver is not deleted");
        }

        driver.setDeletedAt(null);

        Driver restoredDriver = driverRepository.save(driver);

        return mapToResponse(restoredDriver);
    }

    private DriverResponse mapToResponse(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getLicenseNumber(),
                driver.getPhone(),
                driver.getStatus(),
                driver.getCreatedAt(),
                driver.getUpdatedAt(),
                driver.getDeletedAt()
        );
    }
}