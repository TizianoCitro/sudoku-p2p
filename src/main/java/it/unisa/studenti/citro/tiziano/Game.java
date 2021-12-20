package it.unisa.studenti.citro.tiziano;

import it.unisa.studenti.citro.tiziano.listener.MessageListenerImpl;
import it.unisa.studenti.citro.tiziano.sudoku.SudokuGameImpl;
import it.unisa.studenti.citro.tiziano.sudoku.printer.SudokuPrettyPrinter;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.List;

import static it.unisa.studenti.citro.tiziano.sudoku.utils.SudokuUtils.ERROR_WHILE_PLACING_NUMBER;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.Scores.*;
import static it.unisa.studenti.citro.tiziano.utils.Choices.CANCEL;
import static it.unisa.studenti.citro.tiziano.utils.Choices.CONFIRM;
import static it.unisa.studenti.citro.tiziano.utils.GameOptions.*;

/**
 * Models a game.
 */
public class Game {

    /**
     * Allows to play the game.
     */
    public static void main(String[] args) throws Exception {
        try {
            CmdLineParser parser = new CmdLineParser(new Game());
            parser.parseArgument(args);
            TextIO textIO = TextIoFactory.getTextIO();
            TextTerminal<?> terminal = textIO.getTextTerminal();
            SudokuGameImpl peer = new SudokuGameImpl(id, master, new MessageListenerImpl(id));
            terminal.printf("Starting peer with id %d on %s\n", id, master);
            while (true) {
                int option = readOption(textIO, terminal);
                switch (option) {
                    case 1:
                        generateNewSudoku(peer, terminal, textIO);
                        break;
                    case 2:
                        join(peer, terminal, textIO);
                        break;
                    case 3:
                        joined(peer, terminal);
                        break;
                    case 4:
                        getSudoku(peer, terminal, textIO);
                        break;
                    case 5:
                        placeNumber(peer, terminal, textIO);
                        break;
                    case 6:
                        leave(peer, terminal, textIO);
                        break;
                    case 7:
                        exit(peer, terminal, textIO);
                        break;
                    default: break;
                }
            }
        } catch (CmdLineException e) {
            System.err.printf("Error with command line %s\n", e.getMessage());
        }
    }

    /**
     * Creates a new sudoku game.
     * @param peer si the user playing the game
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     */
    private static void generateNewSudoku(SudokuGameImpl peer, TextTerminal<?> terminal, TextIO textIO) {
        String name = textIO.newStringInputReader()
                .withDefaultValue("default-sudoku")
                .read("\nEnter a name for the game");
        Integer[][] cells = peer.generateNewSudoku(name);
        if (cells == null) {
            terminal.printf("\nCannot create game with name %s, it may already exist.\n", name);
            return;
        }
        terminal.printf("\nGame with name %s created successfully. Let's play!\n", name);
        SudokuPrettyPrinter.printOnTerminal(peer.getSudoku(name), terminal);
    }

    /**
     * Joins in a game.
     * @param peer si the user playing the game
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     */
    private static void join(SudokuGameImpl peer, TextTerminal<?> terminal, TextIO textIO) {
        String name = textIO.newStringInputReader()
                .withDefaultValue("default-sudoku")
                .read("\nName of the game you want to join");
        String nickname = textIO.newStringInputReader()
                .withDefaultValue("default-nickname")
                .read("\nNickname you want to join the game with");
        if (peer.join(name, nickname)) {
            terminal.printf("\nSuccessfully joined game with name %s as %s. Choose %s to see the game grid.\n",
                    name, nickname, GET_GAME.getOption());
            return;
        }
        terminal.printf("\nCannot join game with name %s. You may have already joined it or the nickname %s is already taken.\n",
                    name, nickname);
    }

    /**
     * Provides the list of joined game.
     * @param peer si the user playing the game
     * @param terminal is the terminal where to write.
     */
    private static boolean joined(SudokuGameImpl peer, TextTerminal<?> terminal) {
        List<String> joined = peer.joined();
        if (joined.isEmpty()) {
            terminal.println("You've not joined a game yet.");
            return false;
        }
        terminal.printf("\nYou've joined the following games:\n");
        joined.forEach(j -> terminal.printf("- %s\n", j));
        return true;
    }

    /**
     * Gets the Sudoku matrix game, with only the number placed by the user.
     * @param peer si the user playing the game
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     */
    private static void getSudoku(SudokuGameImpl peer, TextTerminal<?> terminal, TextIO textIO) {
        String name = textIO.newStringInputReader()
                .withDefaultValue("default-sudoku")
                .read("\nName of the game you want to see");
        Integer[][] cells = peer.getSudoku(name);
        if (cells != null) {
            terminal.printf("\nSuccessfully retrieved game with name %s.\n", name);
            SudokuPrettyPrinter.printOnTerminal(cells, terminal);
            return;
        }
        terminal.printf("\nCannot retrieve game with name %s.\n", name);
    }

    /**
     * Places a new solution number in the game.
     * @param peer si the user playing the game
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     */
    private static void placeNumber(SudokuGameImpl peer, TextTerminal<?> terminal, TextIO textIO) {
        String name = textIO.newStringInputReader().withDefaultValue("default-sudoku")
                .read("\nName of the game where to put number");
        int number = textIO.newIntInputReader().withMinVal(1).withMaxVal(9)
                .read("\nNumber you want to place (1-9):");
        int row = textIO.newIntInputReader().withMinVal(1).withMaxVal(9)
                .read("\nRow where you want to place the number (1-9):");
        int column = textIO.newIntInputReader().withMinVal(1).withMaxVal(9)
                .read("\nColumn where you want to place the number (1-9):");
        Integer score = peer.placeNumber(name, row, column, number);
        if (score == ERROR_WHILE_PLACING_NUMBER) {
            terminal.printf("\nCannot place number on game %s.\n", name);
            return;
        }
        if (score == NUMBER_ALREADY_PLACED.getScore()) {
            terminal.printf("\nNumber already placed.\n");
        }
        if (score == CORRECT_NUMBER.getScore()) {
            terminal.printf("\nNumber placed! You gained 1 point.\n");
        }
        if (score == INCORRECT_NUMBER.getScore()) {
            terminal.printf("\nWrong number! You lost 1 point.\n");
        }
        SudokuPrettyPrinter.printOnTerminal(peer.getSudoku(name), terminal);
    }

    /**
     * Leaves a game.
     * @param peer si the user playing the game
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     */
    private static void leave(SudokuGameImpl peer, TextTerminal<?> terminal, TextIO textIO) {
        if (!joined(peer, terminal)) {
            terminal.println("There's no game to leave.");
            return;
        }
        String name = textIO.newStringInputReader()
                .withDefaultValue("default-sudoku")
                .read("\nName of the game you want to leave");
        if (peer.leave(name)) {
            terminal.printf("\nSuccessfully left game with name %s.\n", name);
            return;
        }
        terminal.printf("\nCannot leave game with name %s.\n", name);
    }

    /**
     * Allows the user to exit.
     * @param peer si the user playing the game
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     */
    private static void exit(SudokuGameImpl peer, TextTerminal<?> terminal, TextIO textIO) {
        String choice = textIO.newStringInputReader()
                .withDefaultValue(CANCEL.getChoice())
                .read("\nDo you really want to leave (Y/N)?");
        if (!choice.equalsIgnoreCase(CONFIRM.getChoice())) {
            return;
        }
        if (!peer.leaveNetwork()) {
            terminal.printf("\nCannot leave the network. Please try again.\n");
            return;
        }
        terminal.printf("\nNetwork left successfully\n");
        System.exit(0);
    }

    /**
     * Reads what the user has selected as an option.
     * @param textIO is the stream where to read.
     * @param terminal is the terminal where to write.
     * @return the option selected by the user.
     */
    private static int readOption(TextIO textIO, TextTerminal<?> terminal) {
        printMenu(terminal);
        String option = textIO.newStringInputReader().withMinLength(0).read("\nWhat to do?");
        try {
            return Integer.parseInt(option);
        } catch (Exception e) {
            return DEFAULT.getOption();
        }
    }

    /**
     * Prints the menu with the option the user can choose.
     * @param terminal is the terminal where to write.
     */
    private static void printMenu(TextTerminal<?> terminal) {
        terminal.println("\nThere is what you can do:");
        terminal.printf("%d - Create new game\n", NEW_GAME.getOption());
        terminal.printf("%d - Join game\n", JOIN.getOption());
        terminal.printf("%d - See joined games\n", JOINED.getOption());
        terminal.printf("%d - Get the game grid\n", GET_GAME.getOption());
        terminal.printf("%d - Place a number\n", PLACE_NUMBER.getOption());
        terminal.printf("%d - Leave game\n", LEAVE.getOption());
        terminal.printf("%d - Exit\n", EXIT.getOption());
    }

    /**
     * The master peer ip address.
     */
    @Option(name = "-m", aliases = "--masterIp", usage= "the master peer ip address", required = true)
    private static String master;

    /**
     * The unique identifier for the peer.
     */
    @Option(name = "-id", aliases = "--peerIdentifier", usage = "the unique identifier for the peer", required = true)
    private static int id;
}
