import java.lang.reflect.Array;
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

            System.out.println("\nTour du joueur " + pseudos.get(joueurCourant));
            tour(plateau, idJoueur);

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
                if (tab[i][j][4] == idJoueur) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static int compterSorties(int[][][] tab, int x, int y) {
        int sorties = 0;

        if (tab[x][y][0] == 0 && y > 0) sorties++;
        if (tab[x][y][2] == 0 && y < 6) sorties++;
        if (tab[x][y][1] == 0 && x > 0) sorties++;
        if (tab[x][y][3] == 0 && x < 6) sorties++;

        return sorties;
    }

    public static String directionAuto(int[][][] tab, int x, int y) {

        if (tab[x][y][0] == 0 && y > 0) return "g";
        if (tab[x][y][2] == 0 && y < 6) return "d";
        if (tab[x][y][1] == 0 && x > 0) return "h";
        if (tab[x][y][3] == 0 && x < 6) return "b";

        return "";
    }

    public static void tour(int[][][] tab, int idJoueur) {
        Scanner scanner = new Scanner(System.in);
        int nbpas = lancerDé();
        boolean aDejaChoisi = false; // message intersection à partir du 2e choix

        int[] pos = trouverJoueur(tab, idJoueur);
        int x = pos[0];
        int y = pos[1];

        int pasRestants = nbpas;

        while (pasRestants > 0) {

            int sorties = compterSorties(tab, x, y);
            String direction = "";

            // Intersection (≥ 2 sorties) → demande au joueur
            if (sorties >= 2) {
                if (aDejaChoisi) {
                    System.out.println("Intersection — il vous reste " + pasRestants + " pas");
                }
                System.out.print("Direction ? (g, d, h, b) : ");
                direction = scanner.nextLine();
                aDejaChoisi = true;
            }
            // Chemin unique → avance automatique
            else if (sorties == 1) {
                direction = directionAuto(tab, x, y);
                System.out.println("Chemin unique — déplacement automatique (" + pasRestants + " pas restants)");
            }
            // Cul-de-sac → on arrête
            else {
                System.out.println("Cul-de-sac !");
                break;
            }

            boolean deplacementOK = false;

            // GAUCHE
            if (direction.equalsIgnoreCase("g") && tab[x][y][0] == 0 && y > 0 && tab[x][y-1][2] == 0) {
                tab[x][y][4] = 0;
                tab[x][y-1][4] = idJoueur;
                y = y - 1;
                deplacementOK = true;
            }
            // DROITE
            else if (direction.equalsIgnoreCase("d") && tab[x][y][2] == 0 && y < 6 && tab[x][y+1][0] == 0) {
                tab[x][y][4] = 0;
                tab[x][y+1][4] = idJoueur;
                y = y + 1;
                deplacementOK = true;
            }
            // HAUT
            else if (direction.equalsIgnoreCase("h") && tab[x][y][1] == 0 && x > 0 && tab[x-1][y][3] == 0) {
                tab[x][y][4] = 0;
                tab[x-1][y][4] = idJoueur;
                x = x - 1;
                deplacementOK = true;
            }
            // BAS
            else if (direction.equalsIgnoreCase("b") && tab[x][y][3] == 0 && x < 6 && tab[x+1][y][1] == 0) {
                tab[x][y][4] = 0;
                tab[x+1][y][4] = idJoueur;
                x = x + 1;
                deplacementOK = true;
            }

            if (!deplacementOK) {
                System.out.println("Déplacement impossible !");
                continue; // pas consommé
            }

            pasRestants--; // consommer un pas
            Plateau.afficherPlateau(tab);
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
