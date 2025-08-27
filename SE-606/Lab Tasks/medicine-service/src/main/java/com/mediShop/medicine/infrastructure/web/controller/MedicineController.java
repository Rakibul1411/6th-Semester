package com.mediShop.medicine.infrastructure.web.controller;

import com.mediShop.medicine.application.usecase.*;
import com.mediShop.medicine.application.dto.*;
import com.mediShop.medicine.domain.valueobject.MedicineType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class MedicineController {
    private final AddMedicineUseCase addMedicineUseCase;
    private final UpdateMedicineUseCase updateMedicineUseCase;
    private final GetMedicineUseCase getMedicineUseCase;
    private final SearchMedicinesUseCase searchMedicinesUseCase;
    private final GetExpiringMedicinesUseCase getExpiringMedicinesUseCase;
    private final DeleteMedicineUseCase deleteMedicineUseCase;

    public MedicineController(AddMedicineUseCase addMedicineUseCase,
                              UpdateMedicineUseCase updateMedicineUseCase,
                              GetMedicineUseCase getMedicineUseCase,
                              SearchMedicinesUseCase searchMedicinesUseCase,
                              GetExpiringMedicinesUseCase getExpiringMedicinesUseCase,
                              DeleteMedicineUseCase deleteMedicineUseCase) {
        this.addMedicineUseCase = addMedicineUseCase;
        this.updateMedicineUseCase = updateMedicineUseCase;
        this.getMedicineUseCase = getMedicineUseCase;
        this.searchMedicinesUseCase = searchMedicinesUseCase;
        this.getExpiringMedicinesUseCase = getExpiringMedicinesUseCase;
        this.deleteMedicineUseCase = deleteMedicineUseCase;
    }

    @PostMapping
    public ResponseEntity<MedicineResponse> addMedicine(@Valid @RequestBody AddMedicineRequest request) {
        MedicineResponse response = addMedicineUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Integer id,
                                                           @Valid @RequestBody UpdateMedicineRequest request) {
        request.setMedicineId(id);
        MedicineResponse response = updateMedicineUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicine(@PathVariable Integer id) {
        System.out.println("----------------------------------------------->>>>> inside getMedicine method with id: " + id);
        MedicineResponse response = getMedicineUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineResponse>> searchMedicines(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String batchNumber) {

        SearchMedicineRequest request = new SearchMedicineRequest();
        request.setName(name);
        if (type != null) {
            request.setType(MedicineType.valueOf(type.toUpperCase()));
        }
        request.setCategory(category);
        request.setBatchNumber(batchNumber);

        List<MedicineResponse> response = searchMedicinesUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        SearchMedicineRequest request = new SearchMedicineRequest();
        List<MedicineResponse> response = searchMedicinesUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<MedicineResponse>> getExpiringMedicines(
            @RequestParam(defaultValue = "30") Integer days) {
        List<MedicineResponse> response = getExpiringMedicinesUseCase.execute(days);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Integer id) {
        deleteMedicineUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}