package it.unisa.studenti.citro.tiziano.sudoku.model;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

import static it.unisa.studenti.citro.tiziano.sudoku.model.Cell.EMPTY_CELL;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.*;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Position.nextCellPosition;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.GridUtils.getRandomCellNumber;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.Scores.*;
import static it.unisa.studenti.citro.tiziano.sudoku.verifier.GridVerifier.verifyGridNumber;
import static it.unisa.studenti.citro.tiziano.sudoku.verifier.SudokuVerifier.verifyPosition;

/**
 * Models a Sudoku game.
 */
@Getter
public class Sudoku {

    /**
     * Builds a Sudoku game with a given name.
     * It initializes the game grid with random values.
     * @param name is the name for the game.
     */
    public Sudoku(String name) {
        this.name = name;
        players = new LinkedList<>();
        scores = new LinkedList<>();
        initGrid(CELL_TO_CLEAN);
    }

    /**
     * Builds a Sudoku game with a given name.
     * It initializes the game grid with given values.
     * @param name is the name for the game.
     * @param cells the values for the grid.
     */
    public Sudoku(String name, Integer[][] cells) {
        this.name = name;
        players = new LinkedList<>();
        scores = new LinkedList<>();
        grid = new Grid(BLOCK_NUMBER, cells);
    }

    /**
     * Place a number on a cell in the grid.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     * @param number is the number to place.
     * @return the score based on the attempt.
     */
    public int placeNumber(int row, int column, int number) {
        // Number already placed
        if (grid.getCellNumber(row, column) != EMPTY_CELL) {
            return NUMBER_ALREADY_PLACED.getScore();
        }

        // Number to place is correct
        if (verifyPosition(this, row, column, number)) {
            grid.setCellNumber(number, row, column);
            return CORRECT_NUMBER.getScore();
        }

        // Number to place is not correct
        return INCORRECT_NUMBER.getScore();
    }

    /**
     * Checks if the game has been completed.
     * @return true if the game has been completed, false otherwise.
     */
    public boolean isCompleted() {
        for (int i = FIRST; i <= TOTAL_BLOCK_NUMBER; i++) {
            for (int j = FIRST; j <= TOTAL_BLOCK_NUMBER; j++) {
                if (grid.getCellNumber(i, j) == EMPTY_CELL)
                    return false;
            }
        }
        return true;
    }

    /**
     * Initializes the game grid with random values and cleans some cells for placing numbers in them.
     * @param cellsToClean is the number of cells to clean.
     */
    private void initGrid(int cellsToClean) {
        grid = new Grid(BLOCK_NUMBER);
        populateGrid(FIRST, FIRST);
        cleanCells(cellsToClean);
    }

    /**
     * Populates the game grid with random values.
     * @param row is the row for starting populating.
     * @param column is the column for starting populating.
     */
    private void populateGrid(int row, int column) {
        int gridSize = grid.getGridSize();
        if (grid.isCellNotFilled(gridSize, gridSize)) {
            while (grid.getCellExcludedNumbers(row, column) < gridSize) {
                int candidate;
                do {
                    candidate = getRandomCellNumber(gridSize);
                } while (grid.isNumberExcluded(candidate, row, column));
                if (verifyGridNumber(grid, candidate, row, column)) {
                    grid.setCellNumber(candidate, row, column);
                    Position nextPosition = nextCellPosition(grid, row, column);
                    int positionRow = nextPosition.getRow();
                    int positionColumn = nextPosition.getColumn();
                    if (positionRow <= gridSize && positionColumn <= gridSize) {
                        populateGrid(positionRow, positionColumn);
                    }
                } else {
                    grid.addNumberAsExcluded(candidate, row, column);
                }
            }
            if (grid.isCellNotFilled(gridSize, gridSize)) {
                grid.clearCell(row, column);
            }
        }
    }

    /**
     * Cleans some cells for placing numbers in them.
     * @param cellsToClean is the number of cells to clean.
     */
    private void cleanCells(int cellsToClean) {
        int round = TOTAL_BLOCK_NUMBER + 1;
        for (int cell = 0; cell < cellsToClean; cell++) {
            int row = (int) (Math.random() * round) % TOTAL_BLOCK_NUMBER;
            int column = (int) (Math.random() * round) % TOTAL_BLOCK_NUMBER;
            grid.setCellNumber(EMPTY_CELL, row + 1, column + 1);
        }
    }

    /**
     * The game name.
     */
    private String name;

    /**
     * The game grid.
     */
    private Grid grid;

    /**
     * Users who have been playing the game.
     */
    private List<String> players;

    /**
     * Scores of the users who have been playing the game.
     */
    private List<Integer> scores;

    /**
     * The default number of cells to clean.
     */
    public static final Integer CELL_TO_CLEAN = 6;
}
