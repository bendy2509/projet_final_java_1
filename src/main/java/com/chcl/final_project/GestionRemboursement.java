package com.chcl.final_project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Gestion des remboursements pour les prêts.
 *
 * @author Bendy SERVILUS
 */
public class GestionRemboursement {

    private List<Remboursement> remboursements = new ArrayList<>();
    private java.sql.Date dateRemboursement;
    private static GestionPret gestionPret = new GestionPret();

    /**
     * Affiche le menu pour gérer les remboursements.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     */
    public void gererRemboursements(Scanner scanner) {
        while (true) {
            System.out.println("1. Enregistrer un remboursement");
            System.out.println("2. Afficher tous les remboursements");
            System.out.println("0. Retour au menu précédent");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consomme la nouvelle ligne

            switch (choix) {
                case 1 ->
                    enregistrerRemboursement(scanner);
                case 2 ->
                    afficherTousLesRemboursements();
                case 0 -> {
                    return;
                }
                default ->
                    System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Enregistre un remboursement pour un prêt.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     */
    public void enregistrerRemboursement(Scanner scanner) {
        int idPret = obtenirIdPret(scanner);
        if (idPret == -1) {
            return;
        }

        Pret pret = gestionPret.getPretById(idPret);
        double dette = calculerDette(pret);

        // Vérifie si la dette a déjà été entièrement payée
        if (dette <= 0) {
            //De toute façon ce sera toujours supérieur ou égal à zéro
            System.out.println("Cette dette a déjà été remboursée.");
            return;
        }

        System.out.println("Dette pour ce prêt : " + dette + " gourdes.");
        System.out.println("Versement périodique : " + pret.getVersementPeriodique());
        int versementNum = choixNomVersement(pret);

        if (versementDejaPaye(pret, versementNum)) {
            // TODO: Plus tard
            System.out.println("Vous avez déjà fini de payer le versement " + versementNum);
            return;
        }

        double montantPaye = obtenirMontant(scanner, dette);
        if (montantPaye == -1) {
            return;
        }

        // Pour différencier avec la variable global
        this.dateRemboursement = obtenirDateRemboursement(scanner);
        traiterRemboursement(idPret, pret, versementNum, montantPaye);
    }

    /**
     * Obtient l'ID du prêt à partir de l'entrée de l'utilisateur.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     * @return L'ID du prêt ou -1 si l'ID est invalide/N'est pas trouvé.
     */
    private int obtenirIdPret(Scanner scanner) {
        while (true) {
            System.out.print("ID du prêt: ");
            try {
                int idPret = Integer.parseInt(scanner.nextLine());
                if (gestionPret.getPretById(idPret) != null) {
                    return idPret;
                } else {
                    System.out.println("Pas de prêt avec cet ID.");
                    return -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vous devez entrer un nombre entier pour l'ID du prêt.");
            }
        }
    }

    /**
     * Calcule la dette totale restante pour un prêt.
     *
     * @param pret Le prêt pour lequel calculer la dette.
     * @return La dette totale restante.
     */
    private double calculerDette(Pret pret) {
        return (pret.getVersementPeriodique() * 4) - getTotalRemboursementByIdPret(pret.getId());
    }

    /**
     * Vérifie si le versement spécifié a déjà été payé.
     *
     * @param pret Le prêt associé au versement.
     * @param versementNum Le numéro du versement.
     * @return True si le versement a déjà été payé, sinon false.
     */
    private boolean versementDejaPaye(Pret pret, int versementNum) {
        return getTotalRemboursementByIdPret(pret.getId()) >= pret.getVersementPeriodique() * versementNum;
    }

    /**
     * Obtient le montant à rembourser de l'utilisateur.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     * @param dette La dette restante pour le prêt.
     * @return Le montant à rembourser ou -1 si le montant est invalide.
     */
    private double obtenirMontant(Scanner scanner, double dette) {
        while (true) {
            System.out.print("Montant à rembourser : ");
            try {
                double montantPaye = Double.parseDouble(scanner.nextLine());
                if (montantPaye > 0 && montantPaye <= dette) {
                    return montantPaye;
                } else {
                    System.out.println("Montant invalide. La dette est de : " + dette + ", "
                            + "vous ne pouvez pas payer plus ni un montant inférieur ou égal à zéro.");
                    return -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre valide.");
            }
        }
    }

    /**
     * Obtient la date de remboursement de l'utilisateur.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     * @return La date de remboursement sous forme d'objet java.sql.Date.
     */
    private java.sql.Date obtenirDateRemboursement(Scanner scanner) {
        while (true) {
            System.out.print("Date de remboursement (format yyyy-mm-dd): ");
            String dateString = scanner.nextLine();
            try {
                return java.sql.Date.valueOf(dateString);
            } catch (IllegalArgumentException e) {
                System.out.println("Date invalide. Format attendu : yyyy-mm-dd.");
            }
        }
    }

    /**
     * Permet à l'utilisateur de choisir le numéro du versement pour lequel il
     * souhaite rembourser.
     *
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     * @param pret Le prêt pour lequel choisir le versement.
     * @return Le numéro du versement sélectionné.
     */
    private int choixNomVersement(Pret pret) {
        double totalRembourse = getTotalRemboursementByIdPret(pret.getId());

        while (true) {
            System.out.println("Choix du versement : ");
            if (totalRembourse < pret.getVersementPeriodique()) {
                System.out.println("Versement 1");
                return 1;
            } else if (totalRembourse < pret.getVersementPeriodique() * 2) {
                System.out.println("Versement 2");
                return 2;
            } else if (totalRembourse < pret.getVersementPeriodique() * 3) {
                System.out.println("Versement 3");
                return 3;
            } else {
                System.out.println("Versement 4");
                return 4;
            }

        }
    }

    /**
     * Traite le remboursement en appliquant le montant payé aux versements en
     * cours et suivants si nécessaire.
     *
     * @param idPret L'ID du prêt pour lequel le remboursement est effectué/C'est un choix, je pourrais ne pas le mettre
     * et le récuperer à l'aide de l'intance du pret.(Pret.idPret())
     * @param pret Le prêt pour lequel le remboursement est effectué.
     * @param versementNum Le numéro du versement à partir duquel commencer.
     * @param montantPaye Le montant total payé par l'utilisateur.
     */
    private void traiterRemboursement(int idPret, Pret pret, int versementNum, double montantPaye) {
        double montantVersement = pret.getVersementPeriodique(); // Montant fixe de chaque versement
        double montantRestant = montantPaye; // Montant total que l'utilisateur a payé et qui doit être appliqué aux versements

        // Traite chaque versement jusqu'à ce que le montant payé/donné par l'User soit entièrement utilisé
        while (montantRestant > 0 && versementNum <= 4) {
            double montantDuVersement = montantVersement; // Montant à payer pour le versement en cours
            // Calcul du montant déjà payé pour ce versement en soustrayant les paiements précédents
            double montantDejaPaye = getTotalRemboursementByIdPret(idPret) - montantVersement * (versementNum - 1);

            // Calcule le montant qui peut être appliqué à ce versement, en fonction de ce qui a déjà été payé et du montant restant à payer
            double montantPourCeVersement = Math.min(montantDuVersement - montantDejaPaye, montantRestant);

            // Enregistre le remboursement pour ce versement
            String nomVersement = "Versement " + versementNum;
            ajouterRemboursement(idPret, nomVersement, montantPourCeVersement, this.dateRemboursement, gestionPret);

            // Soustrait le montant qui vient d'être appliqué au versement du montant restant à payer
            montantRestant -= montantPourCeVersement;

            // Passe au versement suivant pour appliquer l'éventuel excédent
            versementNum++;
        }
        
        // Si le prêt est remboursé, changer le statut du prêt
        if (getTotalRemboursementByIdPret(idPret) == montantVersement * 4){
            pret.setdejaRembourse(Boolean.TRUE);
        }

    }

    /**
     * Ajoute un remboursement à la liste des remboursements.
     *
     * @param idPret L'ID du prêt pour lequel ajouter le remboursement.
     * @param nomVersement Le nom du versement (ex: "Versement 1").
     * @param montantPaye Le montant payé pour le remboursement.
     * @param dateRemboursement La date à laquelle le remboursement a été
     * effectué.
     * @param gestionPret Instance de GestionPret pour récupérer les détails du
     * prêt.
     */
    public void ajouterRemboursement(int idPret, String nomVersement, double montantPaye, Date dateRemboursement, GestionPret gestionPret) {
        Pret pret = gestionPret.getPretById(idPret);

        // Vérifie si le prêt existe
        if (pret == null) {
            System.out.println("ID de prêt n'existe pas.");
            return;
        }

        // Vérifie si le montant payé est valide
        if (montantPaye <= 0) {
            System.out.println("Le montant payé doit être supérieur à 0.");
            return;
        }

        Remboursement remboursement = new Remboursement(idPret, nomVersement, montantPaye, dateRemboursement);
        remboursements.add(remboursement);
        System.out.println("Remboursement ajouté avec succès.");
    }

    /**
     * Affiche tous les remboursements enregistrés.
     */
    public void afficherTousLesRemboursements() {
        if (!remboursements.isEmpty()) {
            for (Remboursement remboursement : remboursements) {
                System.out.println("-------------------------------");
                remboursement.afficherDetailsRemboursement();
                System.out.println("-------------------------------");
            }
        } else {
            System.out.println("Pas de remboursement enregistré !");
        }
    }

    /**
     * Calcule le montant total déjà remboursé pour un prêt spécifique.
     *
     * @param idPret ID du prêt.
     * @return Le montant total déjà remboursé.
     */
    public double getTotalRemboursementByIdPret(int idPret) {
        double total = 0;
        for (Remboursement remboursement : remboursements) {
            if (remboursement.getIdPret() == idPret) {
                total += remboursement.getMontantPaye();
            }
        }
        return total;
    }
}
