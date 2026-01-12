import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;

public class MoteurJeu {
    public static boolean dejaPris(ArrayList<String> pseudos, String pseudo){
        // On parcourt tous les pseudos déjà pris
        for (String s : pseudos) {
            // On compare avec le nouveau pseudo
            if (s.equalsIgnoreCase(pseudo)) {
                return true; // Le pseudo est déjà pris
            }
        }
        // Le pseudo n'est pas encore pris
        return false;
    }

    public static void lancerJeu() {
        final String[] couleurs = {
                "\u001B[34m",  // bleu (joueur 1)
                "\u001B[31m", // rouge (joueur 2)
                "\u001B[32m", // vert (joueur 3)
                "\u001B[33m" // jaune (joueur 4)
        };

        final String couleurGagnant = "\u001B[38;5;214m";
        final String RESET = "\u001B[0m"; // réinitialise les couleurs
        Scanner scanner = new Scanner(System.in);
        int nbJoueurs;
        // Création des listes des pseudos, des ID des joueurs, et de leur pseudo
        ArrayList<String> pseudos = new ArrayList<>();
        ArrayList<Integer> idJoueurs = new ArrayList<>();
        ArrayList<Integer> scores = new ArrayList<>();
        String pseudo;
        System.out.println("===== Règles du jeu ===== \nCe jeu se joue entre 2 et 4 joueurs.\n" +
                "Le but du jeu est de ramasser ses 3 objets avant que les autres ne ramassent les leurs.\n" +
                "Vous pourrez vous déplacer d'un certain nombre de pas en fonction de vos lancers de dé.\n" +
                "Un petit conseil : Faites attention aux séismes... utilisez-les à votre avantage ! \uD83D\uDE0A");
        String valideRegles;
        // Vérificiation de lecture des règles
        System.out.print("Avez-vous bien lu et compris les règles ?\n" +
                "Êtes-vous prêt à jouer et affronter vos adversaires dans une bataille au coeur du labyrinthe !? o/n : ");
        valideRegles = scanner.nextLine();
        while(!valideRegles.equalsIgnoreCase("o")){
            if(valideRegles.equalsIgnoreCase("n"))
                System.out.print("Alors veuillez relire les règles et entrez 'o' lorsque vous serez prêt : ");
            else
                System.out.print("Veuillez entrer soit 'o' pour oui, ou soit 'n' pour non :  ");
            valideRegles = scanner.nextLine();
        }
        do {
            System.out.print("Super alors commençons !\nCombien êtes-vous de joueurs ? ( Entre 2 et 4 joueurs ) - ");
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
            } while (pseudo.length() < 3 || dejaPris(pseudos, pseudo)); // vérifie que le pseudo n'est pas déjà pris, et qu'il ait plus de 3 caractères
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

        ArrayList<ArrayList<Integer>> itemsJoueurs = new ArrayList<>();
        Random rdm = new Random();

        // 1. On prépare les formes disponibles [0, 1, 2, 3] et on les mélange
        ArrayList<Integer> formesDisponibles = new ArrayList<>();
        for (int f = 0; f < 4; f++) {
            formesDisponibles.add(f);
        }
        Collections.shuffle(formesDisponibles);

        for (int i = 0; i < nbJoueurs; i++) {
            ArrayList<Integer> itemsARecup = new ArrayList<>();

            // On attribue la forme unique à ce joueur
            int maForme = formesDisponibles.get(i);

            // 2. On prépare les 6 couleurs [0, 1, 2, 3, 4, 5] et on les mélange pour ce joueur
            ArrayList<Integer> couleursPossibles = new ArrayList<>();
            for (int c = 0; c < 6; c++) {
                couleursPossibles.add(c);
            }
            Collections.shuffle(couleursPossibles);

            // 3. On prend les 3 premières couleurs du mélange
            for (int j = 0; j < 3; j++) {
                int maCouleur = couleursPossibles.get(j);

                // Calcul de l'ID final : (Couleur * 4) + Forme
                int idFinal = (maCouleur * 4) + maForme;
                itemsARecup.add(idFinal);
            }

            itemsJoueurs.add(itemsARecup);
        }
        int[][][] plateau = Plateau.creationPlateau(idJoueurs, itemsJoueurs);
        Plateau.afficherPlateau(plateau);
        int joueurCourant = 0;

        boolean victoire = false;
        int nbtours = 1;
        while (!victoire) {
            int idJoueur = idJoueurs.get(joueurCourant);
            System.out.println("Tour n°"+ nbtours +" - C'est au tour de : " + couleurs[joueurCourant] + pseudos.get(joueurCourant) + RESET);

            // Affichage des items restants à trouver pour le joueur
            System.out.print("Items à trouver : ");
            for (Integer id : itemsJoueurs.get(joueurCourant)) {
                // On utilise l'ID stocké pour aller chercher le symbole correspondant
                System.out.print(Items.SYMBOLES[id] + " ");
            }
            System.out.println();
            if (nbtours >= 2) {
                System.out.println("Le plateau tremble...");
                Plateau.faireCoulisser(plateau);
                Plateau.afficherPlateau(plateau);
            }
            tour(plateau, scores, couleurs, idJoueur, nbJoueurs, pseudos, itemsJoueurs);
            if (joueurCourant == nbJoueurs-1)
                nbtours ++;

            // Vérification victoire
            if (scores.get(joueurCourant) >= 3) {
                System.out.println("*".repeat(14 + pseudos.get(joueurCourant).length()));
                System.out.println("VICTOIRE DE " + couleurGagnant + pseudos.get(joueurCourant) + RESET + " !");
                System.out.println("*".repeat(14 + pseudos.get(joueurCourant).length()));
                victoire = true;
            }

            joueurCourant = (joueurCourant + 1) % nbJoueurs;
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

    public static void tour(int[][][] tab, ArrayList<Integer> scores, String[] couleurs, int idJoueur, int nbJoueurs, ArrayList<String> pseudos, ArrayList<ArrayList<Integer>> itemsJoueurs) {
        final String[] COULEURS_ITEMS = {
                "\u001B[31m", // 0: Rouge
                "\u001B[32m", // 1: Vert
                "\u001B[35m", // 2: Violet
                "\u001B[36m",  // 3: Cyan
                "\u001B[38;5;208m", // 4: Orange
                "\u001B[38;5;205m" // 5: Rose
        };

        Scanner scanner = new Scanner(System.in);
        int pasRestants = lancerDé();
        final String RESET = "\u001B[0m";
        boolean stop = false;
        int indexJoueur = idJoueur - 2;

        while (pasRestants > 0 && !stop) {
            boolean stopAnnule = false;
            int[] pos = trouverJoueur(tab, idJoueur);
            int x = pos[0]; int y = pos[1]; int z = pos[2];

            // On vérifie si les 4 murs de la tuile actuelle (autour du joueur) sont fermés
            if (tab[x][y][0] == 1 && tab[x][y][1] == 1 && tab[x][y][2] == 1 && tab[x][y][3] == 1) {
                System.out.println("⚠️ Vous êtes enfermé ! Une issue de secours s'ouvre magiquement...");
                Random rdmSecours = new Random();
                int murAouvrir = rdmSecours.nextInt(4); // Choisit un mur au hasard entre 0 et 3
                tab[x][y][murAouvrir] = 0;

                // Pour que ce soit cohérent visuellement, on ouvre aussi le mur du voisin
                if (murAouvrir == 0 && y > 0)
                    tab[x][y-1][2] = 0; // Gauche
                else if (murAouvrir == 1 && x > 0)
                    tab[x-1][y][3] = 0; // Haut
                else if (murAouvrir == 2 && y < 6)
                    tab[x][y+1][0] = 0; // Droite
                else if (murAouvrir == 3 && x < 6)
                    tab[x+1][y][1] = 0; // Bas

                Plateau.afficherPlateau(tab);
            }

            int nx = x; int ny = y; int nz = z;
            int nnx = x; int nny = y; int nnz = z;

            boolean movePossible = false;

            System.out.print("Il vous reste " + pasRestants + " pas. Direction ? (g, d, h, b) \n" +
                    "Si vous voulez vous arrêter ici, veuillez entrer 'q' : ");
            String direction = scanner.nextLine();

            if (direction.equalsIgnoreCase("q")) {
                String choix;
                do {
                    System.out.print("Êtes-vous sûr de vouloir abandonner vos " + pasRestants + " pas restants ? o/n : ");
                    choix = scanner.nextLine();
                } while (!choix.equalsIgnoreCase("o") && !choix.equalsIgnoreCase("n"));

                if (choix.equalsIgnoreCase("o")) {
                    System.out.println(couleurs[idJoueur-2] + pseudos.get(idJoueur-2) + RESET + " a décidé de s'arrêter !");
                    stop = true;
                } else {
                    System.out.println(couleurs[idJoueur-2] + pseudos.get(idJoueur-2) + RESET + " a changé d'avis...");
                    stopAnnule = true;
                }
            }

            else if (direction.equalsIgnoreCase("b")) {
                if (z == 1) {
                    nz = 4;
                    nnz = 3;
                    movePossible = true;
                } else if (z == 4 && tab[x][y][3] != 1) {
                    nz = 3;
                    nnx = (x < 6) ? x + 1 : x;
                    nnz = (x < 6) ? 1 : 3;
                    movePossible = true;
                } else if (z == 3 && x < 6 && tab[x + 1][y][1] != 1) {
                    nx = x + 1;
                    nz = 1;
                    nnx = x + 1;
                    nnz = 4;
                    movePossible = true;
                }
            } else if (direction.equalsIgnoreCase("h")) {
                if (z == 3) {
                    nz = 4;
                    nnz = 1;
                    movePossible = true;
                } else if (z == 4 && tab[x][y][1] != 1) {
                    nz = 1;
                    nnx = (x > 0) ? x - 1 : x;
                    nnz = (x > 0) ? 3 : 1;
                    movePossible = true;
                } else if (z == 1 && x > 0 && tab[x - 1][y][3] != 1) {
                    nx = x - 1;
                    nz = 3;
                    nnx = x - 1;
                    nnz = 4;
                    movePossible = true;
                }
            } else if (direction.equalsIgnoreCase("d")) {
                if (z == 0) {
                    nz = 4;
                    nnz = 2;
                    movePossible = true;
                } else if (z == 4 && tab[x][y][2] != 1) {
                    nz = 2;
                    nny = (y < 6) ? y + 1 : y;
                    nnz = (y < 6) ? 0 : 2;
                    movePossible = true;
                } else if (z == 2 && y < 6 && tab[x][y + 1][0] != 1) {
                    ny = y + 1;
                    nz = 0;
                    nny = y + 1;
                    nnz = 4;
                    movePossible = true;
                }
            } else if (direction.equalsIgnoreCase("g")) {
                if (z == 2) {
                    nz = 4;
                    nnz = 0;
                    movePossible = true;
                } else if (z == 4 && tab[x][y][0] != 1) {
                    nz = 0;
                    nny = (y > 0) ? y - 1 : y;
                    nnz = (y > 0) ? 2 : 0;
                    movePossible = true;
                } else if (z == 0 && y > 0 && tab[x][y - 1][2] != 1) {
                    ny = y - 1;
                    nz = 2;
                    nny = y - 1;
                    nnz = 4;
                    movePossible = true;
                }
            }

            if (movePossible) {
                int cible = tab[nx][ny][nz];
                boolean obstacle = false;

                if (cible >= 2 && cible != idJoueur) {
                    obstacle = true;
                } else if (cible <= -10) {
                    int idItem = Math.abs(cible) - 10;
                    if (!itemsJoueurs.get(indexJoueur).contains(idItem)) {
                        obstacle = true;
                    }
                }

                if (obstacle) {
                    // Si on peut effectivement sauter (destination nn differente de l'obstacle et vide)
                    if ((nnx != nx || nny != ny || nnz != nz) && tab[nnx][nny][nnz] == 0) {
                        boolean murBloquant = false;

                        // Vérification précise des murs physiques entre le joueur et sa destination
                        if (direction.equalsIgnoreCase("b")) {
                            if (tab[x][y][3] == 1 || (x < 6 && tab[x + 1][y][1] == 1))
                                murBloquant = true;
                        } else if (direction.equalsIgnoreCase("h")) {
                            if (tab[x][y][1] == 1 || (x > 0 && tab[x - 1][y][3] == 1))
                                murBloquant = true;
                        } else if (direction.equalsIgnoreCase("d")) {
                            if (tab[x][y][2] == 1 || (y < 6 && tab[x][y + 1][0] == 1))
                                murBloquant = true;
                        } else if (direction.equalsIgnoreCase("g")) {
                            if (tab[x][y][0] == 1 || (y > 0 && tab[x][y - 1][2] == 1))
                                murBloquant = true;
                        }

                        if (!murBloquant) {
                            tab[x][y][z] = 0;
                            tab[nnx][nny][nnz] = idJoueur;
                            pasRestants--;
                            Plateau.afficherPlateau(tab);
                            System.out.println("Obstacle détecté ! Vous enjambez.");
                        } else {
                            System.out.println(couleurs[1] + "Saut impossible" + RESET + " : Un mur ou une entité occupe la case suivante ! " + couleurs[2] + "Aucun pas n'a été consommé." + RESET);
                        }
                    } else {
                        // Cas où la case derrière l'obstacle est un mur de bordure ou une autre entité
                        if (nnx == nx && nny == ny && nnz == nz) {
                            System.out.println("Vous ne pouvez pas sauter au-delà du bord du plateau !");
                        } else {
                            System.out.println(couleurs[1] + "Saut impossible" + RESET + " : Un mur ou une entité occupe la case suivante ! " + couleurs[2] + "Aucun pas n'a été consommé." + RESET);
                        }
                    }
                } else {
                    tab[x][y][z] = 0;
                    tab[nx][ny][nz] = idJoueur;
                    pasRestants--;
                    System.out.println();
                    Plateau.afficherPlateau(tab);
                    if (cible <= -10) {
                        int idItem = Math.abs(cible) - 10;

                        // On retrouve l'index de la couleur (0 à 5)
                        int indexCouleurItem = idItem / 4;
                        String couleurObjet = COULEURS_ITEMS[indexCouleurItem];

                        System.out.println("Bravo ! " + couleurs[idJoueur - 2] + pseudos.get(idJoueur - 2) + RESET +
                                " a ramassé : " + couleurObjet + Items.NOMS[idItem] + RESET);

                        scores.set(indexJoueur, scores.get(indexJoueur) + 1);
                        itemsJoueurs.get(indexJoueur).remove(Integer.valueOf(idItem));
                    }
                }

                if (scores.get(indexJoueur) >= 3) {
                    return;
                }
            } else {
                if (!stop && !stopAnnule) {
                    System.out.println("Vous être réntré dans un mur ! Aucun pas n'a été perdu, veuillez rejouer.");
                }
            }
        }
    }

    public static int lancerDé() {
        String pret;
        Scanner scanner = new Scanner(System.in);
        Random rdm = new Random();
        do {
            System.out.print("Veuillez entrer 'l' pour lancer le dé ! ");
            pret = scanner.nextLine();
        } while(!pret.equalsIgnoreCase("l"));
        int resultat = rdm.nextInt(1,7);
        System.out.println("Vous avez obtenu un " + resultat);
        return resultat;
    }
}
