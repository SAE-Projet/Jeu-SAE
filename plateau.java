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

        // Initialisation : tout fermé par défaut
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                for (int k = 0; k < 5; k++) {
                    plateau[i][j][k] = 1;
                }
                plateau[i][j][4] = 0; // pas de joueur
            }
        }

        // Génération cohérente des murs
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {

                // GAUCHE
                if (j == 0) plateau[i][j][0] = 1;
                else plateau[i][j][0] = plateau[i][j - 1][2];

                // HAUT
                if (i == 0) plateau[i][j][1] = 1;
                else plateau[i][j][1] = plateau[i - 1][j][3];

                // DROITE
                if (j == 6) plateau[i][j][2] = 1;
                else plateau[i][j][2] = rdm.nextInt(2);

                // BAS
                if (i == 6) plateau[i][j][3] = 1;
                else plateau[i][j][3] = rdm.nextInt(2);
            }
        }

        // Placement des joueurs avec au moins 2 sorties
        if (idJoueurs.size() >= 1) { // J1 Haut-Gauche
            plateau[0][0][4] = idJoueurs.get(0);
            plateau[0][0][2] = 0; // droite
            plateau[0][0][3] = 0; // bas
        }

        if (idJoueurs.size() >= 2) { // J2 Haut-Droite
            plateau[0][6][4] = idJoueurs.get(1);
            plateau[0][6][0] = 0; // gauche
            plateau[0][6][3] = 0; // bas
        }

        if (idJoueurs.size() >= 3) { // J3 Bas-Gauche
            plateau[6][0][4] = idJoueurs.get(2);
            plateau[6][0][2] = 0; // droite
            plateau[6][0][1] = 0; // haut
        }

        if (idJoueurs.size() >= 4) { // J4 Bas-Droite
            plateau[6][6][4] = idJoueurs.get(3);
            plateau[6][6][0] = 0; // gauche
            plateau[6][6][1] = 0; // haut
        }

        return plateau;
    }

    public static void faireCoulisser(int[][][] plateau) {
        Random rdm = new Random();
        boolean estLigne = rdm.nextBoolean();
        int indice = rdm.nextInt(7);
        boolean direction = rdm.nextBoolean();

        int[] temp = new int[5];

        if (estLigne) {
            String sens = direction ? "DROITE" : "GAUCHE";
            System.out.println("\n>>> SÉISME : Ligne " + (indice + 1) + " vers la " + sens + " <<<");

            if (direction) { // VERS LA DROITE
                for (int k = 0; k < 5; k++) temp[k] = plateau[indice][6][k];
                for (int j = 6; j > 0; j--) {
                    for (int k = 0; k < 5; k++) plateau[indice][j][k] = plateau[indice][j-1][k];
                }
                for (int k = 0; k < 5; k++) plateau[indice][0][k] = temp[k];
            } else { // VERS LA GAUCHE
                for (int k = 0; k < 5; k++) temp[k] = plateau[indice][0][k];
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 5; k++) plateau[indice][j][k] = plateau[indice][j+1][k];
                }
                for (int k = 0; k < 5; k++) plateau[indice][6][k] = temp[k];
            }
        } else {
            String sens = direction ? "BAS" : "HAUT";
            System.out.println("\n>>> SÉISME : Colonne " + (indice + 1) + " vers le " + sens + " <<<");

            if (direction) { // VERS LE BAS
                for (int k = 0; k < 5; k++) temp[k] = plateau[6][indice][k];
                for (int i = 6; i > 0; i--) {
                    for (int k = 0; k < 5; k++) plateau[i][indice][k] = plateau[i-1][indice][k];
                }
                for (int k = 0; k < 5; k++) plateau[0][indice][k] = temp[k];
            } else { // VERS LE HAUT
                for (int k = 0; k < 5; k++) temp[k] = plateau[0][indice][k];
                for (int i = 0; i < 6; i++) {
                    for (int k = 0; k < 5; k++) plateau[i][indice][k] = plateau[i+1][indice][k];
                }
                for (int k = 0; k < 5; k++) plateau[6][indice][k] = temp[k];
            }
        }

        // --- RÉPARATION AVEC RÉPULSION DES JOUEURS ---
        for (int k = 0; k < 7; k++) {
            // 1. BORD HAUT (Ligne 0, position Haut [1])
            if (plateau[0][k][1] >= 2) {
                // Si un joueur est sur le bord haut, on le pousse au milieu de sa case
                plateau[0][k][4] = plateau[0][k][1];
                plateau[0][k][1] = 1; // On remet le mur
            } else {
                plateau[0][k][1] = 1;
            }

            // 2. BORD BAS (Ligne 6, position Bas [3])
            if (plateau[6][k][3] >= 2) {
                plateau[6][k][4] = plateau[6][k][3];
                plateau[6][k][3] = 1;
            } else {
                plateau[6][k][3] = 1;
            }

            // 3. BORD GAUCHE (Colonne 0, position Gauche [0])
            if (plateau[k][0][0] >= 2) {
                plateau[k][0][4] = plateau[k][0][0];
                plateau[k][0][0] = 1;
            } else {
                plateau[k][0][0] = 1;
            }

            // 4. BORD DROIT (Colonne 6, position Droite [2])
            if (plateau[k][6][2] >= 2) {
                plateau[k][6][4] = plateau[k][6][2];
                plateau[k][6][2] = 1;
            } else {
                plateau[k][6][2] = 1;
            }
        }
    }

    public static void afficherPlateau(int[][][] plateau) {
        final String MUR = "█";
        final String PION = "●";

        final String[] couleurs = {
                "\u001B[34m",  // bleu (joueur 1)
                "\u001B[31m",  // rouge (joueur 2)
                "\u001B[32m",  // vert (joueur 3)
                "\u001B[33m"   // jaune (joueur 4)
        };
        final String RESET = "\u001B[0m";
        final String couleurMur = "\u001B[38;5;94m"; // marron

        for (int i = 0; i < 7; i++) {
            // --- LIGNE HAUT DE LA TUILE (Coins + Mur/Joueur Haut) ---
            for (int j = 0; j < 7; j++) {
                System.out.print(couleurMur + MUR + RESET); // Coin haut-gauche (toujours mur)

                // Case HAUT (indice 1)
                int contenu = plateau[i][j][1];
                if (contenu >= 2)
                    System.out.print(couleurs[contenu - 2] + PION + RESET);
                else if (contenu == 1)
                    System.out.print(couleurMur + MUR + RESET);
                else
                    System.out.print(" ");

                System.out.print(couleurMur + MUR + RESET); // Coin haut-droit (toujours mur)
            }
            System.out.println();

            // --- LIGNE CENTRALE DE LA TUILE (Gauche | Milieu | Droite) ---
            for (int j = 0; j < 7; j++) {
                // Case GAUCHE (indice 0)
                int gauche = plateau[i][j][0];
                if (gauche >= 2)
                    System.out.print(couleurs[gauche - 2] + PION + RESET);
                else if (gauche == 1)
                    System.out.print(couleurMur + MUR + RESET);
                else
                    System.out.print(" ");

                // Case MILIEU (indice 4)
                int milieu = plateau[i][j][4];
                if (milieu >= 2)
                    System.out.print(couleurs[milieu - 2] + PION + RESET);
                else
                    System.out.print(" ");

                // Case DROITE (indice 2)
                int droite = plateau[i][j][2];
                if (droite >= 2)
                    System.out.print(couleurs[droite - 2] + PION + RESET);
                else if (droite == 1)
                    System.out.print(couleurMur + MUR + RESET);
                else
                    System.out.print(" ");
            }
            System.out.println();

            // --- LIGNE BAS DE LA TUILE (Coins + Mur/Joueur Bas) ---
            for (int j = 0; j < 7; j++) {
                System.out.print(couleurMur + MUR + RESET); // Coin bas-gauche

                // Case BAS (indice 3)
                int contenu = plateau[i][j][3];
                if (contenu >= 2)
                    System.out.print(couleurs[contenu - 2] + PION + RESET);
                else if (contenu == 1)
                    System.out.print(couleurMur + MUR + RESET);
                else
                    System.out.print(" ");

                System.out.print(couleurMur + MUR + RESET); // Coin bas-droit
            }
            System.out.println();
        }
        System.out.println();
    }
}
