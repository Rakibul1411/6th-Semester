package com.mediShop.medicine.infrastructure.persistence.mapper;

import com.mediShop.medicine.domain.entity.Medicine;
import com.mediShop.medicine.infrastructure.persistence.entity.MedicineJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public Medicine toDomainEntity(MedicineJpaEntity jpaEntity) {
        return new Medicine(
                jpaEntity.getMedicineId(),
                jpaEntity.getName(),
                jpaEntity.getType(),
                jpaEntity.getCategory(),
                jpaEntity.getBatchNumber(),
                jpaEntity.getExpiryDate(),
                jpaEntity.getLocation(),
                jpaEntity.getSupplierId()
        );
    }

    public MedicineJpaEntity toJpaEntity(Medicine domainEntity) {
        MedicineJpaEntity entity = new MedicineJpaEntity();
        entity.setMedicineId(domainEntity.getMedicineId());
        entity.setName(domainEntity.getName());
        entity.setType(domainEntity.getType());
        entity.setCategory(domainEntity.getCategory());
        entity.setBatchNumber(domainEntity.getBatchNumber());
        entity.setExpiryDate(domainEntity.getExpiryDate());
        entity.setLocation(domainEntity.getLocation());
        entity.setSupplierId(domainEntity.getSupplierId());
        return entity;
    }
}