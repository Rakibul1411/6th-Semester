package com.mediShop.medicine.domain.exception;

public class MedicineNotFoundException extends RuntimeException {
    public MedicineNotFoundException(String message) {
        super(message);
    }

    public MedicineNotFoundException(Integer medicineId) {
        super("Medicine not found with ID: " + medicineId);
    }
}