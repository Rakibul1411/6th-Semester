package com.mediShop.medicine.domain.repository;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.valueobject.MedicineType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicineRepository {
    Medicine save(Medicine medicine);
    Optional<Medicine> findById(Integer medicineId);
    List<Medicine> findAll();
    List<Medicine> findByName(String name);
    List<Medicine> findByType(MedicineType type);
    List<Medicine> findByCategory(String category);
    List<Medicine> findByBatchNumber(String batchNumber);
    List<Medicine> findExpiringBefore(LocalDate date);
    List<Medicine> findExpiredMedicines();
    List<Medicine> findBySupplierId(Integer supplierId);
    boolean existsByBatchNumber(String batchNumber);
    void deleteById(Integer medicineId);
}