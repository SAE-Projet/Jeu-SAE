import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;

class PlateauTests {

    @Test
    void testCreationPlateauDimensions() {
        ArrayList<Integer> idJoueurs = new ArrayList<>(Arrays.asList(2, 3));
        ArrayList<ArrayList<Integer>> itemsJoueurs = new ArrayList<>();
        itemsJoueurs.add(new ArrayList<>());
        itemsJoueurs.add(new ArrayList<>());

        int[][][] plateau = Plateau.creationPlateau(idJoueurs, itemsJoueurs);

        // Vérification des dimensions 7x7x5
        assertEquals(7, plateau.length);
        assertEquals(7, plateau[0].length);
        assertEquals(5, plateau[0][0].length);
    }

    @Test
    void testPlacementJoueurs() {
        ArrayList<Integer> idJoueurs = new ArrayList<>(Arrays.asList(2, 3, 4, 5));
        ArrayList<ArrayList<Integer>> itemsJoueurs = new ArrayList<>();
        for(int i=0; i<4; i++) itemsJoueurs.add(new ArrayList<>());

        int[][][] plateau = Plateau.creationPlateau(idJoueurs, itemsJoueurs);

        // J1 doit être en [0][0][4]
        assertEquals(2, plateau[0][0][4]);
        // J2 doit être en [0][6][4]
        assertEquals(3, plateau[0][6][4]);
        // J3 doit être en [6][0][4]
        assertEquals(4, plateau[6][0][4]);
        // J4 doit être en [6][6][4]
        assertEquals(5, plateau[6][6][4]);
    }

    @Test
    void testBordsSontDesMurs() {
        ArrayList<Integer> idJoueurs = new ArrayList<>();
        ArrayList<ArrayList<Integer>> items = new ArrayList<>();
        int[][][] plateau = Plateau.creationPlateau(idJoueurs, items);

        // Vérifier que les bords extérieurs sont bien fermés (valeur 1)
        for (int i = 0; i < 7; i++) {
            assertEquals(1, plateau[0][i][1], "Bord haut doit être un mur");
            assertEquals(1, plateau[6][i][3], "Bord bas doit être un mur");
            assertEquals(1, plateau[i][0][0], "Bord gauche doit être un mur");
            assertEquals(1, plateau[i][6][2], "Bord droit doit être un mur");
        }
    }
}
