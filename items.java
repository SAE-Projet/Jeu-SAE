public class Items {
    public static final String VIOLET     = "\u001B[35m";
    public static final String VERT       = "\u001B[32m";
    public static final String ROUGE      = "\u001B[31m";
    public static final String CYAN       = "\u001B[36m";
    public static final String ROSE       = "\u001B[38;5;205m";
    public static final String ORANGE     = "\u001B[38;5;208m";
    public static final String RESET      = "\u001B[0m";

    private static final String[] FORMES = {"★", "♦", "▲", "♠"};

    // 6 couleurs x 4 formes = 24 items
    public static final String[] SYMBOLES = {
            VIOLET+FORMES[0]+RESET,     VIOLET+FORMES[1]+RESET,     VIOLET+FORMES[2]+RESET,     VIOLET+FORMES[3]+RESET,
            VERT+FORMES[0]+RESET,       VERT+FORMES[1]+RESET,       VERT+FORMES[2]+RESET,       VERT+FORMES[3]+RESET,
            ROUGE+FORMES[0]+RESET,      ROUGE+FORMES[1]+RESET,      ROUGE+FORMES[2]+RESET,      ROUGE+FORMES[3]+RESET,
            CYAN+FORMES[0]+RESET,       CYAN+FORMES[1]+RESET,       CYAN+FORMES[2]+RESET,       CYAN+FORMES[3]+RESET,
            ROSE+FORMES[0]+RESET,       ROSE+FORMES[1]+RESET,       ROSE+FORMES[2]+RESET,       ROSE+FORMES[3]+RESET,
            ORANGE+FORMES[0]+RESET,     ORANGE+FORMES[1]+RESET,     ORANGE+FORMES[2]+RESET,     ORANGE+FORMES[3]+RESET
    };

    public static final String[] NOMS = {
            "Étoile Nebula", "Améthyste", "Pyramide pourpre", "Pique galaxy",
            "Étoile Emeraude", "Jade", "Pyramide sylvestre", "Pique de ronce",
            "Étoile d'Azur", "Saphir", "Pyramide marin", "Pique abyssal",
            "Étoile Givrée", "Cyanite", "Pyramide de glace", "Pique de givre",
            "Étoile Filante", "Rhodonite", "Pyramide rosé", "Pique bonbon",
            "Étoile Solaire", "Topaze", "Pyramide enflammé", "Pique de cuivre"
    };
}
