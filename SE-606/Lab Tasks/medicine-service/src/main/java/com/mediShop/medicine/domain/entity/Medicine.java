package com.mediShop.medicine.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mediShop.medicine.domain.valueobject.MedicineType;

public class Medicine {
    private final Integer medicineId;
    private String name;
    private MedicineType type;
    private String category;
    private String batchNumber;
    private LocalDate expiryDate;
    private String location;
    private Integer supplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Medicine(Integer medicineId, String name, MedicineType type,
                    String category, String batchNumber, LocalDate expiryDate,
                    String location, Integer supplierId) {
        this.medicineId = medicineId;
        this.name = validateName(name);
        this.type = type;
        this.category = category;
        this.batchNumber = batchNumber;
//        this.expiryDate = validateExpiryDate(expiryDate);
        this.expiryDate = expiryDate;
        this.location = location;
        this.supplierId = supplierId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Medicine create(String name, MedicineType type, String category,
                                  String batchNumber, LocalDate expiryDate,
                                  String location, Integer supplierId) {
        return new Medicine(null, name, type, category, batchNumber,
                expiryDate, location, supplierId);
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be empty");
        }
        return name.trim();
    }

//    private LocalDate validateExpiryDate(LocalDate expiryDate) {
//        if (expiryDate == null) {
//            throw new IllegalArgumentException("Expiry date cannot be null");
//        }
//        if (expiryDate.isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("Expiry date cannot be in the past");
//        }
//        return expiryDate;
//    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isNearExpiry(int daysThreshold) {
        return LocalDate.now().plusDays(daysThreshold).isAfter(expiryDate) ||
                LocalDate.now().plusDays(daysThreshold).isEqual(expiryDate);
    }

    public void updateDetails(String name, MedicineType type, String category,
                              String location, Integer supplierId) {
        this.name = validateName(name);
        this.type = type;
        this.category = category;
        this.location = location;
        this.supplierId = supplierId;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getMedicineId() { return medicineId; }
    public String getName() { return name; }
    public MedicineType getType() { return type; }
    public String getCategory() { return category; }
    public String getBatchNumber() { return batchNumber; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getLocation() { return location; }
    public Integer getSupplierId() { return supplierId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}