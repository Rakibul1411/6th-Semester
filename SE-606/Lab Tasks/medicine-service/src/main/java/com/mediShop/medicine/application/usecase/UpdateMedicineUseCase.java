package com.mediShop.medicine.application.usecase;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.repository.MedicineRepository;
import com.mediShop.medicine.domain.exception.MedicineNotFoundException;
import com.mediShop.medicine.application.dto.UpdateMedicineRequest;
import com.mediShop.medicine.application.dto.MedicineResponse;
import org.springframework.stereotype.Service;

@Service
public class UpdateMedicineUseCase implements UseCase<UpdateMedicineRequest, MedicineResponse> {
    private final MedicineRepository medicineRepository;

    public UpdateMedicineUseCase(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public MedicineResponse execute(UpdateMedicineRequest request) {
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new MedicineNotFoundException(request.getMedicineId()));

        medicine.updateDetails(
                request.getName(),
                request.getType(),
                request.getCategory(),
                request.getLocation(),
                request.getSupplierId()
        );

        Medicine updatedMedicine = medicineRepository.save(medicine);
        return MedicineResponse.from(updatedMedicine);
    }
}