package it.unisa.studenti.citro.tiziano.model;

import lombok.Getter;

/**
 * Models the options when placing a number.
 */
public enum PlaceOptions {
    ALREADY_PLACED(0),
    CORRECT(1),
    WRONG(2);

    PlaceOptions(int option) {
        this.option = option;
    }

    /**
     * The option when the number has to be placed.
     */
    @Getter
    private int option;
}
