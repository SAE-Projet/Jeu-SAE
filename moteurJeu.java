import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;

public class MoteurJeu {
    public static boolean dejaPris(ArrayList<String> pseudos, String pseudo){
        // On parcourt tous les pseudos déjà pris
        for (int i = 0; i < pseudos.size(); i++) {
            // On compare avec le nouveau pseudo
            if (pseudos.get(i).equalsIgnoreCase(pseudo)) {
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

        final String RESET = "\u001B[0m"; // couleur du terminal
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
        while (!victoire) {
            int idJoueur = idJoueurs.get(joueurCourant);
            System.out.println("\nC'EST AU TOUR DE : " + couleurs[joueurCourant] + pseudos.get(joueurCourant) + RESET);

            // Affichage des items restants à trouver pour le joueur
            System.out.print("Items à trouver : ");
            for (Integer id : itemsJoueurs.get(joueurCourant)) {
                // On utilise l'ID stocké pour aller chercher le symbole correspondant
                System.out.print(Items.SYMBOLES[id] + " ");
            }
            System.out.println();
            tour(plateau, scores, couleurs, idJoueur, nbJoueurs, pseudos, itemsJoueurs);

            // Vérification victoire
            if (scores.get(joueurCourant) >= 3) {
                System.out.println("******************************************");
                System.out.println("VICTOIRE DE " + pseudos.get(joueurCourant) + " !");
                System.out.println("******************************************");
                victoire = true;
            }

            System.out.println("Le plateau tremble...");
            Plateau.faireCoulisser(plateau);
            Plateau.afficherPlateau(plateau);

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
        Scanner scanner = new Scanner(System.in);
        int pasRestants = lancerDé();
        final String RESET = "\u001B[0m";
        boolean stop = false;
        int indexJoueur = idJoueur - 2;

        while (pasRestants > 0 && !stop) {
            boolean stopAnnule = false;
            int[] pos = trouverJoueur(tab, idJoueur);
            int x = pos[0], y = pos[1], z = pos[2];
            int nx = x, ny = y, nz = z;

            // Variables pour le saut (enjambement)
            int nnx = x, nny = y, nnz = z;
            boolean deplacementOK = false;
            boolean estUnSaut = false;

            System.out.print("Il vous reste " + pasRestants + " pas. Direction ? (g, d, h, b) \n" +
                    "Si vous voulez vous arrêter ici, veuillez entrer 'q' : ");
            String direction = scanner.nextLine();

            if (direction.equalsIgnoreCase("q")){
                String choix;
                do {
                    System.out.print("Etes-vous sûr de vouloir abandonner vos " + pasRestants + " pas restants ? o/n : ");
                    choix = scanner.nextLine();
                } while (!choix.equalsIgnoreCase("o") && !choix.equalsIgnoreCase("n"));

                if (choix.equalsIgnoreCase("o")) {
                    System.out.println(couleurs[idJoueur-2] + pseudos.get(idJoueur-2) + RESET + " a décidé de s'arrêter !");
                    stop = true;
                } else {
                    System.out.println(couleurs[idJoueur-2] + pseudos.get(idJoueur-2) + RESET + " a changé d'avis... veuillez rejouer s'il vous plaît.");
                    stopAnnule = true;
                }
            }
            else if (direction.equalsIgnoreCase("b")) {
                if (z == 1) {
                    nx = x; ny = y; nz = 4; // Case normale
                    nnx = x; nny = y; nnz = 3; // Case après saut
                }
                else if (z == 4 && tab[x][y][3] == 0) {
                    nx = x; ny = y; nz = 3;
                    if (x < 6) { nnx = x + 1; nny = y; nnz = 1; }
                }
                else if (z == 3 && x < 6 && tab[x+1][y][1] == 0) {
                    nx = x + 1; ny = y; nz = 1;
                    nnx = x + 1; nny = y; nnz = 4;
                }
                deplacementOK = true;
            }
            else if (direction.equalsIgnoreCase("h")) {
                if (z == 3) {
                    nx = x; ny = y; nz = 4;
                    nnx = x; nny = y; nnz = 1;
                }
                else if (z == 4 && tab[x][y][1] == 0) {
                    nx = x; ny = y; nz = 1;
                    if (x > 0) { nnx = x - 1; nny = y; nnz = 3; }
                }
                else if (z == 1 && x > 0 && tab[x-1][y][3] == 0) {
                    nx = x - 1; ny = y; nz = 3;
                    nnx = x - 1; nny = y; nnz = 4;
                }
                deplacementOK = true;
            }
            else if (direction.equalsIgnoreCase("d")) {
                if (z == 0) {
                    nx = x; ny = y; nz = 4;
                    nnx = x; nny = y; nnz = 2;
                }
                else if (z == 4 && tab[x][y][2] == 0) {
                    nx = x; ny = y; nz = 2;
                    if (y < 6) { nnx = x; nny = y + 1; nnz = 0; }
                }
                else if (z == 2 && y < 6 && tab[x][y+1][0] == 0) {
                    nx = x; ny = y + 1; nz = 0;
                    nnx = x; nny = y + 1; nnz = 4;
                }
                deplacementOK = true;
            }
            else if (direction.equalsIgnoreCase("g")) {
                if (z == 2) {
                    nx = x; ny = y; nz = 4;
                    nnx = x; nny = y; nnz = 0;
                }
                else if (z == 4 && tab[x][y][0] == 0) {
                    nx = x; ny = y; nz = 0;
                    if (y > 0) { nnx = x; nny = y - 1; nnz = 2; }
                }
                else if (z == 0 && y > 0 && tab[x][y-1][2] == 0) {
                    nx = x; ny = y - 1; nz = 2;
                    nnx = x; nny = y - 1; nnz = 4;
                }
                deplacementOK = true;
            }

            if (deplacementOK) {
                int cible = tab[nx][ny][nz];
                boolean obstacle = false;

                // Détection si la cible est un obstacle (Joueur adverse ou Item adverse)
                if (cible >= 2 && cible != idJoueur) {
                    obstacle = true;
                } else if (cible <= -10) {
                    int idItem = Math.abs(cible) - 10;
                    if (!itemsJoueurs.get(indexJoueur).contains(idItem)) {
                        obstacle = true;
                    }
                }

                if (obstacle) {
                    // Tentative de saut : On vérifie si la case d'après est libre (pas de mur et pas d'obstacle)
                    // On vérifie aussi que le saut ne sort pas du plateau et consomme 2 pas
                    if (nnx != x || nny != y || nnz != z) {
                        int apresSaut = tab[nnx][nny][nnz];
                        if (apresSaut == 0) {
                            System.out.println("Obstacle détecté ! Vous enjambez et avancez de 2 cases.");
                            tab[x][y][z] = 0;
                            tab[nnx][nny][nnz] = idJoueur;
                            pasRestants -= 2;
                            estUnSaut = true;
                        } else {
                            System.out.println("Déplacement impossible : La case après l'obstacle est occupée !");
                        }
                    } else {
                        System.out.println("Déplacement impossible : Un obstacle bloque le chemin !");
                    }
                } else {
                    // Déplacement normal (Vide ou Item à soi)
                    if (cible <= -10) {
                        int idItem = Math.abs(cible) - 10;
                        System.out.println("Bravo ! Vous avez ramassé : " + Items.NOMS[idItem]);
                        scores.set(indexJoueur, scores.get(indexJoueur) + 1);
                        itemsJoueurs.get(indexJoueur).remove(Integer.valueOf(idItem));
                    }

                    tab[x][y][z] = 0;
                    tab[nx][ny][nz] = idJoueur;
                    pasRestants--;
                }

                // Rafraîchissement du plateau après mouvement réussi
                if (!obstacle || estUnSaut) {
                    System.out.println();
                    for (int i = 0; i < nbJoueurs; i++) {
                        System.out.print(couleurs[i] + "● " + RESET);
                        System.out.println("Score " + pseudos.get(i) + " : " + scores.get(i));
                    }
                    Plateau.afficherPlateau(tab);
                }

                if (scores.get(indexJoueur) >= 3) {
                    return;
                }
            } else if (!stop && !stopAnnule) {
                System.out.println("Déplacement impossible : Un mur vous bloque !");
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
        } while(!pret.equals("l"));
        int resultat = rdm.nextInt(1,7);
        System.out.println("Vous avez obtenu un " + resultat);
        return resultat;
    }
}
