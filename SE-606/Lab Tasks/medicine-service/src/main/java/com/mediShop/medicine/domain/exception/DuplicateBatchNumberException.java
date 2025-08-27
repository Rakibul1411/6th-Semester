package com.mediShop.medicine.domain.exception;

public class DuplicateBatchNumberException extends RuntimeException {
    public DuplicateBatchNumberException(String batchNumber) {
        super("Medicine with batch number already exists: " + batchNumber);
    }
}