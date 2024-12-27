package com.pharmacie.pharmacie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DosageServiceTest {

    private DosageService dosageService;

    @BeforeEach
    void setUp() {
        dosageService = new DosageService();
    }

    @Test
    void testCalculatePediatricDose() {
        // Pour un enfant de 6 ans avec une dose adulte de 300mg
        assertEquals(100.0, dosageService.calculatePediatricDose(6, 300), 0.01);

        // Pour un nouveau-né (0 ans)
        assertEquals(0.0, dosageService.calculatePediatricDose(0, 300), 0.01);
    }

    @Test
    void testCalculatePediatricDoseInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            dosageService.calculatePediatricDose(-1, 300);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            dosageService.calculatePediatricDose(6, -300);
        });
    }

    @Test
    void testCalculateDoseParPoids() {
        // Pour un patient de 70kg avec une dose de 5mg/kg
        assertEquals(350.0, dosageService.calculateDoseParPoids(70, 5), 0.01);
    }

    @Test
    void testCalculateDoseParPoidsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            dosageService.calculateDoseParPoids(0, 5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            dosageService.calculateDoseParPoids(70, -5);
        });
    }

    @Test
    void testIsDoseSecuritaire() {
        assertTrue(dosageService.isDoseSecuritaire(300, 500));
        assertFalse(dosageService.isDoseSecuritaire(600, 500));
    }

    @Test
    void testIsDoseSecuritaireInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            dosageService.isDoseSecuritaire(300, -500);
        });
    }
}

