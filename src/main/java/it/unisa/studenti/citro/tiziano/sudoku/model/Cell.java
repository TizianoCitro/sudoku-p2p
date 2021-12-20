package it.unisa.studenti.citro.tiziano.sudoku.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Models a single cell in a grid.
 */
public class Cell {

    public Cell() {
        excluded = new HashSet<>();
    }

    /**
     * Builds a cell by placing a number on it and also setting it as filled.
     * @param number is the number to place.
     */
    public Cell(int number) {
        this.number = number;
        isFilled = true;
        excluded = new HashSet<>();
    }

    /**
     * Sets the number for a cell while setting it as filled.
     * @param number is the number to insert.
     */
    public void setNumber(int number) {
        this.number = number;
        isFilled = true;
        excluded.add(number);
    }

    /**
     * Clear a cell by setting it as an empty cell.
     */
    public void clear() {
        number = EMPTY_CELL;
        isFilled = false;
        excluded.clear();
    }

    /**
     * Checks whether the number has been already used for the cell.
     * @param number is the number to check.
     * @return true if the number has been already used, false otherwise.
     */
    public boolean isExcluded(int number) {
        return excluded.contains(number);
    }

    /**
     * Adds a number so that it appear as excluded.
     * @param number is the number to exclude.
     */
    public void addNumberAsExcluded(int number) {
        excluded.add(number);
    }

    /**
     * Provides the number of excluded numbers.
     * @return the number of excluded numbers.
     */
    public int getExcludedSize() {
        return excluded.size();
    }

    /**
     * Number placed on the cell.
     */
    @Getter
    private int number;

    /**
     * Whether the cell is filled or not.
     */
    @Getter
    private boolean isFilled;

    /**
     * Numbers excluded for the cell because they've been used already.
     */
    private Set<Integer> excluded;

    /**
     * Value for an empty cell.
     */
    public static final int EMPTY_CELL = 0;
}
