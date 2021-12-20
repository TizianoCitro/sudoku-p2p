package it.unisa.studenti.citro.tiziano.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.FIRST;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.TOTAL_BLOCK_NUMBER;

public class GameUtils {

    /**
     * Checks if the values are valid for a game.
     * @param gameName is the game name.
     * @param row is the row to check.
     * @param column is the column.
     * @param number is the number to check.
     * @param gamesNames are the names of the games for a user.
     * @return true if the values are valid, false otherwise.
     */
    public static boolean isGameValid(String gameName, int row, int column, int number, List<String> gamesNames) {
        if (row < FIRST || row > TOTAL_BLOCK_NUMBER) {
            return false;
        }
        if (column < FIRST || column > TOTAL_BLOCK_NUMBER) {
            return false;
        }
        if (number < FIRST || number > TOTAL_BLOCK_NUMBER) {
            return false;
        }
        // Check whether the game has been joined,
        // because it is not possible place a number in a game
        // that has not been joined yet.
        return gamesNames.contains(gameName);
    }

    /**
     * Checks if the values are valid for a game.
     * @param gameName is the game name.
     * @param nickname is the nickname to check.
     * @param gamesNames are the names of the games for a user.
     * @return true if the values are valid, false otherwise.
     */
    public static boolean isGameValid(String gameName, String nickname, List<String> gamesNames) {
        return StringUtils.isNotBlank(gameName)
                && StringUtils.isNotBlank(nickname)
                && !gamesNames.contains(gameName);
    }

    /**
     * Checks if the game name is valid.
     * @param gameName is the game name
     * @return true if the game name is valid, false otherwise.
     */
    public static boolean isGameNameValid(String gameName) {
        return StringUtils.isNotBlank(gameName);
    }
}
