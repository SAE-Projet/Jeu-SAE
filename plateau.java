import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
public class Plateau {
    /*  La fonction creationPlateau initialise un plateau à trois dimensions de taille 7x7 avec 5 positions par tuile.
        1ère dimension -> lignes
        2ème dimension -> colonnes
        3ème dimension -> cases ( 0 = gauche , 1 = haut , 2 = droite , 3 = bas , 4 = milieu )
     */
    public static int[][][] creationPlateau(ArrayList<Integer> idJoueurs, ArrayList<ArrayList<Integer>> itemsJoueurs) {
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

        // Initialisation : tout fermé par défaut
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                for (int k = 0; k < 5; k++) {
                    plateau[i][j][k] = 1;
                }
                plateau[i][j][4] = 0;
            }
        }

        // Génération avec probabilité réduite (environ 25% de murs)
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {

                // GAUCHE : Cohérence avec le voisin de gauche
                if (j == 0) {
                    plateau[i][j][0] = 1;
                } else {
                    plateau[i][j][0] = plateau[i][j - 1][2];
                }

                // HAUT : Cohérence avec le voisin du haut
                if (i == 0) {
                    plateau[i][j][1] = 1;
                } else {
                    plateau[i][j][1] = plateau[i - 1][j][3];
                }

                // DROITE : 25% de chance d'avoir un mur
                if (j == 6) {
                    plateau[i][j][2] = 1;
                } else {
                    float chanceMur = rdm.nextFloat();
                    if (chanceMur < 0.25f) {
                        plateau[i][j][2] = 1;
                    } else {
                        plateau[i][j][2] = 0;
                    }
                }

                // BAS : 25% de chance d'avoir un mur
                if (i == 6) {
                    plateau[i][j][3] = 1;
                } else {
                    float chanceMur = rdm.nextFloat();
                    if (chanceMur < 0.25f) {
                        plateau[i][j][3] = 1;
                    } else {
                        plateau[i][j][3] = 0;
                    }
                }
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

        // faire spawn les items
        for (int i = 0; i < idJoueurs.size(); i++) {
            ArrayList<Integer> ItemsParJoueur = itemsJoueurs.get(i);
            for (Integer itemID : ItemsParJoueur) {
                boolean position = false;
                while (!position) {
                    int itemLigne = rdm.nextInt(7);
                    int itemColonne = rdm.nextInt(7);

                    boolean zoneValide = false;
                    // Joueurs 1 et 2 (Haut) : on évite les lignes 0 et 1
                    if (i < 2) {
                        if (itemLigne >= 2) zoneValide = true;
                    }
                    // Joueurs 3 et 4 (Bas) : on évite les lignes 5 et 6
                    else {
                        if (itemLigne <= 4) zoneValide = true;
                    }

                    if (zoneValide && plateau[itemLigne][itemColonne][4] == 0) {
                        plateau[itemLigne][itemColonne][4] = -(itemID + 10);
                        position = true;
                    }
                }
            }
        }
        return plateau;
    }

    public static void faireCoulisser(int[][][] plateau) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- PHASE DE SÉISME : MODIFICATION DU LABYRINTHE ---");

        // 1. Choix du type : Ligne ou Colonne
        int choixType = 0;
        while (choixType != 1 && choixType != 2) {
            System.out.print("Voulez-vous déplacer une LIGNE (1) ou une COLONNE (2) ? ");
            if (scanner.hasNextInt()) {
                choixType = scanner.nextInt();
            } else {
                scanner.next(); // vide l'entrée en cas d'erreur
            }
        }
        boolean estLigne = (choixType == 1);

        // 2. Choix de l'indice (1 à 7 pour l'utilisateur, 0 à 6 pour le code)
        int indice = -1;
        while (indice < 0 || indice > 6) {
            System.out.print("Entrez le numéro de la " + (estLigne ? "ligne" : "colonne") + " (1 à 7) : ");
            if (scanner.hasNextInt()) {
                indice = scanner.nextInt() - 1;
            } else {
                scanner.next();
            }
        }

        // 3. Choix de la direction
        int choixDir = 0;
        String dir1 = estLigne ? "DROITE" : "BAS";
        String dir2 = estLigne ? "GAUCHE" : "HAUT";

        while (choixDir != 1 && choixDir != 2) {
            System.out.print("Direction : " + dir1 + " (1) ou " + dir2 + " (2) ? ");
            if (scanner.hasNextInt()) {
                choixDir = scanner.nextInt();
            } else {
                scanner.next();
            }
            System.out.println();
        }
        boolean direction = (choixDir == 1);

        // --- EXÉCUTION DU COULISSEMENT (Votre logique reste la même) ---
        int[] temp = new int[5];

        if (estLigne) {
            if (direction) { // VERS LA DROITE
                for (int k = 0; k < 5; k++)
                    temp[k] = plateau[indice][6][k];
                for (int j = 6; j > 0; j--) {
                    for (int k = 0; k < 5; k++)
                        plateau[indice][j][k] = plateau[indice][j-1][k];
                }
                for (int k = 0; k < 5; k++)
                    plateau[indice][0][k] = temp[k];
            } else { // VERS LA GAUCHE
                for (int k = 0; k < 5; k++)
                    temp[k] = plateau[indice][0][k];
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 5; k++)
                        plateau[indice][j][k] = plateau[indice][j+1][k];
                }
                for (int k = 0; k < 5; k++)
                    plateau[indice][6][k] = temp[k];
            }
        } else {
            if (direction) { // VERS LE BAS
                for (int k = 0; k < 5; k++)
                    temp[k] = plateau[6][indice][k];
                for (int i = 6; i > 0; i--) {
                    for (int k = 0; k < 5; k++)
                        plateau[i][indice][k] = plateau[i-1][indice][k];
                }
                for (int k = 0; k < 5; k++)
                    plateau[0][indice][k] = temp[k];
            } else { // VERS LE HAUT
                for (int k = 0; k < 5; k++)
                    temp[k] = plateau[0][indice][k];
                for (int i = 0; i < 6; i++) {
                    for (int k = 0; k < 5; k++)
                        plateau[i][indice][k] = plateau[i+1][indice][k];
                }
                for (int k = 0; k < 5; k++)
                    plateau[6][indice][k] = temp[k];
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
                else if (milieu <= -10) {
                    int idItem = Math.abs(milieu) - 10;
                    System.out.print(Items.SYMBOLES[idItem] + RESET);
                }
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
