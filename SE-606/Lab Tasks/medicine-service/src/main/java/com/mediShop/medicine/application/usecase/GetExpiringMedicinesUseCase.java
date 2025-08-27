package com.mediShop.medicine.application.usecase;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.repository.MedicineRepository;
import com.mediShop.medicine.application.dto.MedicineResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetExpiringMedicinesUseCase implements UseCase<Integer, List<MedicineResponse>> {
    private final MedicineRepository medicineRepository;

    public GetExpiringMedicinesUseCase(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<MedicineResponse> execute(Integer daysThreshold) {
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        List<Medicine> expiringMedicines = medicineRepository.findExpiringBefore(thresholdDate);

        return expiringMedicines.stream()
                .map(MedicineResponse::from)
                .collect(Collectors.toList());
    }
}