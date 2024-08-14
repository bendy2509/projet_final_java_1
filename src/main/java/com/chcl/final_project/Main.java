package com.chcl.final_project;

import java.util.Scanner;

/**
 * Classe principale pour le projet final
 *
 * @author Bendy
 */
public class Main {

    private static GestionPret gestionPret = new GestionPret();
    private static GestionRemboursement gestionRemboursement = new GestionRemboursement();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                MenuPrincipal();
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    traiterChoix(choice, scanner);
                } catch (NumberFormatException e) {
                    System.out.println("Vous devez rentrer un nombre");
                }
            }
        }
    }

    /**
     * Affiche le menu principal.
     */
    public static void MenuPrincipal() {
        System.out.println("********** Menu Principal **********");
        System.out.println("1. Gérer les prêts");
        System.out.println("2. Gérer les remboursements");
        System.out.println("3. Quitter");
        System.out.println("--- Faites votre choix (1-3)");
    }

    /**
     * Traite le choix de l'utilisateur.
     *
     * @param choice Le choix de l'utilisateur.
     * @param scanner Scanner pour lire l'entrée de l'utilisateur.
     */
    public static void traiterChoix(int choice, Scanner scanner) {
        switch (choice) {
            case 1 ->
                gestionPret.gererPrets(scanner);
            case 2 ->
                gestionRemboursement.gererRemboursements(scanner);
            case 3 ->
                quitterProgramme();
            default ->
                System.out.println("Choix invalide. Veuillez réessayer.");
        }
    }

    /**
     * Quitte le programme
     */
    public static void quitterProgramme() {
        System.out.println("Quitter le programme.");
        System.exit(0); // Termine le programme
    }
}
