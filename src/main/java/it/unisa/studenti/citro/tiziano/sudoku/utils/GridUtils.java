package it.unisa.studenti.citro.tiziano.sudoku.utils;

import it.unisa.studenti.citro.tiziano.sudoku.model.Grid;

import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.TOTAL_BLOCK_NUMBER;

/**
 * Provides utility functionalities.
 */
public class GridUtils {

    /**
     * Provides a random number for placing it in a cell.
     * @param gridSize is the grid size.
     * @return a random number for placing it in a cell.
     */
    public static int getRandomCellNumber(int gridSize) {
        int n = TOTAL_BLOCK_NUMBER + 1;
        return (int) (Math.random() * n) % gridSize + 1;
    }

    /**
     * Returns the exact values from a given value.
     * @param n is the original value.
     * @return the exact value.
     */
    public static int toExact(int n) {
        return n - 1;
    }

    /**
     * Calculates the value to use from the given value.
     * @param grid is the grid for which the value has to be calculated.
     * @param n is the value to use for calculating the needed value.
     * @return the calculated value.
     */
    public static int calc(Grid grid, int n) {
        int blockSize = grid.getBlockSize();
        int calculated = n;
        if (calculated % blockSize == 0) {
            calculated -= blockSize - 1;
        } else {
            calculated = (calculated / blockSize) * blockSize + 1;
        }
        return calculated;
    }
}
