package it.unisa.studenti.citro.tiziano.model;

import lombok.Getter;

/**
 * Models the options when placing a number.
 */
public enum PlaceOptions {
    ALREADY_PLACED(0),
    ALREADY_PLACED_BUT_INCORRECT(1),
    CORRECT(2),
    WRONG(3);

    PlaceOptions(int option) {
        this.option = option;
    }

    /**
     * The option when the number has to be placed.
     */
    @Getter
    private int option;
}
