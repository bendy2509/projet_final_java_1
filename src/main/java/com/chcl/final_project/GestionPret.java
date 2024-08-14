/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chcl.final_project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bendy SERVILUS
 */
public class GestionPret {

    private static List<Pret> prets = new ArrayList<>();

    public static void enregistrementPret(Scanner scanner) {
        String prenom, nom;
        int niveau;
        double montantEmprunte;

        System.out.print("Prénom de l'étudiant: ");
        prenom = scanner.nextLine();
        System.out.print("Nom de l'étudiant: ");
        nom = scanner.nextLine();

        OUTER:
        while (true) {
            System.out.print("Niveau (2, 3, 4 ou taper 0 pour quitter ): ");
            try {
                niveau = Integer.parseInt(scanner.nextLine());
                switch (niveau) {
                    case 2, 3, 4 -> {
                        break OUTER; //TODO: Non compris, generer par netbeans.
                    }
                    case 0 -> {
                        return;
                    }
                    default ->
                        System.out.println("Niveau invalide. Veuillez entrer 2, 3 ou 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
            }
        }

        while (true) {
            System.out.print("Montant emprunté : ");
            try {
                montantEmprunte = Double.parseDouble(scanner.nextLine());
                if (montantEmprunte > 0) {
                    break;
                } else {
                    System.out.println("Montant invalide. Veuillez entrer un montant supérieur à zéro.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre valide.");
            }
        }

        // Vérifier si l'étudiant n'a pas fini de rembourser le prêt, si il y en avait un en cours
        if (verificationPret(nom, prenom)) {
            System.out.println(prenom + " " + nom + " a déja un prêt en cours. Il/Elle devra d'abord fini de rembourser ce prêt.");
            System.out.println("L'enregistrement est annulé.");
            return;
        }

        ajouterPret(prenom, nom, niveau, montantEmprunte, new Date());
    }

    private static void ajouterPret(String prenomEtudiant, String nomEtudiant, int niveau, double montantEmprunte, Date datePret) {

        // On a deja faire cette verification, mais bon...
        if (niveau < 2 || niveau > 4) {
            System.out.println("Niveau invalide. Seuls les niveaux 2, 3 et 4 sont autorisés.");
            return;
        }
        if (montantEmprunte <= 0) {
            System.out.println("Le montant emprunté doit être supérieur à 0.");
            return;
        }

        Pret pret = new Pret(prenomEtudiant, nomEtudiant, niveau, montantEmprunte, datePret);
        prets.add(pret);

        // Ajouter au tableau
        System.out.println("Prêt ajouté avec succès.");
    }

    /**
     * Gère les prêts.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     */
    public void gererPrets(Scanner scanner) {
        while (true) {
            int choix;

            while (true) {
                System.out.println("1. Enregistrer un prêt");
                System.out.println("2. Afficher tous les prêts");
                System.out.println("3. Retour au menu principal");
                System.out.print("Faites votre choix (1-3): ");

                try {
                    choix = Integer.parseInt(scanner.nextLine());
                    if (choix >= 1 && choix <= 3) {
                        break;
                    } else {
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 3.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                }
            }

            switch (choix) {
                case 1 ->
                    enregistrementPret(scanner);
                case 2 ->
                    afficherTousLesPrets();
                case 3 -> {
                    return; // Retourne au menu principal
                }
//                default ->
//                    System.out.println("Choix invalide.");
            }
        }
    }

    public static void afficherTousLesPrets() {
        if (!prets.isEmpty()) {
            for (Pret pret : prets) {
                System.out.println("-------------------------------");
                pret.afficherDetailsPret();
                System.out.println("-------------------------------");
            }
        } else {
            System.out.println("Pas de prêt enregistré !");
        }

    }

    /**
     *
     * @param nom Nom de l'étudiant
     * @param prenom de de l'étudiant
     * @return true si il/elle a un pret rn cours, false sinon.
     */
    public static boolean verificationPret(String nom, String prenom) {
        for (Pret pret : prets) {
            if (nom.equalsIgnoreCase(pret.getNomEtudiant()) && pret.getPrenomEtudiant().equalsIgnoreCase(prenom)) {
                if (!pret.getDejaRembourse()) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     *
     * @param id ID pret
     * @return Une instance de la classe Pret ou null
     */
    public Pret getPretById(int id) {
        for (Pret pret : prets) {
            if (pret.getId() == id) {
                return pret;
            }
        }
        return null;
    }

}
