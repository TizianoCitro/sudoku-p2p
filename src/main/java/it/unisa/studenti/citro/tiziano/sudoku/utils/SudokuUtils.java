package it.unisa.studenti.citro.tiziano.sudoku.utils;

/**
 * Provides some utility functionalities.
 */
public class SudokuUtils {

    /**
     * Provides the identifier for players.
     * @param _game_name is the game name.
     * @return the identifier for players.
     */
    public static String withPlayers(String _game_name) {
        return new StringBuilder(_game_name).append(PLAYERS).toString();
    }

    /**
     * Provides the identifier for players nicknames.
     * @param _game_name is the game name.
     * @return the identifier for players nicknames.
     */
    public static String withNicknames(String _game_name) {
        return new StringBuilder(_game_name).append(NICKNAMES).toString();
    }

    /**
     * Provides the identifier for players scores.
     * @param _game_name is the game name.
     * @return the identifier for players scores.
     */
    public static String withScores(String _game_name) {
        return new StringBuilder(_game_name).append(SCORES).toString();
    }

    /**
     * Suffix for players identifier.
     */
    public static final String PLAYERS = "_players";

    /**
     * Suffix for players nicknames identifier.
     */
    public static final String NICKNAMES = "_nicknames";

    /**
     * Suffix for players scores identifier.
     */
    public static final String SCORES = "_scores";

    /**
     * Default port for a peer in the network.
     */
    public static final int DEFAULT_PORT = 4000;

    /**
     * Starting score for players.
     */
    public static final int STARTING_SCORE = 0;

    /**
     * Value for indicating an error while attempting to place a number on a cell in the game.
     */
    public static final int ERROR_WHILE_PLACING_NUMBER = -2;
}
