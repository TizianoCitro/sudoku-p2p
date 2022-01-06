package it.unisa.studenti.citro.tiziano;

import it.unisa.studenti.citro.tiziano.listener.MessageListenerImpl;
import it.unisa.studenti.citro.tiziano.model.PlaceNumber;
import it.unisa.studenti.citro.tiziano.sudoku.SudokuGameImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import static it.unisa.studenti.citro.tiziano.model.PlaceNumber.getPlaceNumber;
import static it.unisa.studenti.citro.tiziano.model.PlaceOptions.*;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.FIRST;
import static it.unisa.studenti.citro.tiziano.sudoku.model.Grid.TOTAL_BLOCK_NUMBER;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.Scores.*;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.SudokuUtils.ERROR_WHILE_PLACING_NUMBER;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Models the unit tests.
 */
@TestMethodOrder(OrderAnnotation.class)
public class SudokuGameTest {

    /**
     * Tests the generation of a new Sudoku game by a peer
     * and the subsequent joining of other peers.
     */
    @Test
    @Order(1)
    public void generateNewSudokuAndJoin() {
        assertNotNull(peer1.generateNewSudoku(GAME), "PEER-ONE has generated game with name GAME");
        assertTrue(peer1.join(GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertTrue(peer2.join(GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertTrue(peer3.join(GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertTrue(peer4.join(GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");
    }

    /**
     * Tests the generation of a duplicated game.
     */
    @Test
    @Order(2)
    public void generateAlreadyExistingSudoku() {
        assertNull(peer1.generateNewSudoku(GAME),
                "PEER-ONE has tried to generate GAME game again");
        assertNull(peer2.generateNewSudoku(GAME),
                "PEER-TWO has tried to generate GAME game again");
        assertNull(peer3.generateNewSudoku(GAME),
                "PEER-THREE has tried to generate GAME game again");
        assertNull(peer4.generateNewSudoku(GAME),
                "PEER-FOUR has tried to generate GAME game again");
    }

    /**
     * Tests that it is not possible to join a game that does not exist.
     */
    @Test
    @Order(3)
    public void joinNotExistingGame() {
        assertFalse(peer1.join(NOT_EXISTING_GAME, PEER_ONE),
                "PEER-ONE has tried to join a not existing game");
        assertFalse(peer2.join(NOT_EXISTING_GAME, PEER_TWO),
                "PEER-TWO has tried to join a not existing game");
        assertFalse(peer3.join(NOT_EXISTING_GAME, PEER_THREE),
                "PEER-THREE has tried to join a not existing game");
        assertFalse(peer4.join(NOT_EXISTING_GAME, PEER_FOUR),
                "PEER-FOUR has tried to join a not existing game");
    }

    /**
     * Tests that it is not possible to join a game that has been joined already.
     */
    @Test
    @Order(4)
    public void joinAlreadyJoinedGame() {
        assertTrue(peer1.join(GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertFalse(peer1.join(GAME, PEER_ONE), "PEER-ONE has tried to join the game GAME again");

        assertTrue(peer2.join(GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertFalse(peer2.join(GAME, PEER_TWO), "PEER-TWO has tried to join the game GAME again");

        assertTrue(peer3.join(GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertFalse(peer3.join(GAME, PEER_THREE), "PEER-THREE has tried to join the game GAME again");

        assertTrue(peer4.join(GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");
        assertFalse(peer4.join(GAME, PEER_FOUR), "PEER-FOUR has tried to join the game GAME again");
    }

    /**
     * Tests the possibility to retrieve joined games when at least one game has been joined.
     */
    @Test
    @Order(5)
    public void joinedGames() {
        assertTrue(peer3.join(GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        List<String> joined = peer3.joined();
        assertNotNull(joined);
        assertFalse(joined.isEmpty(), "PEER-THREE has joined a game already");
    }

    /**
     * Tests that no games is retrieved when the peer has not joined a game yet.
     */
    @Test
    @Order(6)
    public void notJoinedGamesYet() {
        List<String> joined = peer3.joined();
        assertNotNull(joined);
        assertTrue(joined.isEmpty(), "PEER-THREE has not joined any game yet");
    }

    /**
     * Tests the possibility to retrieve a joined game grid when at least one game has been joined.
     */
    @Test
    @Order(7)
    public void getSudokuWhenJoined() {
        assertTrue(peer4.join(GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");
        Integer[][] sudoku = peer4.getSudoku(GAME);
        assertNotNull(sudoku, "PEER-FOUR has joined the game GAME, thus can get the grid");
    }

    /**
     * Tests that the game grid is not retrieved when the peer has not joined the game yet.
     */
    @Test
    @Order(8)
    public void getSudokuWhenGameDoesNotExist() {
        Integer[][] sudoku = peer4.getSudoku(NOT_EXISTING_GAME);
        assertNull(sudoku, "PEER-FOUR has tried to get the grid for a not existing game");
    }

    /**
     * Tests a peer leaving a game when the game had been joined.
     */
    @Test
    @Order(9)
    public void leaveGame() {
        assertTrue(peer1.join(GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertTrue(peer1.leave(GAME), "PEER-ONE has left the game GAME");

        assertTrue(peer2.join(GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertTrue(peer2.leave(GAME), "PEER-TWO has left the game GAME");

        assertTrue(peer3.join(GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertTrue(peer3.leave(GAME), "PEER-THREE has left the game GAME");

        assertTrue(peer4.join(GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");
        assertTrue(peer4.leave(GAME), "PEER-FOUR has left the game GAME");
    }

    /**
     * Tests that a peer cannot leave a game that has not joined.
     */
    @Test
    @Order(10)
    public void leaveNotJoinedGame() {
        assertFalse(peer1.leave(GAME), "PEER-ONE has not joined the game GAME, thus cannot left");
        assertFalse(peer2.leave(GAME), "PEER-TWO has not joined the game GAME, thus cannot left");
        assertFalse(peer3.leave(GAME), "PEER-THREE has not joined the game GAME, thus cannot left");
        assertFalse(peer4.leave(GAME), "PEER-FOUR has not joined the game GAME, thus cannot left");
    }

    /**
     * Tests that a peer cannot leave a game that does not exist.
     */
    @Test
    @Order(11)
    public void leaveNotExistingGame() {
        assertFalse(peer1.leave(NOT_EXISTING_GAME), "PEER-ONE has tried to leave a not existing game");
        assertFalse(peer2.leave(NOT_EXISTING_GAME), "PEER-TWO has tried to leave a not existing game");
        assertFalse(peer3.leave(NOT_EXISTING_GAME), "PEER-THREE has tried to leave a not existing game");
        assertFalse(peer4.leave(NOT_EXISTING_GAME), "PEER-FOUR has tried to leave a not existing game");
    }

    /**
     * Tests the functionality of placing a number
     * when the peer tries to place a number that has already been placed
     * and the tried number is correct.
     */
    @Test
    @Order(12)
    public void alreadyPlacedNumber() {
        Integer[][] sudoku = peer1.generateNewSudoku(ALREADY_PLACED_GAME);
        assertNotNull(sudoku, "PEER-ONE created a game for placing numbers");
        assertTrue(peer1.join(ALREADY_PLACED_GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertTrue(peer2.join(ALREADY_PLACED_GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertTrue(peer3.join(ALREADY_PLACED_GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertTrue(peer4.join(ALREADY_PLACED_GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");

        PlaceNumber placeNumber = getPlaceNumber(sudoku, ALREADY_PLACED.getOption());
        assertNotNull(placeNumber, "Cannot get number to place");
        Integer score = peer1.placeNumber(ALREADY_PLACED_GAME,
                placeNumber.getRow(), placeNumber.getColumn(), placeNumber.getNumber());
        assertEquals(NUMBER_ALREADY_PLACED.getScore(), score);
    }

    /**
     * Tests the functionality of placing a number
     * when the peer tries to place a correct number.
     */
    @Test
    @Order(13)
    public void placeCorrectNumber() {
        Integer[][] sudoku = peer3.generateNewSudoku(CORRECT_PLACED_GAME);
        assertNotNull(sudoku, "PEER-THREE created a game for placing numbers");
        assertTrue(peer1.join(CORRECT_PLACED_GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertTrue(peer2.join(CORRECT_PLACED_GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertTrue(peer3.join(CORRECT_PLACED_GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertTrue(peer4.join(CORRECT_PLACED_GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");

        PlaceNumber placeNumber = getPlaceNumber(sudoku, CORRECT.getOption());
        assertNotNull(placeNumber, "Cannot get number to place");
        int row = placeNumber.getRow();
        int column = placeNumber.getColumn();
        int number = placeNumber.getNumber();
        Integer score = peer3.placeNumber(CORRECT_PLACED_GAME, row, column, number);
        assertEquals(CORRECT_NUMBER.getScore(), score);

        // Verifying that all peers have the placed number.
        Integer[][] peer2Sudoku = peer2.getSudoku(CORRECT_PLACED_GAME);
        assertEquals(number, peer2Sudoku[row - 1][column -1]);
        Integer[][] peer3Sudoku = peer3.getSudoku(CORRECT_PLACED_GAME);
        assertEquals(number, peer3Sudoku[row - 1][column -1]);
        Integer[][] peer4Sudoku = peer4.getSudoku(CORRECT_PLACED_GAME);
        assertEquals(number, peer4Sudoku[row - 1][column -1]);
    }

    /**
     * Tests the functionality of placing a number
     * when the peer tries to place an incorrect number.
     */
    @Test
    @Order(14)
    public void placeIncorrectNumber() {
        Integer[][] sudoku = peer2.generateNewSudoku(INCORRECT_PLACED_GAME);
        assertNotNull(sudoku, "PEER-TWO created a game for placing numbers");
        assertTrue(peer1.join(INCORRECT_PLACED_GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertTrue(peer2.join(INCORRECT_PLACED_GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertTrue(peer3.join(INCORRECT_PLACED_GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertTrue(peer4.join(INCORRECT_PLACED_GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");

        PlaceNumber placeNumber = getPlaceNumber(sudoku, WRONG.getOption());
        assertNotNull(placeNumber, "Cannot get number to place");
        Integer score = peer2.placeNumber(INCORRECT_PLACED_GAME,
                placeNumber.getRow(), placeNumber.getColumn(), placeNumber.getNumber());
        assertEquals(INCORRECT_NUMBER.getScore(), score);
    }

    /**
     * Tests the functionality of placing a number
     * when the peer tries to place a number that has already been placed,
     * but the tried number is incorrect.
     */
    @Test
    @Order(15)
    public void alreadyPlacedNumberButIncorrect() {
        Integer[][] sudoku = peer1.generateNewSudoku(ALREADY_PLACED_INCORRECT_GAME);
        assertNotNull(sudoku, "PEER-ONE created a game for placing numbers");
        assertTrue(peer1.join(ALREADY_PLACED_INCORRECT_GAME, PEER_ONE), "PEER-ONE has joined the game GAME");
        assertTrue(peer2.join(ALREADY_PLACED_INCORRECT_GAME, PEER_TWO), "PEER-TWO has joined the game GAME");
        assertTrue(peer3.join(ALREADY_PLACED_INCORRECT_GAME, PEER_THREE), "PEER-THREE has joined the game GAME");
        assertTrue(peer4.join(ALREADY_PLACED_INCORRECT_GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");

        PlaceNumber placeNumber = getPlaceNumber(sudoku, ALREADY_PLACED_BUT_INCORRECT.getOption());
        assertNotNull(placeNumber, "Cannot get number to place");
        Integer score = peer1.placeNumber(ALREADY_PLACED_INCORRECT_GAME,
                placeNumber.getRow(), placeNumber.getColumn(), placeNumber.getNumber());
        assertEquals(INCORRECT_NUMBER.getScore(), score);
    }


    /**
     * Tests that a peer cannot place a number in a game that has not joined yet.
     */
    @Test
    @Order(16)
    public void placeNumberInNotJoinedGame() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, FIRST, FIRST, FIRST),
                "PEER-ONE has tried to place a number in a game that has not joined yet");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, FIRST, FIRST, FIRST),
                "PEER-TWO has tried to place a number in a game that has not joined yet");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, FIRST, FIRST, FIRST),
                "PEER-THREE has tried to place a number in a game that has not joined yet");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, FIRST, FIRST, FIRST),
                "PEER-FOUR has tried to place a number in a game that has not joined yet");
    }

    /**
     * Tests that a peer cannot place a number in a game that does not exist.
     */
    @Test
    @Order(17)
    public void placeNumberInNotExistingGame() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(NOT_EXISTING_GAME, FIRST, FIRST, FIRST),
                "PEER-ONE has tried to place a number in a game that does not exist");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(NOT_EXISTING_GAME, FIRST, FIRST, FIRST),
                "PEER-TWO has tried to place a number in a game that does not exist");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(NOT_EXISTING_GAME, FIRST, FIRST, FIRST),
                "PEER-THREE has tried to place a number in a game that does not exist");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(NOT_EXISTING_GAME, FIRST, FIRST, FIRST),
                "PEER-FOUR has tried to place a number in a game that does not exist");
    }

    /**
     * Tests that a peer cannot create a new game with empty name.
     */
    @Test
    @Order(18)
    public void generateNewSudokuWithEmptyName() {
        assertNull(peer1.generateNewSudoku(""), "PEER-ONE has tried to generate a game with empty name");
    }

    /**
     * Tests that peers cannot join when providing empty name for a game.
     */
    @Test
    @Order(19)
    public void joinSudokuWithEmptyName() {
        assertFalse(peer1.join("", PEER_ONE), "PEER-ONE has tried to join a game with empty name");
        assertFalse(peer2.join("", PEER_TWO), "PEER-TWO has tried to join a game with empty name");
        assertFalse(peer3.join("", PEER_THREE), "PEER-THREE has tried to join a game with empty name");
        assertFalse(peer4.join("", PEER_FOUR), "PEER-FOUR has tried to join a game with empty name");
    }

    /**
     * Tests that peers cannot join a game without providing a nickname.
     */
    @Test
    @Order(20)
    public void joinSudokuWithoutNickname() {
        assertFalse(peer1.join(GAME, ""), "PEER-ONE has tried to join a game without providing a nickname");
        assertFalse(peer2.join(GAME, ""), "PEER-TWO has tried to join a game without providing a nickname");
        assertFalse(peer3.join(GAME, ""), "PEER-THREE has tried to join a game without providing a nickname");
        assertFalse(peer4.join(GAME, ""), "PEER-FOUR has tried to join a game without providing a nickname");
    }

    /**
     * Tests the possibility to retrieve a joined game grid by providing an empty name for the game.
     */
    @Test
    @Order(21)
    public void getSudokuWhitEmptyName() {
        assertTrue(peer4.join(GAME, PEER_FOUR), "PEER-FOUR has joined the game GAME");
        Integer[][] sudoku = peer4.getSudoku("");
        assertNull(sudoku, "PEER-FOUR has tried to get a grid for a name but it has given an empty name");
    }

    /**
     * Tests that a peer cannot place a number when it has specified a wrong row index,
     * lower than the minimum possible.
     */
    @Test
    @Order(22)
    public void placeNumberInRowLowerThanMin() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, FIRST - 1, FIRST, FIRST),
                "PEER-ONE has tried to place a number in a not valid row");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, FIRST - 1, FIRST, FIRST),
                "PEER-TWO has tried to place a number in a not valid row");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, FIRST - 1, FIRST, FIRST),
                "PEER-THREE has tried to place a number in a not valid row");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, FIRST - 1, FIRST, FIRST),
                "PEER-FOUR has tried to place a number in a not valid row");
    }

    /**
     * Tests that a peer cannot place a number when it has specified a wrong row index,
     * higher than the maximum possible.
     */
    @Test
    @Order(23)
    public void placeNumberInRowHigherThanMax() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, TOTAL_BLOCK_NUMBER + 1, FIRST, FIRST),
                "PEER-ONE has tried to place a number in a not valid row");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, TOTAL_BLOCK_NUMBER + 1, FIRST, FIRST),
                "PEER-TWO has tried to place a number in a not valid row");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, TOTAL_BLOCK_NUMBER + 1, FIRST, FIRST),
                "PEER-THREE has tried to place a number in a not valid row");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, TOTAL_BLOCK_NUMBER + 1, FIRST, FIRST),
                "PEER-FOUR has tried to place a number in a not valid row");
    }

    /**
     * Tests that a peer cannot place a number when it has specified a wrong column index,
     * lower than the minimum possible.
     */
    @Test
    @Order(24)
    public void placeNumberInColumnLowerThanMin() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, FIRST, FIRST - 1, FIRST),
                "PEER-ONE has tried to place a number in a not valid column");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, FIRST, FIRST - 1, FIRST),
                "PEER-TWO has tried to place a number in a not valid column");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, FIRST, FIRST - 1, FIRST),
                "PEER-THREE has tried to place a number in a not valid column");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, FIRST, FIRST - 1, FIRST),
                "PEER-FOUR has tried to place a number in a not valid column");
    }

    /**
     * Tests that a peer cannot place a number when it has specified a wrong column index,
     * higher than the maximum possible.
     */
    @Test
    @Order(25)
    public void placeNumberInColumnHigherThanMax() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, FIRST, TOTAL_BLOCK_NUMBER + 1, FIRST),
                "PEER-ONE has tried to place a number in a not valid column");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, FIRST, TOTAL_BLOCK_NUMBER + 1, FIRST),
                "PEER-TWO has tried to place a number in a not valid column");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, FIRST, TOTAL_BLOCK_NUMBER + 1, FIRST),
                "PEER-THREE has tried to place a number in a not valid column");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, FIRST, TOTAL_BLOCK_NUMBER + 1, FIRST),
                "PEER-FOUR has tried to place a number in a not valid column");
    }

    /**
     * Tests that a peer cannot place a number when it ha specified a number lower than the minimum possible.
     */
    @Test
    @Order(26)
    public void placeNumberLowerThanMin() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, FIRST, FIRST, FIRST - 1),
                "PEER-ONE has tried to place a number lower than the minimum possible");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, FIRST, FIRST, FIRST - 1),
                "PEER-TWO has tried to place a number lower than the minimum possible");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, FIRST, FIRST, FIRST - 1),
                "PEER-THREE has tried to place a number lower than the minimum possible");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, FIRST, FIRST, FIRST - 1),
                "PEER-FOUR has tried to place a number lower than the minimum possible");
    }

    /**
     * Tests that a peer cannot place a number when it ha specified a number higher than the maximum possible.
     */
    @Test
    @Order(27)
    public void placeNumberHigherThanMax() {
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer1.placeNumber(GAME, FIRST, FIRST, TOTAL_BLOCK_NUMBER + 1),
                "PEER-ONE has tried to place a number higher than the maximum possible");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer2.placeNumber(GAME, FIRST, FIRST, TOTAL_BLOCK_NUMBER + 1),
                "PEER-TWO has tried to place a number higher than the maximum possible");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer3.placeNumber(GAME, FIRST, FIRST, TOTAL_BLOCK_NUMBER + 1),
                "PEER-THREE has tried to place a number higher than the maximum possible");
        assertEquals(ERROR_WHILE_PLACING_NUMBER, peer4.placeNumber(GAME, FIRST, FIRST, TOTAL_BLOCK_NUMBER + 1),
                "PEER-FOUR has tried to place a number higher than the maximum possible");
    }

    /**
     * Initializes the four peer for testing purposes.
     */
    @BeforeAll
    public static void setup() throws Exception {
        peer1 = new SudokuGameImpl(0, MASTER_IP, new MessageListenerImpl(0));
        peer2 = new SudokuGameImpl(1, MASTER_IP, new MessageListenerImpl(1));
        peer3 = new SudokuGameImpl(2, MASTER_IP, new MessageListenerImpl(2));
        peer4 = new SudokuGameImpl(3, MASTER_IP, new MessageListenerImpl(3));
    }

    /**
     * All peers leaves the games that they have joined in the previous test.
     */
    @AfterEach
    public void leaveAllGames() {
        leaveGames(peer1);
        leaveGames(peer2);
        leaveGames(peer3);
        leaveGames(peer4);
    }

    /**
     * All peers leave the network at the end of the testing process.
     */
    @AfterAll
    public static void leaveNetwork() {
        peer1.leaveNetwork();
        peer2.leaveNetwork();
        peer3.leaveNetwork();
        peer4.leaveNetwork();
    }

    /**
     * A peer leaves all the games joined in the previous test.
     * @param peer the peer that leaves the games.
     */
    private void leaveGames(SudokuGameImpl peer) {
        for (String game: new ArrayList<>(peer.getGamesNames())) {
            peer.leave(game);
        }
    }

    /**
     * Peers used for testing purposes.
     */
    private static SudokuGameImpl peer1;
    private static SudokuGameImpl peer2;
    private static SudokuGameImpl peer3;
    private static SudokuGameImpl peer4;

    /**
     * Games names used for testing purposes.
     */
    public static final String GAME = "GAME";
    public static final String NOT_EXISTING_GAME = "NOT_EXISTING";
    public static final String INCORRECT_PLACED_GAME = "INCORRECT_PLACED";
    public static final String CORRECT_PLACED_GAME = "CORRECT_PLACED";
    public static final String ALREADY_PLACED_GAME = "ALREADY_PLACED";
    public static final String ALREADY_PLACED_INCORRECT_GAME = "ALREADY_PLACED_INCORRECT";

    /**
     * Nicknames for the peers used for testing purposes.
     */
    public static final String PEER_ONE = "ONE";
    public static final String PEER_TWO = "TWO";
    public static final String PEER_THREE = "THREE";
    public static final String PEER_FOUR = "FOUR";

    /**
     * Default ip address,
     */
    public static final String MASTER_IP = "127.0.0.1";
}
