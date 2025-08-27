package com.mediShop.medicine.infrastructure.client;

import org.springframework.stereotype.Component;

@Component
public class SupplierClientFallback implements SupplierClient {

    @Override
    public SupplierResponse getSupplierById(Integer supplierId) {
        SupplierResponse fallback = new SupplierResponse();
        fallback.setId(supplierId);
        fallback.setCompanyName("Unknown (Service Unavailable)");
        fallback.setActive(true); // Default to true to prevent blocking operations
        return fallback;
    }

    @Override
    public boolean supplierExists(Integer supplierId) {
        return true; // Default to true to prevent blocking operations
    }
}