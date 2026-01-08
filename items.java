public class Items {
    // Codes couleurs ANSI personnalisés
    public static final String VIOLET = "\u001B[35m";
    public static final String NOIR   = "\u001B[30;1m"; // Gris foncé / Noir brillant
    public static final String BLANC  = "\u001B[37m";
    public static final String CYAN   = "\u001B[36m";
    public static final String ROSE   = "\u001B[38;5;205m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String RESET  = "\u001B[0m";

    // Les 4 formes choisies pour leur stabilité d'affichage
    private static final String[] FORMES = {"★", "♦", "●", "♠"};

    // Génération automatique des 24 items (6 couleurs x 4 formes)
    public static final String[] SYMBOLES = {
            VIOLET+FORMES[0], VIOLET+FORMES[1], VIOLET+FORMES[2], VIOLET+FORMES[3],
            NOIR+FORMES[0],   NOIR+FORMES[1],   NOIR+FORMES[2],   NOIR+FORMES[3],
            BLANC+FORMES[0],  BLANC+FORMES[1],  BLANC+FORMES[2],  BLANC+FORMES[3],
            CYAN+FORMES[0],   CYAN+FORMES[1],   CYAN+FORMES[2],   CYAN+FORMES[3],
            ROSE+FORMES[0],   ROSE+FORMES[1],   ROSE+FORMES[2],   ROSE+FORMES[3],
            ORANGE+FORMES[0], ORANGE+FORMES[1], ORANGE+FORMES[2], ORANGE+FORMES[3]
    };

    // Noms correspondants pour l'affichage des objectifs
    public static final String[] NOMS = {
            "Étoile Nebula", "Améthyste", "Orbe pourpre", "Pique galaxy",
            "Étoile du Chaos", "Onyx", "Orbe du néant", "Pique obscur",
            "Étoile Céleste", "Quartz", "Orbe lumineux", "Pique d'ivoire",
            "Étoile Givrée", "Cyanite", "Orbe de glace", "Pique d'azur",
            "Étoile Filante", "Rhodonite", "Orbe rosé", "Pique bonbon",
            "Étoile Solaire", "Topaze", "Orbe enflammé", "Pique de cuivre"
    };
}
