package it.unisa.studenti.citro.tiziano.sudoku.printer;

import org.beryx.textio.TextTerminal;

import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.BLOCK_NUMBER;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.TOTAL_BLOCK_NUMBER;

/**
 * Provides pretty printing capabilities for making the game more comfortable.
 */
public class SudokuPrettyPrinter {

    /**
     * Prints the game grid on the terminal.
     * @param cellsNumbers are the cells' numbers in the grid that must be printed.
     * @param terminal is the terminal where to print th grid.
     */
    public static void printOnTerminal(Integer[][] cellsNumbers, TextTerminal<?> terminal) {
        terminal.println();
        terminal.print("    ");
        for (int i = 0; i < TOTAL_BLOCK_NUMBER; i++) {
            terminal.printf(" %d", i + 1);
            if (i % BLOCK_NUMBER == 2) {
                terminal.print(" ");
            }
        }
        terminal.println();
        terminal.print("     ");
        int stop = TOTAL_BLOCK_NUMBER + 1;
        for (int k = 0; k < stop; k++) {
            terminal.print("__");
        }
        terminal.println();
        for (int i = 0; i < TOTAL_BLOCK_NUMBER; i++) {
            terminal.printf("  %d ", i + 1);
            for (int j = 0; j < TOTAL_BLOCK_NUMBER; j++) {
                if (j % BLOCK_NUMBER == 0) {
                    terminal.print("|");
                }
                if (cellsNumbers[i][j] == 0) {
                    terminal.print("X ");
                } else {
                    terminal.printf("%d ", cellsNumbers[i][j]);
                }
                if (j == TOTAL_BLOCK_NUMBER - 1) {
                    terminal.print("|");
                }
            }
            terminal.println();
            if (i % BLOCK_NUMBER == 2) {
                terminal.print("     ");
                for (int k = 0; k < stop; k++) {
                    terminal.print("__");
                }
                terminal.println();
            }
        }
    }
}
