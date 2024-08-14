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
public class Remboursement {

    private static int compteur = 1;
    private int id;
    private int idPret;
    private String nomVersement;
    private double montantPaye;
    private Date dateRemboursement;

    public Remboursement(int idPret, String nomVersement, double montantPaye, Date dateRemboursement) {
        this.id = compteur++;
        this.idPret = idPret;
        this.nomVersement = nomVersement;
        this.montantPaye = montantPaye;
        this.dateRemboursement = dateRemboursement;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdPret() {
        return idPret;
    }

    public String getNomVersement() {
        return nomVersement;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public Date getDateRemboursement() {
        return dateRemboursement;
    }

    public void afficherDetailsRemboursement() {
        System.out.println("ID du Remboursement: " + id);
        System.out.println("ID du Prêt: " + idPret);
        System.out.println("Nom du versement: " + nomVersement);
        System.out.println("Montant payé: " + montantPaye + " gourdes.");
        System.out.println("Date de remboursement: " + dateRemboursement);
    }
}
