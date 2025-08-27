package com.mediShop.medicine.infrastructure.persistence.repository;

import com.mediShop.medicine.infrastructure.persistence.entity.MedicineJpaEntity;
import com.mediShop.medicine.domain.valueobject.MedicineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface MedicineJpaRepository extends JpaRepository<MedicineJpaEntity, Integer> {
    List<MedicineJpaEntity> findByNameContainingIgnoreCase(String name);
    List<MedicineJpaEntity> findByType(MedicineType type);
    List<MedicineJpaEntity> findByCategoryContainingIgnoreCase(String category);
    List<MedicineJpaEntity> findByBatchNumber(String batchNumber);
    List<MedicineJpaEntity> findBySupplierId(Integer supplierId);
    boolean existsByBatchNumber(String batchNumber);

    @Query("SELECT m FROM MedicineJpaEntity m WHERE m.expiryDate < :date")
    List<MedicineJpaEntity> findExpiringBefore(@Param("date") LocalDate date);

    @Query("SELECT m FROM MedicineJpaEntity m WHERE m.expiryDate < CURRENT_DATE")
    List<MedicineJpaEntity> findExpiredMedicines();
}