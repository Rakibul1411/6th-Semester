package com.mediShop.medicine.application.usecase;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.repository.MedicineRepository;
import com.mediShop.medicine.domain.exception.MedicineNotFoundException;
import com.mediShop.medicine.application.dto.MedicineResponse;
import org.springframework.stereotype.Service;

@Service
public class GetMedicineUseCase implements UseCase<Integer, MedicineResponse> {
    private final MedicineRepository medicineRepository;

    public GetMedicineUseCase(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public MedicineResponse execute(Integer medicineId) {
        System.out.println("----------------------------------------------->>>>> inside GetMedicineUseCase method before call ");
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new MedicineNotFoundException(medicineId));
        System.out.println("----------------------------------------------->>>>> inside GetMedicineUseCase method after call ");

        return MedicineResponse.from(medicine);
    }
}