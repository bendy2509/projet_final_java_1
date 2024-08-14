/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chcl.final_project;

import java.util.Date;

/**
 *
 * @author Bendy SERVILUS
 */
public class Pret {

    private static int compteur = 1;
    private int id;
    private String prenomEtudiant;
    private String nomEtudiant;
    private int niveau;
    private double montantEmprunte;
    private final double tauxInteret = 5.5;
    private double versementPeriodique;
    private Date datePret;
    private Boolean dejaRembourse;

    public Pret(String prenomEtudiant, String nomEtudiant, int niveau, double montantEmprunte, Date datePret) {
        this.id = compteur++;
        this.prenomEtudiant = prenomEtudiant;
        this.nomEtudiant = nomEtudiant;
        this.niveau = niveau;
        this.montantEmprunte = montantEmprunte;
        this.datePret = datePret;
        this.versementPeriodique = calculerVersementPeriodique();
        this.dejaRembourse = false;
    }

    private double calculerVersementPeriodique() {
        // Calcul du montant total à rembourser en ajoutant les intérêts au montant emprunté
        double montantTotal = montantEmprunte + (montantEmprunte * (tauxInteret / 100));

        // Calcul du versement périodique en divisant le montant total par le nombre de versements
        return montantTotal / 4;
    }

    // Setters
    public void setdejaRembourse(Boolean dejaRembourse) {
        this.dejaRembourse = dejaRembourse;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Boolean getDejaRembourse() {
        return dejaRembourse;
    }

    public String getPrenomEtudiant() {
        return prenomEtudiant;
    }

    public String getNomEtudiant() {
        return nomEtudiant;
    }

    public int getNiveau() {
        return niveau;
    }

    public double getMontantEmprunte() {
        return montantEmprunte;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public double getVersementPeriodique() {
        return versementPeriodique;
    }

    public Date getDatePret() {
        return datePret;
    }

    public void afficherDetailsPret() {
        System.out.println("ID du Prêt : " + id);
        System.out.println("Nom de l'étudiant : " + prenomEtudiant + " " + nomEtudiant);
        System.out.println("Niveau : " + niveau);
        System.out.println("Montant emprunté : " + montantEmprunte + " gourdes.");
        System.out.println("Taux d'intérêt : " + tauxInteret + " %");
        System.out.println("Montant total à rembourser : " + versementPeriodique * 4 + " gourdes");
        System.out.println("Versement périodique : " + versementPeriodique + " gourdes.");
        System.out.println("Date du prêt : " + datePret);
        System.out.println(true == dejaRembourse ? "Déjà remboursé : Oui" : "Déjà remboursé : Non");
    }
}
