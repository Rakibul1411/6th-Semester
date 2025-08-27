package com.mediShop.medicine.infrastructure.persistence.repository;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.domain.repository.MedicineRepository;
import com.mediShop.medicine.domain.valueobject.MedicineType;
import com.mediShop.medicine.infrastructure.persistence.entity.MedicineJpaEntity;
import com.mediShop.medicine.infrastructure.persistence.mapper.MedicineMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MedicineRepositoryImpl implements MedicineRepository {
    private final MedicineJpaRepository jpaRepository;
    private final MedicineMapper mapper;

    public MedicineRepositoryImpl(MedicineJpaRepository jpaRepository, MedicineMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Medicine save(Medicine medicine) {
        MedicineJpaEntity entity = mapper.toJpaEntity(medicine);
        MedicineJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Medicine> findById(Integer medicineId) {
        System.out.println("----------------------------------------------->>>>> inside MedicineRepositoryImpl.findById method with id: " + medicineId);
        return jpaRepository.findById(medicineId)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Medicine> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findByName(String name) {
        return jpaRepository.findByNameContainingIgnoreCase(name).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findByType(MedicineType type) {
        return jpaRepository.findByType(type).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findByCategory(String category) {
        return jpaRepository.findByCategoryContainingIgnoreCase(category).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findByBatchNumber(String batchNumber) {
        return jpaRepository.findByBatchNumber(batchNumber).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findExpiringBefore(LocalDate date) {
        return jpaRepository.findExpiringBefore(date).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findExpiredMedicines() {
        return jpaRepository.findExpiredMedicines().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> findBySupplierId(Integer supplierId) {
        return jpaRepository.findBySupplierId(supplierId).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByBatchNumber(String batchNumber) {
        return jpaRepository.existsByBatchNumber(batchNumber);
    }

    @Override
    public void deleteById(Integer medicineId) {
        jpaRepository.deleteById(medicineId);
    }
}