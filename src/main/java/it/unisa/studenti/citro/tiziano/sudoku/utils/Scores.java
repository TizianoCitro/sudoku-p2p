package it.unisa.studenti.citro.tiziano.sudoku.utils;

import lombok.Getter;

/**
 * Models scores when a user places a number on a cell in the grid.
 */
public enum Scores {
    INCORRECT_NUMBER(-1),
    CORRECT_NUMBER(1),
    NUMBER_ALREADY_PLACED(0);

    Scores(int score) {
        this.score = score;
    }

    /**
     * The score for the player who's attempting to place a number.
     */
    @Getter
    private int score;
}
