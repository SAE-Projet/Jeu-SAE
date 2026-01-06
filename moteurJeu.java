import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;

public class MoteurJeu {
    public static boolean dejaPris(ArrayList<String> pseudos, String pseudo){
        for (String p : pseudos) {
            if (p.equalsIgnoreCase(pseudo))
                return true;
        }
        return false;
    }

    public static void lancerJeu() {
        final String[] couleurs = {
                "\u001B[34m",  // bleu (joueur 1)
                "\u001B[31m", // rouge (joueur 2)
                "\u001B[32m", // vert (joueur 3)
                "\u001B[33m" // jaune (joueur 4)
        };

        final String RESET = "\u001B[0m";
        Scanner scanner = new Scanner(System.in);
        int nbJoueurs;
        ArrayList<String> pseudos = new ArrayList<>();
        ArrayList<Integer> idJoueurs = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        String pseudo;
        do {
            System.out.print("Combien êtes-vous de joueurs ? ( Entre 2 et 4 joueurs ) - ");
            nbJoueurs = scanner.nextInt();
        } while(nbJoueurs < 2 || nbJoueurs > 4);
        scanner.nextLine();
        for (int i = 1; i <= nbJoueurs; i++) {
            do {
                System.out.print(couleurs[i-1] + "● " + RESET);
                System.out.print("Joueur n°" + i + ", veuillez entrer votre pseudo (min 3 caractères) : ");
                pseudo = scanner.nextLine();
                if (dejaPris(pseudos, pseudo))
                    System.out.println("Ce pseudo est déjà pris.");
                if (pseudo.length() < 3)
                    System.out.println("Pseudo trop court !");
            } while (pseudo.length() < 3 || dejaPris(pseudos, pseudo)); // vérifie que le pseudo n'est pas déjà pris, et qu'il soit de plus de 3 caractères
            pseudos.add(pseudo);
            idJoueurs.add(i+1);
            scores.add(0);
        }
        System.out.println();

        for (int i = 0; i < nbJoueurs; i++) {
            System.out.print(couleurs[i] + "● " + RESET);
            System.out.println("Score " + pseudos.get(i) + " : " + scores.get(i));
        }
        System.out.println();

        // Jfais une liste avec tous les items, check la classe items pr voir tous les objets que y a

        ArrayList<Integer> listeItems = new ArrayList<>();

        for (int i = -2; i >= -25; i--) {
            listeItems.add(i);
        }
        Collections.shuffle(listeItems);

        // Jfais une liste pour que chaque joueur ait 3 items à récup

        ArrayList<ArrayList<Integer>> itemsJoueurs = new ArrayList<>();

        // Pour avoir l'indice de départ avec lql on récup les items pr chaque joueur
        int indItem = 0;

        for (int i = 0; i < nbJoueurs; i++) {
            ArrayList<Integer> itemsARecup = new ArrayList<>();

            // Ça récup les 3 items dans la liste mélangée)
            itemsARecup.add(listeItems.get(indItem++));
            itemsARecup.add(listeItems.get(indItem++));
            itemsARecup.add(listeItems.get(indItem++));

            // on donne les 3 items au premier joueur puis on enchaîne avc les suivants

            itemsJoueurs.add(itemsARecup);
        }

        int[][][] plateau = Plateau.creationPlateau(idJoueurs);
        Plateau.afficherPlateau(plateau);
        int joueurCourant = 0;

        while (true) {
            int idJoueur = idJoueurs.get(joueurCourant);
            System.out.println("Tour du joueur " + pseudos.get(joueurCourant));
            tour(plateau, scores, couleurs, idJoueur, nbJoueurs, pseudos);
            System.out.println("Le plateau tremble...");
            Plateau.faireCoulisser(plateau);
            Plateau.afficherPlateau(plateau);

            // joueur suivant
            joueurCourant++;
            if (joueurCourant == nbJoueurs) {
                joueurCourant = 0;
            }
        }
    }

    public static int[] trouverJoueur(int[][][] tab, int idJoueur) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                for (int k = 0; k < 5; k++) {
                    if (tab[i][j][k] == idJoueur)
                        return new int[]{i, j, k};
                }
            }
        }
        return null;
    }

    public static void tour(int[][][] tab, ArrayList<Integer> scores, String[] couleurs, int idJoueur, int nbJoueurs, ArrayList<String> pseudos) {
        Scanner scanner = new Scanner(System.in);
        int pasRestants = lancerDé();
        final String RESET = "\u001B[0m";
        boolean stop = false;

        while (pasRestants > 0 && !stop) {
            int[] pos = trouverJoueur(tab, idJoueur);
            int x = pos[0], y = pos[1], z = pos[2];
            String choix;

            do {
                System.out.println("Il vous reste " + pasRestants + " pas. Voulez-vous vous arrêter ? o/n");
                choix = scanner.nextLine();
            } while (!choix.equalsIgnoreCase("o") && !choix.equalsIgnoreCase("n"));
            if(choix.equalsIgnoreCase("o")) {
                do {
                    System.out.println("Etes-vous sûr de vouloir abandonner vos " + pasRestants + " pas restants ? o/n");
                    choix = scanner.nextLine();
                } while (!choix.equalsIgnoreCase("o") && !choix.equalsIgnoreCase("n"));
                if (choix.equalsIgnoreCase("o"))
                    stop = true;
            }
            if(!stop) {
                System.out.print("Direction ? (g, d, h, b) : ");
                String direction = scanner.nextLine();

                boolean deplacementOK = false;
                int nx = x, ny = y, nz = z;

                // --- BAS ---
                if (direction.equalsIgnoreCase("b")) {
                    if (z == 1) {
                        nz = 4;
                        deplacementOK = true;
                    } // Bord haut -> Milieu

                    else if (z == 4 && tab[x][y][3] == 0) {
                        nz = 3;
                        deplacementOK = true;
                    } // Milieu -> Bord bas

                    else if (z == 3 && x < 6) { // Déjà sur le bord bas -> Changement de case
                        if (tab[x + 1][y][1] == 0) {
                            nx = x + 1;
                            nz = 1;
                            deplacementOK = true;
                        }
                    }
                }

                // --- HAUT ---
                else if (direction.equalsIgnoreCase("h")) {
                    if (z == 3) {
                        nz = 4;
                        deplacementOK = true;
                    } // Bord bas -> Milieu

                    else if (z == 4 && tab[x][y][1] == 0) {
                        nz = 1;
                        deplacementOK = true;
                    } // Milieu -> Bord haut

                    else if (z == 1 && x > 0) { // Déjà sur le bord haut -> Changement de case
                        if (tab[x - 1][y][3] == 0) {
                            nx = x - 1;
                            nz = 3;
                            deplacementOK = true;
                        }
                    }
                }

                // --- DROITE ---
                else if (direction.equalsIgnoreCase("d")) {
                    if (z == 0) {
                        nz = 4;
                        deplacementOK = true;
                    } // Bord gauche -> Milieu

                    else if (z == 4 && tab[x][y][2] == 0) {
                        nz = 2;
                        deplacementOK = true;
                    } // Milieu -> Bord droit

                    else if (z == 2 && y < 6) { // Déjà sur le bord droit -> Changement de case
                        if (tab[x][y + 1][0] == 0) {
                            ny = y + 1;
                            nz = 0;
                            deplacementOK = true;
                        }
                    }
                }

                // --- GAUCHE ---
                else if (direction.equalsIgnoreCase("g")) {
                    if (z == 2) {
                        nz = 4;
                        deplacementOK = true;
                    } // Bord droit -> Milieu

                    else if (z == 4 && tab[x][y][0] == 0) {
                        nz = 0;
                        deplacementOK = true;
                    } // Milieu -> Bord gauche

                    else if (z == 0 && y > 0) { // Déjà sur le bord gauche -> Changement de case
                        if (tab[x][y - 1][2] == 0) {
                            ny = y - 1;
                            nz = 2;
                            deplacementOK = true;
                        }
                    }
                }

                if (deplacementOK) {
                    tab[x][y][z] = 0;
                    tab[nx][ny][nz] = idJoueur;
                    pasRestants--;

                    System.out.println();
                    for (int i = 0; i < nbJoueurs; i++) {
                        System.out.print(couleurs[i] + "● " + RESET);
                        System.out.println("Score " + pseudos.get(i) + " : " + scores.get(i));
                    }
                    System.out.println();
                    Plateau.afficherPlateau(tab);
                } else {
                    System.out.println("Déplacement impossible !");
                }
            }
        }
    }

    public static int lancerDé() {
        String pret;
        Scanner scanner = new Scanner(System.in);
        Random rdm = new Random();
        do {
            System.out.print("Veuillez entrer 'l' lorsque vous êtes prêt à lancer le dé ! ");
            pret = scanner.nextLine();
        } while(!pret.equals("l"));
        int resultat = rdm.nextInt(1,7);
        System.out.println("Vous avez obtenu un " + resultat);
        return resultat;
    }
}
