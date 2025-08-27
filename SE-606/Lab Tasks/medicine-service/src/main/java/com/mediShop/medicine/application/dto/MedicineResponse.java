package com.mediShop.medicine.application.dto;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.valueobject.MedicineType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MedicineResponse {
    private Integer medicineId;
    private String name;
    private MedicineType type;
    private String category;
    private String batchNumber;
    private LocalDate expiryDate;
    private String location;
    private Integer supplierId;
    private boolean expired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MedicineResponse() {}

    public static MedicineResponse from(Medicine medicine) {
        MedicineResponse response = new MedicineResponse();
        response.medicineId = medicine.getMedicineId();
        response.name = medicine.getName();
        response.type = medicine.getType();
        response.category = medicine.getCategory();
        response.batchNumber = medicine.getBatchNumber();
        response.expiryDate = medicine.getExpiryDate();
        response.location = medicine.getLocation();
//        response.supplierId = medicine.getSupplierId();
        response.expired = medicine.isExpired();
        response.createdAt = medicine.getCreatedAt();
        response.updatedAt = medicine.getUpdatedAt();
        return response;
    }

    public Integer getMedicineId() { return medicineId; }
    public String getName() { return name; }
    public MedicineType getType() { return type; }
    public String getCategory() { return category; }
    public String getBatchNumber() { return batchNumber; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getLocation() { return location; }
    public Integer getSupplierId() { return supplierId; }
    public boolean isExpired() { return expired; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}