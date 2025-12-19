import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class MoteurJeu {
    public static boolean dejaPris(ArrayList<String> pseudos, String pseudo){
        for (String p : pseudos) {
            if (p.equalsIgnoreCase(pseudo))
                return true;
        }
        return false;
    }

    public static int[][][] lancerJeu() {
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
        int[][][] plateau = Plateau.creationPlateau(idJoueurs);
        Plateau.afficherPlateau(plateau);
        return plateau;
    }

    public static void tour(int[][][] tab){
        Scanner scanner = new Scanner(System.in);
        String direction;
        int nbpas = lancerDé();
        for (int i = 0; i < nbpas; i++) {
            System.out.print("Dans quelle direction voulez-vous aller ? ");
            direction = scanner.nextLine();
            if(tab[i])
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
        System.out.print("Vous avez obtenu un " + resultat);
        return resultat;
    }
}
