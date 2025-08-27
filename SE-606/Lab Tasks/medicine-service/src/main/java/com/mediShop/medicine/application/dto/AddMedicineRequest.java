package com.mediShop.medicine.application.dto;

import com.mediShop.medicine.domain.valueobject.MedicineType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class AddMedicineRequest {
    @NotBlank(message = "Medicine name is required")
    @Size(min = 2, max = 100, message = "Medicine name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Medicine type is required")
    private MedicineType type;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @NotBlank(message = "Batch number is required")
    @Size(max = 50, message = "Batch number must not exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @NotNull(message = "Supplier ID is required")
    @Positive(message = "Supplier ID must be positive")
    private Integer supplierId;

    public AddMedicineRequest() {}

    public AddMedicineRequest(String name, MedicineType type, String category,
                              String batchNumber, LocalDate expiryDate,
                              String location, Integer supplierId) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.location = location;
        this.supplierId = supplierId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public MedicineType getType() { return type; }
    public void setType(MedicineType type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
}