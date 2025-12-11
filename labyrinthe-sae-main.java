import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
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
        do {
            System.out.print("Combien êtes-vous de joueurs ? ( Entre 2 et 4 joueurs ) - ");
            nbJoueurs = scanner.nextInt();
        } while(nbJoueurs < 2 || nbJoueurs > 4);
        scanner.nextLine();
        for (int i = 1; i <= nbJoueurs; i++) {
            System.out.print("Joueur n°" + i + " veuillez entrer votre pseudo : ");
            pseudos.add(scanner.nextLine());
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
    }
}
