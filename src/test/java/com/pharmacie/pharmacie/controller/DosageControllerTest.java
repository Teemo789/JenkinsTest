package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.service.DosageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

class DosageControllerTest {

    private DosageController dosageController;
    private DosageService dosageService;

    @BeforeEach
    void setUp() {
        dosageService = mock(DosageService.class);
        dosageController = new DosageController(dosageService);
    }

    @Test
    void testCalculDosePediatrique() {
        when(dosageService.calculatePediatricDose(6, 300.0)).thenReturn(100.0);

        ResponseEntity<?> response = dosageController.calculDosePediatrique(6, 300.0);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(100.0, body.get("dose"));
        assertEquals("mg", body.get("unite"));

        verify(dosageService).calculatePediatricDose(6, 300.0);
    }

    @Test
    void testCalculDoseParPoids() {
        when(dosageService.calculateDoseParPoids(70.0, 5.0)).thenReturn(350.0);
        when(dosageService.isDoseSecuritaire(350.0, 500.0)).thenReturn(true);

        ResponseEntity<?> response = dosageController.calculDoseParPoids(70.0, 5.0, 500.0);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(350.0, body.get("dose"));
        assertEquals("mg", body.get("unite"));
        assertTrue((Boolean) body.get("securitaire"));

        verify(dosageService).calculateDoseParPoids(70.0, 5.0);
        verify(dosageService).isDoseSecuritaire(350.0, 500.0);
    }
}

