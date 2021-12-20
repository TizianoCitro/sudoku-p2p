package it.unisa.studenti.citro.tiziano.sudoku.verifier;

import lombok.AllArgsConstructor;
import it.unisa.studenti.citro.tiziano.sudoku.model.Cell;
import it.unisa.studenti.citro.tiziano.sudoku.model.Grid;

import static it.unisa.studenti.citro.tiziano.sudoku.utils.GridUtils.calc;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.GridUtils.toExact;

/**
 * Provides functionalities to verify the state of the grid.
 * So that it becomes possible to verify whether an action ca be performed or not.
 */
@AllArgsConstructor
public class GridVerifier {

    /**
     * Verify that a number can be placed.
     * @param grid is the grid to verify.
     * @param number is the number to verify.
     * @param row is the row where the number has to be placed.
     * @param column is the column where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    public static boolean verifyGridNumber(Grid grid, int number, int row, int column) {
        return (verifyNumber(grid, number, row, column)
                && verifyRowNumber(grid, number, row)
                && verifyColumnNumber(grid, number, column));
    }

    /**
     * Verifies if the number can be placed int the related subgrid.
     * @param grid is the grid to verify.
     * @param number is the number to verify.
     * @param row is the row where the number has to be placed.
     * @param column is the column where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    public static boolean verifyNumber(Grid grid, int number, int row, int column) {
        return verifyCellsNumber(grid, number, calc(grid, row), calc(grid, column));
    }

    /**
     * Verifies if the number can be placed in the row.
     * @param grid is the grid to verify.
     * @param number is the number to verify.
     * @param row is the row where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    public static boolean verifyRowNumber(Grid grid, int number, int row) {
        for (int j = 0; j < grid.getGridSize(); ++j) {
            Cell cell = grid.getCells()[toExact(row)][j];
            if (verifyCellNumber(cell, number)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if the number can be placed in the column.
     * @param grid is the grid to verify.
     * @param number is the number to verify.
     * @param column is the column where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    public static boolean verifyColumnNumber(Grid grid, int number, int column) {
        for (int i = 0; i < grid.getGridSize(); ++i) {
            Cell cell = grid.getCells()[i][toExact(column)];
            if (verifyCellNumber(cell, number)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if the number to place is the same for every cell in the grid.
     * @param grid is the grid to verify.
     * @param number is the number to verify.
     * @param row is the row where the number has to be placed.
     * @param column is the column where the number has to be placed.
     * @return true if the number csn be placed, false otherwise.
     */
    private static boolean verifyCellsNumber(Grid grid, int number, int row, int column) {
        int blockSize = grid.getBlockSize();
        for (int i = row; i < row + blockSize; ++i) {
            for (int j = column; j < column + blockSize; ++j) {
                Cell cell = grid.getCells()[toExact(i)][toExact(j)];
                if (verifyCellNumber(cell, number)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifies if the number to place is the same in the cell.
     * @param cell is the cell to check.
     * @param number is the number to check.
     * @return true if the number to place is the same in the cell, false otherwise.
     */
    private static boolean verifyCellNumber(Cell cell, int number) {
        return cell.isFilled() && cell.getNumber() == number;
    }
}
