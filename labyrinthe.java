import java.util.Random;

public class Plateau {
    public static int[][][] creationPlateau() {
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
                for (int l = 0; l < 4; l++) {
                    if(i == 0 && l == 1 || i == 6 && l == 3 || j == 0 && l == 0 || j == 6 && l == 2)
                        plateau[i][j][l] = 1;
                    else if (plateau[i][j][l] == -1)
                        plateau[i][j][l] = rdm.nextInt(2);
                }
            }
        }
        return plateau;
    }

    public static void afficherPlateau(int[][][] plateau) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print("#");
                if (plateau[i][j][1] == 0)
                    System.out.print(" ");
                else
                    System.out.print("#");
                System.out.print("#");
            }
            System.out.println();
            for (int j = 0; j < 7; j++) {
                if (plateau[i][j][0] == 0)
                    System.out.print(" ");
                else
                    System.out.print("#");
                if (plateau[i][j][4] == 0)
                    System.out.print(" ");
                else
                    System.out.print("#");
                if (plateau[i][j][2] == 0)
                    System.out.print(" ");
                else
                    System.out.print("#");
            }
            System.out.println();
            for (int j = 0; j < 7; j++) {
                System.out.print("#");
                if (plateau[i][j][3] == 0)
                    System.out.print(" ");
                else
                    System.out.print("#");
                System.out.print("#");
            }
            System.out.println();
        }
    }
}
