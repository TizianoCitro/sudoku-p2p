package it.unisa.studenti.citro.tiziano.sudoku.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Models a position in the game grid.
 */
@Getter
@Setter
@AllArgsConstructor
public class Position {

    /**
     * gets the next position in the grid.
     * @param grid is the grid for the next position.
     * @param row is the row for calculating the next position.
     * @param column is the column for calculating the next position.
     * @return the next position.
     */
    public static Position nextCellPosition(Grid grid, int row, int column) {
        int r = row;
        int col = column;
        if (col < grid.getGridSize()) {
            col += 1;
        } else {
            col = 1;
            r += 1;
        }
        return new Position(r, col);
    }

    /**
     * Row for the next position.
     */
    private int row;

    /**
     * Column for the next position.
     */
    private int column;
}
