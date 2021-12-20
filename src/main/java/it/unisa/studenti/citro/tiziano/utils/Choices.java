package it.unisa.studenti.citro.tiziano.utils;

import lombok.Getter;

/**
 * Models choices that a user can make.
 */
public enum Choices {
    CONFIRM("Y"),
    CANCEL("X");

    Choices(String choice) {
        this.choice = choice;
    }

    /**
     * The user choice.
     */
    @Getter
    private String choice;
}
