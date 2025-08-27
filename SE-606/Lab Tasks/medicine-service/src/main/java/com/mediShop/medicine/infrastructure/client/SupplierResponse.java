package com.mediShop.medicine.infrastructure.client;

import lombok.Data;

@Data
public class SupplierResponse {
    private Integer id;
    private String companyName;
    private String phone;
    private String email;
    private boolean active;
}