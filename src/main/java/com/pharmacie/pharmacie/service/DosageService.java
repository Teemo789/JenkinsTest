package com.pharmacie.pharmacie.service;

import org.springframework.stereotype.Service;

@Service
public class DosageService {


    public double calculatePediatricDose(int ageEnAnnees, double doseAdulte) {
        if (ageEnAnnees < 0) {
            throw new IllegalArgumentException("L'âge ne peut pas être négatif");
        }
        if (doseAdulte < 0) {
            throw new IllegalArgumentException("La dose ne peut pas être négative");
        }
        return (ageEnAnnees * doseAdulte) / (ageEnAnnees + 12);
    }

    public double calculateDoseParPoids(double poidsEnKg, double doseParKg) {
        if (poidsEnKg <= 0) {
            throw new IllegalArgumentException("Le poids doit être supérieur à 0");
        }
        if (doseParKg < 0) {
            throw new IllegalArgumentException("La dose par kg ne peut pas être négative");
        }
        return poidsEnKg * doseParKg;
    }

    public boolean isDoseSecuritaire(double doseCalculee, double doseMaximale) {
        if (doseMaximale < 0) {
            throw new IllegalArgumentException("La dose maximale ne peut pas être négative");
        }
        return doseCalculee <= doseMaximale;
    }
}

