package it.unisa.studenti.citro.tiziano.sudoku.verifier;

import it.unisa.studenti.citro.tiziano.sudoku.model.Sudoku;

import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.*;

/**
 * Provides functionalities to verify the state of the game.
 * So that it becomes possible to verify whether an action ca be performed or not.
 */
public class SudokuVerifier {

    /**
     * Verifies a position in the game, so that a number can be placed.
     * @param sudoku is the Sudoku to verify
     * @param row is the row where the number has to be placed.
     * @param column is the column where the number has to be placed.
     * @param number is the number to verify.
     * @return true if the number can be placed, false otherwise.
     */
    public static boolean verifyPosition(Sudoku sudoku, int row, int column, int number) {
        return verifyRow(sudoku, row, number)
                && verifyColumn(sudoku, column, number)
                && verifyGrid(sudoku, row, column, number);
    }

    /**
     * Verifies if the number can be placed in the row.
     * @param sudoku is the game to verify.
     * @param number is the number to verify.
     * @param row is the row where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    private static boolean verifyRow(Sudoku sudoku, int row, int number) {
        for (int i = FIRST; i <= TOTAL_BLOCK_NUMBER; i++) {
            if (sudoku.getGrid().getCellNumber(row, i) == number) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if the number can be placed in the row.
     * @param sudoku is the game to verify.
     * @param number is the number to verify.
     * @param column is the column where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    private static boolean verifyColumn(Sudoku sudoku, int column, int number) {
        for (int i = FIRST; i <= TOTAL_BLOCK_NUMBER; i++) {
            if (sudoku.getGrid().getCellNumber(i, column) == number) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies the game grid for placing a number
     * @param sudoku is the Sudoku to verify
     * @param row is the row where the number has to be placed.
     * @param column is the column where the number has to be placed.
     * @param number is the number to verify.
     * @return true if the number can be placed, false otherwise.
     */
    private static boolean verifyGrid(Sudoku sudoku, int row, int column, int number) {
        int rw = getStartingPosition(row);
        int col = getStartingPosition(column);
        for (int i = rw; i < rw + BLOCK_NUMBER; i++) {
            for (int j = col; j < col + BLOCK_NUMBER; j++) {
                if (sudoku.getGrid().getCellNumber(i, j) == number) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * provides the starting position for validating the game grid.
     * @param position is the position to use to calculate the starting position.
     * @return the starting position for validating the game grid.
     */
    private static int getStartingPosition(int position) {
        int bottom = FIRST;
        int top = BLOCK_NUMBER;
        while (true) {
            if (position >= bottom && position <= top) {
                return bottom;
            }
            bottom += BLOCK_NUMBER;
            top += BLOCK_NUMBER;
        }
    }
}
