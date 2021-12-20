package it.unisa.studenti.citro.tiziano.sudoku.model;

import lombok.Getter;

import static it.unisa.studenti.citro.tiziano.sudoku.utils.GridUtils.toExact;

/**
 * Models the grid for a game.
 */
@Getter
public class Grid {

    /**
     * Builds the grid with a given block size.
     * @param blockSize is the block size to use.
     */
    public Grid(int blockSize) {
        this.blockSize = blockSize;
        this.gridSize = this.blockSize * this.blockSize;
        initCells();
    }

    /**
     * Builds the grid with a given block size and with given cells.
     * @param blockSize is the block size to use.
     * @param cells are the cells to use to build the grid.
     */
    public Grid(int blockSize, Integer[][] cells) {
        this.blockSize = blockSize;
        this.gridSize = this.blockSize * this.blockSize;
        initCellsWithGivenCells(cells);
    }

    /**
     * Clear a cell in the grid.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     */
    public void clearCell(int row, int column) {
        cells[toExact(row)][toExact(column)].clear();
    }

    /**
     * Checks whether the cell is filled or not,
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     * @return true if the cell is filled, false otherwise.
     */
    public boolean isCellNotFilled(int row, int column) {
        return !cells[toExact(row)][toExact(column)].isFilled();
    }

    /**
     * Provides the grid as a matrix of cells.
     * @return the grid as a matrix of cells.
     */
    public Integer[][] getCellsNumbersAsMatrix() {
        Integer[][] cellsNumbers = new Integer[TOTAL_BLOCK_NUMBER][TOTAL_BLOCK_NUMBER];
        for (int row = 0; row < TOTAL_BLOCK_NUMBER; row++) {
            for (int column = 0; column < TOTAL_BLOCK_NUMBER; column++) {
                cellsNumbers[row][column] = getCellNumber(row + 1, column + 1);
            }
        }
        return cellsNumbers;
    }

    /**
     * Gets the number placed on a cell.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     * @return the number placed on the cell identified by row and col.
     */
    public int getCellNumber(int row, int column) {
        return cells[toExact(row)][toExact(column)].getNumber();
    }

    /**
     * Place a number on a cell.
     * @param number is the number to place.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     */
    public void setCellNumber(int number, int row, int column) {
        cells[toExact(row)][toExact(column)].setNumber(number);
    }

    /**
     * Adds a number so that it appear as excluded.
     * @param number is the number to exclude.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     */
    public void addNumberAsExcluded(int number, int row, int column) {
        cells[toExact(row)][toExact(column)].addNumberAsExcluded(number);
    }

    /**
     * Checks whether the number has been already used for the cell.
     * @param number is the number to check.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     * @return true if the number has been already used, false otherwise.
     */
    public boolean isNumberExcluded(int number, int row, int column) {
        return cells[toExact(row)][toExact(column)].isExcluded(number);
    }

    /**
     * Provides the number of excluded numbers.
     * @param row is the row where the cell is located.
     * @param column is the column where the cell is located.
     * @return the number of excluded numbers.
     */
    public int getCellExcludedNumbers(int row, int column) {
        return cells[toExact(row)][toExact(column)].getExcludedSize();
    }

    /**
     * Initializes the cells in the grid.
     */
    private void initCells() {
        cellsSize = gridSize * gridSize;
        cells = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                cells[i][j] = new Cell();
            }
        }
    }

    /**
     * Initializes the cells in the grid with given numbers.
     * @param cells are the numbers to place.
     */
    private void initCellsWithGivenCells(Integer[][] cells) {
        cellsSize = gridSize * gridSize;
        this.cells = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                this.cells[i][j] = new Cell(cells[i][j]);
            }
        }
    }

    /**
     * Size for blocks.
     */
    private int blockSize;

    /**
     * Grid size.
     */
    private int gridSize;

    /**
     * Size for cells.
     */
    private int cellsSize;

    /**
     * The cells in the grid.
     */
    private Cell[][] cells;

    /**
     * Number of blocks in a subgrid.
     */
    public static final int BLOCK_NUMBER = 3;

    /**
     * Number of blocks in the grid.
     */
    public static final int TOTAL_BLOCK_NUMBER = 9;

    /**
     * Index of the first block in the grid.
     */
    public static final int FIRST = 1;
}
