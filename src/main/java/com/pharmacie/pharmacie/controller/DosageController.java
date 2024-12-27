package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.service.DosageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dosage")
public class DosageController {

    private final DosageService dosageService;

    @Autowired
    public DosageController(DosageService dosageService) {
        this.dosageService = dosageService;
    }

    @GetMapping("/pediatrique")
    public ResponseEntity<?> calculDosePediatrique(
            @RequestParam int age,
            @RequestParam double doseAdulte) {
        try {
            double dose = dosageService.calculatePediatricDose(age, doseAdulte);
            Map<String, Object> response = new HashMap<>();
            response.put("dose", dose);
            response.put("unite", "mg");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/poids")
    public ResponseEntity<?> calculDoseParPoids(
            @RequestParam double poids,
            @RequestParam double doseParKg,
            @RequestParam(required = false) Double doseMaximale) {
        try {
            double doseCalculee = dosageService.calculateDoseParPoids(poids, doseParKg);
            Map<String, Object> response = new HashMap<>();
            response.put("dose", doseCalculee);
            response.put("unite", "mg");

            if (doseMaximale != null) {
                boolean securitaire = dosageService.isDoseSecuritaire(doseCalculee, doseMaximale);
                response.put("securitaire", securitaire);
                response.put("doseMaximale", doseMaximale);
            }

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

