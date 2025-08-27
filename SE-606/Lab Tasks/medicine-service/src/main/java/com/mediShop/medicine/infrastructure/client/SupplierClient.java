package com.mediShop.medicine.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medishop-monolith", path = "/api/suppliers", fallback = SupplierClientFallback.class)
public interface SupplierClient {

    @GetMapping("/{supplierId}")
    SupplierResponse getSupplierById(@PathVariable Integer supplierId);

    @GetMapping("/exists/{supplierId}")
    boolean supplierExists(@PathVariable Integer supplierId);
}