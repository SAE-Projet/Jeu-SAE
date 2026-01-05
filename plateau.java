import java.util.ArrayList;
import java.util.Random;

public class Plateau {
    /*  La fonction creationPlateau initialise un plateau à trois dimensions de taille 7x7 avec 5 positions par tuile.
        1ère dimension -> lignes
        2ème dimension -> colonnes
        3ème dimension -> cases ( 0 = gauche , 1 = haut , 2 = droite , 3 = bas , 4 = milieu )
     */
    public static int[][][] creationPlateau(ArrayList<Integer> idJoueurs) {
        Random rdm = new Random();
        int[][][] plateau = new int[7][7][5];
        int nbrdm;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                for (int l = 0; l < 5; l++) {
                    plateau[i][j][l] = -1;
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                plateau[i][j][4] = 0;
                for (int l = 0; l < 2; l++) {
                    do {
                        nbrdm = rdm.nextInt(4);
                    } while (plateau[i][j][nbrdm] != -1);
                    plateau[i][j][nbrdm] = 0;
                }
                // Coins
                if (i == 0 && j == 0) { // coin haut-gauche
                    plateau[i][j][0] = 1; // gauche fermé
                    plateau[i][j][1] = 1; // haut fermé
                    plateau[i][j][2] = 0; // droite ouvert
                    plateau[i][j][3] = 0; // bas ouvert
                }
                else if (i == 0 && j == 6) { // coin haut-droite
                    plateau[i][j][0] = 0; // gauche ouvert
                    plateau[i][j][1] = 1; // haut fermé
                    plateau[i][j][2] = 1; // droite fermé
                    plateau[i][j][3] = 0; // bas ouvert
                }
                else if (i == 6 && j == 0) { // coin bas-gauche
                    plateau[i][j][0] = 1; // gauche fermé
                    plateau[i][j][1] = 0; // haut ouvert
                    plateau[i][j][2] = 0; // droite ouvert
                    plateau[i][j][3] = 1; // bas fermé
                }
                else if (i == 6 && j == 6) { // coin bas-droite
                    plateau[i][j][0] = 0; // gauche ouvert
                    plateau[i][j][1] = 0; // haut ouvert
                    plateau[i][j][2] = 1; // droite fermé
                    plateau[i][j][3] = 1; // bas fermé
                }
                // Fermeture des bords
                if (i == 0) plateau[i][j][1] = 1; // ligne du haut
                if (i == 6) plateau[i][j][3] = 1; // ligne du bas
                if (j == 0) plateau[i][j][0] = 1; // colonne gauche
                if (j == 6) plateau[i][j][2] = 1; // colonne droite
                // Reste aléatoire
                for (int k = 0; k < 4; k++) {
                    if (plateau[i][j][k] == -1) {
                        plateau[i][j][k] = rdm.nextInt(2);
                    }
                }
            }
        }
        // Les IDs des joueurs sont déjà dans l'ordre (J1 en index 0, J2 en index 1, etc.)

        // J1 (index 0) : Haut-Gauche (0, 0)
        if (idJoueurs.size() >= 1) {
            plateau[0][0][4] = idJoueurs.get(0);
        }

        // J2 (index 1) : Haut-Droite (0, 6)
        if (idJoueurs.size() >= 2) {
            plateau[0][6][4] = idJoueurs.get(1);
        }

        // J3 (index 2) : Bas-Gauche (6, 0)
        if (idJoueurs.size() >= 3) {
            plateau[6][0][4] = idJoueurs.get(2);
        }

        // J4 (index 3) : Bas-Droite (6, 6)
        if (idJoueurs.size() >= 4) {
            plateau[6][6][4] = idJoueurs.get(3);
        }
        return plateau;
    }

    public static void afficherPlateau(int[][][] plateau) {
        final String MUR = "█";

        final String[] couleurs = {
            "\u001B[34m",  // bleu (joueur 1)
            "\u001B[31m", // rouge (joueur 2)
            "\u001B[32m", // vert (joueur 3)
            "\u001B[33m" // jaune (joueur 4)
        };
        final String RESET = "\u001B[0m";

        final String couleurMur = "\u001B[38;5;94m"; // marron

        for (int i = 0; i < 7; i++) {
            // Ligne du haut de chaque case (3 caractères par case : MUR + (espace|MUR) + MUR)
            for (int j = 0; j < 7; j++) {
                System.out.print(couleurMur + MUR + RESET);
                if (plateau[i][j][1] == 0)
                    System.out.print(" ");
                else
                    System.out.print(couleurMur + MUR + RESET);
                System.out.print(couleurMur + MUR + RESET);
            }
            System.out.println();

            // Ligne centrale (gauche - joueur - droite)
            for (int j = 0; j < 7; j++) {
                if (plateau[i][j][0] == 0)
                    System.out.print(" ");
                else
                    System.out.print(couleurMur + MUR + RESET);

                int joueur = plateau[i][j][4]; // position du joueur
                if (joueur == 0)
                    System.out.print(" ");
                else
                    System.out.print(couleurs[joueur-2] + "●" + RESET);

                if (plateau[i][j][2] == 0)
                    System.out.print(" ");
                else
                    System.out.print(couleurMur + MUR + RESET);
            }
            System.out.println();

            // Ligne du bas (même logique que la ligne du haut)
            for (int j = 0; j < 7; j++) {
                System.out.print(couleurMur + MUR + RESET);
                if (plateau[i][j][3] == 0)
                    System.out.print(" ");
                else
                    System.out.print(couleurMur + MUR + RESET);
                System.out.print(couleurMur + MUR + RESET);
            }
            System.out.println();
        }
        System.out.println();
    }
}

