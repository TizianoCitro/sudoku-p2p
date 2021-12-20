package it.unisa.studenti.citro.tiziano.utils;

import lombok.Getter;

/**
 * Models game options that a user can choose when playing.
 */
public enum GameOptions {
    NEW_GAME(1),
    JOIN(2),
    JOINED(3),
    GET_GAME(4),
    PLACE_NUMBER(5),
    LEAVE(6),
    EXIT(7),
    DEFAULT(-1);

    GameOptions(int option) {
        this.option = option;
    }

    /**
     * The option selected by the user.
     */
    @Getter
    private int option;
}
