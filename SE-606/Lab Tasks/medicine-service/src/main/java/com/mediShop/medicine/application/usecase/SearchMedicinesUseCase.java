package com.mediShop.medicine.application.usecase;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.repository.MedicineRepository;
import com.mediShop.medicine.application.dto.SearchMedicineRequest;
import com.mediShop.medicine.application.dto.MedicineResponse;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchMedicinesUseCase implements UseCase<SearchMedicineRequest, List<MedicineResponse>> {
    private final MedicineRepository medicineRepository;

    public SearchMedicinesUseCase(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<MedicineResponse> execute(SearchMedicineRequest request) {
        List<Medicine> medicines;

        if (request.getName() != null && !request.getName().isEmpty()) {
            medicines = medicineRepository.findByName(request.getName());
        } else if (request.getType() != null) {
            medicines = medicineRepository.findByType(request.getType());
        } else if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            medicines = medicineRepository.findByCategory(request.getCategory());
        } else if (request.getBatchNumber() != null && !request.getBatchNumber().isEmpty()) {
            medicines = medicineRepository.findByBatchNumber(request.getBatchNumber());
        } else {
            medicines = medicineRepository.findAll();
        }

        return medicines.stream()
                .map(MedicineResponse::from)
                .collect(Collectors.toList());
    }
}