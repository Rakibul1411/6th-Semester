package com.mediShop.medicine.application.usecase;

import com.mediShop.medicine.application.dto.AddMedicineRequest;
import com.mediShop.medicine.application.dto.MedicineResponse;
import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.exception.DuplicateBatchNumberException;
import com.mediShop.medicine.domain.repository.MedicineRepository;
import com.mediShop.medicine.infrastructure.adapter.SupplierServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddMedicineUseCase implements UseCase<AddMedicineRequest, MedicineResponse> {
    private final MedicineRepository medicineRepository;
    private final SupplierServiceAdapter supplierServiceAdapter;

    @Override
    public MedicineResponse execute(AddMedicineRequest request) {
        // Validate supplier exists and is active
        if (request.getSupplierId() != null) {
            if (!supplierServiceAdapter.supplierExists(request.getSupplierId())) {
                throw new IllegalArgumentException("Supplier with ID " + request.getSupplierId() + " doesn't exist");
            }

            if (!supplierServiceAdapter.isSupplierActive(request.getSupplierId())) {
                throw new IllegalArgumentException("Supplier with ID " + request.getSupplierId() + " is not active");
            }
        }

        // Check for duplicate batch number
        if (medicineRepository.existsByBatchNumber(request.getBatchNumber())) {
            throw new DuplicateBatchNumberException(request.getBatchNumber());
        }

        Medicine medicine = Medicine.create(
                request.getName(),
                request.getType(),
                request.getCategory(),
                request.getBatchNumber(),
                request.getExpiryDate(),
                request.getLocation(),
                request.getSupplierId()
        );

        Medicine savedMedicine = medicineRepository.save(medicine);
        return MedicineResponse.from(savedMedicine);
    }
}