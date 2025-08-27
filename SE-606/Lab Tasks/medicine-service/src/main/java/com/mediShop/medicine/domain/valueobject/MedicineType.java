package com.mediShop.medicine.domain.valueobject;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of medicine", enumAsRef = true)
public enum MedicineType {
    TABLET("Tablet"),
    SYRUP("Syrup"),
    INJECTION("Injection"),
    CAPSULE("Capsule"),
    OINTMENT("Ointment"),
    DROP("Drop");

    private final String displayName;

    MedicineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}