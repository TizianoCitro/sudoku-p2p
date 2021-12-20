package it.unisa.studenti.citro.tiziano.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static it.unisa.studenti.citro.tiziano.sudoku.model.Cell.EMPTY_CELL;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.FIRST;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.TOTAL_BLOCK_NUMBER;

/**
 * Models a number to place for tests related to placing a number.
 */
@Getter
@AllArgsConstructor
public class PlaceNumber {

    /**
     * Gets a number to place based on the specified option.
     * @param sudoku is the sudoku where the number has to be placed.
     * @param option is the option for the number to get.
     * @return the number to place based on the specified option.
     */
    public static PlaceNumber getPlaceNumber(Integer[][] sudoku, int option) {
        PlaceNumber placeNumber;
        for (int i = 0; i < TOTAL_BLOCK_NUMBER; i++) {
            for (int j = 0; j < TOTAL_BLOCK_NUMBER; j++) {
                int cellNumber = sudoku[i][j];
                switch (option) {
                    case 0:
                        placeNumber = getAlreadyPlacedNumber(i, j, cellNumber);
                        if (placeNumber != null) {
                            return placeNumber;
                        }
                        break;
                    case 1:
                        placeNumber = getAlreadyPlacedNumberButIncorrect(sudoku, i, j, cellNumber);
                        if (placeNumber != null) {
                            return placeNumber;
                        }
                        break;
                    case 2:
                        placeNumber = getCorrectNumber(sudoku, i, j, cellNumber);
                        if (placeNumber != null) {
                            return placeNumber;
                        }
                        break;
                    case 3:
                        placeNumber = getIncorrectNumber(sudoku, i, j, cellNumber);
                        if (placeNumber != null) {
                            return placeNumber;
                        }
                        break;
                    default: break;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a number that has already been placed.
     * @param i is the row for the number.
     * @param j is the column for the number.
     * @param cellNumber is the number in the identified cell.
     * @return the number to place.
     */
    private static PlaceNumber getAlreadyPlacedNumber(int i, int j, int cellNumber) {
        if (cellNumber != EMPTY_CELL) {
            return new PlaceNumber(i + 1, j + 1, cellNumber);
        }
        return null;
    }

    /**
     * Retrieves a number that has already been placed but incorrect.
     * @param sudoku is the sudoku where the number has to be placed.
     * @param i is the row for the number.
     * @param j is the column for the number.
     * @param cellNumber is the number in the identified cell.
     * @return the number to place.
     */
    private static PlaceNumber getAlreadyPlacedNumberButIncorrect(Integer[][] sudoku, int i, int j, int cellNumber) {
        if (cellNumber != EMPTY_CELL) {
            for (int col = 0; col < TOTAL_BLOCK_NUMBER; col++) {
                int number = sudoku[i][col];
                if (number != EMPTY_CELL && number != cellNumber) {
                    return new PlaceNumber(i + 1, j + 1, number);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a number that can be placed correctly.
     * @param sudoku is the sudoku where the number has to be placed.
     * @param i is the row for the number.
     * @param j is the column for the number.
     * @param cellNumber is the number in the identified cell.
     * @return the number to place.
     */
    private static PlaceNumber getCorrectNumber(Integer[][] sudoku, int i, int j, int cellNumber) {
        List<Integer> rowValues = new ArrayList<>();
        List<Integer> columnValues = new ArrayList<>();
        if (cellNumber == EMPTY_CELL) {
            for (int col = 0; col < TOTAL_BLOCK_NUMBER; col++) {
                rowValues.add(sudoku[i][col]);
            }
            for (int row = 0; row < TOTAL_BLOCK_NUMBER; row++) {
                columnValues.add(sudoku[row][j]);
            }
            for (int number = FIRST; number <= TOTAL_BLOCK_NUMBER; number++) {
                if (!rowValues.contains(number) && !columnValues.contains(number)) {
                    return new PlaceNumber(i + 1, j + 1, number);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a number that when placed will result in a incorrect attempt.
     * @param sudoku is the sudoku where the number has to be placed.
     * @param i is the row for the number.
     * @param j is the column for the number.
     * @param cellNumber is the number in the identified cell.
     * @return the number to place.
     */
    private static PlaceNumber getIncorrectNumber(Integer[][] sudoku, int i, int j, int cellNumber) {
        if (cellNumber == EMPTY_CELL) {
            for (int col = 0; col < TOTAL_BLOCK_NUMBER; col++) {
                int number = sudoku[i][col];
                if (number != EMPTY_CELL) {
                    return new PlaceNumber(i + 1, j + 1, number);
                }
            }
        }
        return null;
    }

    /**
     * The row where to place the number.
     */
    int row;

    /**
     * The column where to place the number.
     */
    int column;

    /**
     * The number to place.
     */
    int number;
}
