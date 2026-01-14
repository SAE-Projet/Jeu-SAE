import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class MoteurJeuTests {

    @Test
    void testDejaPris() {
        ArrayList<String> pseudos = new ArrayList<>();
        pseudos.add("Aymane");
        pseudos.add("Safwane");

        // Test de pseudos déjà pris (insensible à la casse)
        assertTrue(MoteurJeu.dejaPris(pseudos, "Aymane"));
        assertTrue(MoteurJeu.dejaPris(pseudos, "aymane"));
        assertTrue(MoteurJeu.dejaPris(pseudos, "SAFWANE"));

        // Test de pseudo pas encore pris
        assertFalse(MoteurJeu.dejaPris(pseudos, "Nathan"));
    }

    @Test
    void testTrouverJoueur() {
        int[][][] plateau = new int[7][7][5];
        // On place le joueur 1 (ID = 2) à la position [3][4][4] (ligne 4, colonne 5, au milieu)
        plateau[3][4][4] = 2;

        int[] position = MoteurJeu.trouverJoueur(plateau, 2);

        assertNotNull(position);
        assertEquals(3, position[0]); // Ligne
        assertEquals(4, position[1]); // Colonne
        assertEquals(4, position[2]); // Position dans la tuile
    }

    @Test
    void testTrouverJoueurInexistant() {
        int[][][] plateau = new int[7][7][5];
        int[] position = MoteurJeu.trouverJoueur(plateau, 99);
        assertNull(position);
    }
}
