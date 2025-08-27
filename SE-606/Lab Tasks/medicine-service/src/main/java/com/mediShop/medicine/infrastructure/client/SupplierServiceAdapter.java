package com.mediShop.medicine.infrastructure.adapter;

import com.mediShop.medicine.infrastructure.client.SupplierClient;
import com.mediShop.medicine.infrastructure.client.SupplierResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceAdapter {

    private final SupplierClient supplierClient;

    public SupplierResponse getSupplierById(Integer supplierId) {
        try {
            log.debug("Fetching supplier with ID: {}", supplierId);
            return supplierClient.getSupplierById(supplierId);
        } catch (Exception e) {
            log.error("Error fetching supplier with ID {}: {}", supplierId, e.getMessage());
            throw new RuntimeException("Unable to fetch supplier: " + e.getMessage());
        }
    }

    public boolean isSupplierActive(Integer supplierId) {
        try {
            SupplierResponse supplier = supplierClient.getSupplierById(supplierId);
            return supplier.isActive();
        } catch (Exception e) {
            log.error("Error checking if supplier is active: {}", e.getMessage());
            return false;
        }
    }

    public boolean supplierExists(Integer supplierId) {
        try {
            return supplierClient.supplierExists(supplierId);
        } catch (Exception e) {
            log.error("Error checking if supplier exists: {}", e.getMessage());
            return false;
        }
    }
}