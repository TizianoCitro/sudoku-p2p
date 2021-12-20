package it.unisa.studenti.citro.tiziano.sudoku;

import it.unisa.studenti.citro.tiziano.listener.MessageListener;
import it.unisa.studenti.citro.tiziano.sudoku.model.Sudoku;
import it.unisa.studenti.citro.tiziano.utils.GameUtils;
import lombok.Getter;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.unisa.studenti.citro.tiziano.sudoku.utils.SudokuUtils.*;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.Scores.CORRECT_NUMBER;
import static it.unisa.studenti.citro.tiziano.sudoku.utils.Scores.INCORRECT_NUMBER;
import static it.unisa.studenti.citro.tiziano.utils.GameUtils.isGameValid;

/**
 * Copyright 2017 Universita' degli Studi di Salerno.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * A P2P Sudoku challenge games. Each user can place a number of the
 * sudoku game, if it is not already placed takes 1 point, if it is already placed and it is rights takes
 * 0 point, in other case receive -1 point. The games is based on 9 x 9 matrix.
 * All users that play to a game are automatically informed when a users increment its score, and when
 * the game is finished.
 */
public class SudokuGameImpl implements SudokuGame {

    /**
     * Builds a peer for playing the Sudoku game.
     * @param _id is the peer id.
     * @param _master_peer is the master peer.
     * @param _listener is the message listener.
     */
    public SudokuGameImpl(int _id, String _master_peer, MessageListener _listener) throws Exception {
        peer = new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_PORT +_id).start();
        dht = new PeerBuilderDHT(peer).start();
        init();

        FutureBootstrap futureBootstrap = peer.bootstrap()
                .inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_PORT).start();
        futureBootstrap.awaitUninterruptibly();
        if (futureBootstrap.isSuccess()) {
            peer.discover().peerAddress(futureBootstrap.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        } else {
            throw new Exception("Error in master peer bootstrap");
        }
        peer.objectDataReply((sender, request) -> _listener.parseMessage(request));
    }

    /**
     * Creates a new sudoku game.
     * @param _game_name a String, the sudoku game name.
     * @return bidimensional array containing the grid field of the sudoku game created.
     */
    @Override
    public Integer[][] generateNewSudoku(String _game_name) {
        if (!GameUtils.isGameNameValid(_game_name)) {
            return null;
        }
        sudoku = new Sudoku(_game_name);
        Integer[][] cellsNumbers = sudoku.getGrid().getCellsNumbersAsMatrix();
        try {
            if (retrieveSudoku(_game_name) != null) {
                return null;
            }
            if (!putSudoku(_game_name, cellsNumbers)) {
                return null;
            }
            putPlayers(_game_name);
            putNicknames(_game_name);
            putScores(_game_name);
            return cellsNumbers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Joins in a game.
     * @param _game_name a String, the sudoku game name.
     * @param _nickname a String, the name of the user.
     * @return true if the join is successful, false otherwise.
     */
    @Override
    public boolean join(String _game_name, String _nickname) {
        if (!isGameValid(_game_name, _nickname, gamesNames)) {
            return false;
        }
        try {
            if (!addNickname(_game_name, _nickname)) {
                return false;
            }
            Integer[][] cellsNumbers = addSudoku(_game_name, _nickname);
            if (cellsNumbers == null) {
                return false;
            }
            if (!addPlayer(_game_name)) {
                return false;
            }
            return addScore(_game_name);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the joined games names.
     * @return a list of joined games names, an empty list if none found.
     */
    @Override
    public List<String> joined() {
        return gamesNames;
    }

    /**
     * Gets the Sudoku matrix game, with only the number placed by the user.
     * @param _game_name a String, the sudoku game name.
     * @return the integer matrix of the sudoku game.
     */
    @Override
    public Integer[][] getSudoku(String _game_name) {
        try {
            return retrieveSudoku(_game_name);
       } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Places a new solution number in the game.
     * @param _game_name a String, the sudoku game name.
     * @param _i the position on the row.
     * @param _j the position on the column.
     * @param _number the solution number.
     * @return the integer score of the placed number.
     */
    @Override
    public Integer placeNumber(String _game_name, int _i, int _j, int _number) {
        if (!isGameValid(_game_name, _i, _j, _number, gamesNames)) {
            return ERROR_WHILE_PLACING_NUMBER;
        }
        try {
            Integer[][] cells = retrieveSudoku(_game_name);
            sudoku = new Sudoku(_game_name, cells);
            int score = sudoku.placeNumber(_i, _j, _number);
            // Update the player score
            if (score == CORRECT_NUMBER.getScore() || score == INCORRECT_NUMBER.getScore()) {
                // Get the position and the nickname used for the sudoku
                // dht index and then index in local list
                int globalIndex = -1;
                int localIndex = gamesNames.indexOf(_game_name);
                String nickname = gamesNicknames.get(localIndex);
                List<String> nicknames = retrieveNicknames(_game_name);
                if (nicknames == null) {
                    return ERROR_WHILE_PLACING_NUMBER;
                }
                globalIndex = nicknames.indexOf(nickname);
                if (globalIndex == -1) {
                    return ERROR_WHILE_PLACING_NUMBER;
                }
                // Updating the personal score on the list for that sudoku game.
                List<Integer> scores = updateScore(_game_name, score, globalIndex);
                if (scores == null) {
                    return ERROR_WHILE_PLACING_NUMBER;
                }
                // Updating the sudoku field in dht as well
                if (score == CORRECT_NUMBER.getScore()) {
                    Integer[][] cellsNumbers = sudoku.getGrid().getCellsNumbersAsMatrix();
                    putSudoku(_game_name, cellsNumbers);
                    // Notify all the players for that Sudoku about the new score
                    notifyPlayers(_game_name, scores, nickname, nicknames);
                }
            }
            return score;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_WHILE_PLACING_NUMBER;
        }
    }

    /**
     * Leaves a game.
     * @param _game_name a String, the sudoku game name.
     * @return true if the game is left successfully, false otherwise.
     */
    @Override
    public boolean leave(String _game_name) {
        if (!gamesNames.contains(_game_name)) {
            return false;
        }
        try {
            if (!removePlayer(_game_name)) {
                return false;
            }
            int index = removeNickname(_game_name);
            if (index == -1) {
                return false;
            }
            if (!removeScore(_game_name, index)) {
                return false;
            }
            clear(index);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Leaves the network, thus leaves all the games joined previously.
     * @return true if the network is left successfully, false otherwise.
     */
    @Override
    public boolean leaveNetwork() {
        for (String game: new ArrayList<>(gamesNames)) {
            leave(game);
        }
        dht.peer().announceShutdown().start().awaitUninterruptibly();
        return true;
    }

    /**
     * Notifies all players when a number is placed in the game grid.
     * @param _game_name is the game name.
     * @param scores are the players score.
     * @param nickname is the nickname of the player who has placed the number.
     * @param nicknames are the players score.
     */
    private void notifyPlayers(
            String _game_name, List<Integer> scores,
            String nickname, List<String> nicknames) throws IOException, ClassNotFoundException {
        FutureGet futureGet = dht.get(Number160.createHash(withPlayers(_game_name))).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            String message = buildNotification(_game_name, scores, nickname, nicknames);
            Set<PeerAddress> peers = (Set<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
            for (PeerAddress peer: peers) {
                FutureDirect futureDirect = dht.peer().sendDirect(peer).object(message).start();
                futureDirect.awaitUninterruptibly();
            }
        }
    }

    /**
     * Builds a message to send to all players when a number is placed in the game grid.
     * @param _game_name is the game name.
     * @param scores are the players score.
     * @param nickname is the nickname of the player who has placed the number.
     * @param nicknames are the players score.
     * @return the message to send.
     */
    private String buildNotification(String _game_name, List<Integer> scores, String nickname, List<String> nicknames) {
        StringBuilder messageBuilder = new StringBuilder(_game_name).append(" - ").append(nickname);
        if (sudoku.isCompleted()) {
            messageBuilder.append(" has completed the sudoku!\n").append("Scores:\n");
            for (int i = 0; i < nicknames.size(); i++) {
                messageBuilder.append("- ").append(nicknames.get(i)).append(": ").append(scores.get(i));
                if (i < nicknames.size() - 1) {
                    messageBuilder.append("\n");
                }
            }
        } else {
            messageBuilder.append(" has scored a point!");
        }
        return messageBuilder.toString();
    }

    /**
     * Retrieves a game based on its name.
     * @param _game_name is the game name.
     * @return the game grid as a matrix of integers.
     */
    private Integer[][] retrieveSudoku(String _game_name) throws IOException, ClassNotFoundException {
        FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (!futureGet.isEmpty()) {
                return (Integer[][]) futureGet.dataMap().values().iterator().next().object();
            }
        }
        return null;
    }

    /**
     * Retrieves the nicknames of the players who have been playing a certain game.
     * @param _game_name is the game name.
     * @return the nicknames of the players.
     */
    private List<String> retrieveNicknames(String _game_name) throws IOException, ClassNotFoundException {
        FutureGet futureGet = dht.get(Number160.createHash(withNicknames(_game_name))).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (!futureGet.isEmpty()) {
                return (List<String>) futureGet.dataMap().values().iterator().next().object();
            }
        }
        return null;
    }

    /**
     * Stores the grid of a game.
     * @param _game_name is the game name.
     * @param cellsNumbers the grid of the games as a matrix of integers.
     * @return true if the game has been stored successfully.
     */
    private boolean putSudoku(String _game_name, Integer[][] cellsNumbers) throws IOException {
        FutureGet futureGet = dht.get(Number160.createHash(_game_name)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            dht.put(Number160.createHash(_game_name)).data(new Data(cellsNumbers)).start().awaitUninterruptibly();
            return true;
        }
        // A sudoku with a name equals to _game_name may already exist.
        return false;
    }

    /**
     * Stores new players for a game.
     * @param _game_name is the game name.
     */
    private void putPlayers(String _game_name) throws IOException {
        String withPlayers = withPlayers(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withPlayers)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess() && futureGet.isEmpty()) {
            dht.put(Number160.createHash(withPlayers)).data(new Data(new HashSet<PeerAddress>()))
                    .start().awaitUninterruptibly();
        }
    }

    /**
     * Stores the nicknames for players in a game.
     * @param _game_name is the game name.
     */
    private void putNicknames(String _game_name) throws IOException {
        String withNicknames = withNicknames(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withNicknames)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess() && futureGet.isEmpty()) {
            dht.put(Number160.createHash(withNicknames)).data(new Data(new ArrayList<String>()))
                    .start().awaitUninterruptibly();
        }
    }

    /**
     * Stores the scores for players in a game.
     * @param _game_name is the game name.
     */
    private void putScores(String _game_name) throws IOException {
        String withScores = withScores(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withScores)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess() && futureGet.isEmpty()) {
            dht.put(Number160.createHash(withScores)).data(new Data(new ArrayList<Integer>()))
                    .start().awaitUninterruptibly();
        }
    }

    /**
     * Updates the score for a player who's playing a certain game.
     * @param _game_name  is the game name.
     * @param score the score to update.
     * @param globalIndex index of the score to update.
     * @return the updated scores.
     */
    private List<Integer> updateScore(String _game_name, int score, int globalIndex) throws IOException, ClassNotFoundException {
        String withScores = withScores(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withScores)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return null;
            }
            List<Integer> scores = (List<Integer>) futureGet.dataMap().values().iterator().next().object();
            scores.set(globalIndex, scores.get(globalIndex) + score);
            dht.put(Number160.createHash(withScores)).data(new Data(scores)).start().awaitUninterruptibly();
            return scores;
        }
        return null;
    }

    /**
     * Adds a new game to the games a players' playing.
     * @param _game_name  is the game name.
     * @param _nickname is the nickname the player has joined with.
     * @return the game grid.
     */
    private Integer[][] addSudoku(String _game_name, String _nickname) throws ClassNotFoundException, IOException {
        Integer[][] cellsNumbers = retrieveSudoku(_game_name);
        if (cellsNumbers == null) {
            return null;
        }
        // Adding sudoku and sudoku name to joined games.
        games.add(cellsNumbers);
        gamesNames.add(_game_name);
        gamesNicknames.add(_nickname);
        return cellsNumbers;
    }

    /**
     * Adds a player to a game.
     * @param _game_name is the game name.
     * @return true if the player has been added successfully, false otherwise.
     */
    private boolean addPlayer(String _game_name) throws IOException, ClassNotFoundException {
        // Adding peer to sudoku players - to be notified -.
        String withPlayers = withPlayers(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withPlayers)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return false;
            }
            Set<PeerAddress> players = (Set<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
            players.add(dht.peer().peerAddress());	// Adds itself to the peers list for that game.
            dht.put(Number160.createHash(withPlayers)).data(new Data(players)).start().awaitUninterruptibly();
        }
        return true;
    }

    /**
     * Adds a player nickname to a game.
     * @param _game_name is the game name.
     * @return true if the player nickname has been added successfully, false otherwise.
     */
    private boolean addNickname(String _game_name, String _nickname) throws ClassNotFoundException, IOException {
        // Adding the nickname to the list of nicknames for the sudoku.
        String withNicknames = withNicknames(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withNicknames)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return false;
            }
            List<String> nicknames = (List<String>) futureGet.dataMap().values().iterator().next().object();
            // Checking if the nickname is already taken.
            if (nicknames.contains(_nickname)) {
                return false;
            }
            nicknames.add(_nickname);
            dht.put(Number160.createHash(withNicknames)).data(new Data(nicknames)).start().awaitUninterruptibly();
        }
        return true;
    }

    /**
     * Adds a player score to a game.
     * @param _game_name is the game name.
     * @return true if the player score has been added successfully, false otherwise.
     */
    private boolean addScore(String _game_name) throws IOException, ClassNotFoundException {
        // Adding a new score for the game.
        String withScores = withScores(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withScores)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (futureGet.isEmpty()) {
                return false;
            }
            List<Integer> scores = (List<Integer>) futureGet.dataMap().values().iterator().next().object();
            // Adding the starting score to the list of scores for that game.
            scores.add(STARTING_SCORE);
            dht.put(Number160.createHash(withScores)).data(new Data(scores)).start().awaitUninterruptibly();
        }
        return true;
    }

    /**
     * Removes a player to a game.
     * @param _game_name is the game name.
     * @return true if the player has been removed successfully, false otherwise.
     */
    private boolean removePlayer(String _game_name) throws IOException, ClassNotFoundException {
        // Removes itself from the peers playing the game.
        String withPlayers = withPlayers(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withPlayers)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (!futureGet.isEmpty()) {
                Set<PeerAddress> players = (Set<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
                players.remove(dht.peer().peerAddress());
                dht.put(Number160.createHash(withPlayers)).data(new Data(players)).start().awaitUninterruptibly();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a player nickname to a game.
     * @param _game_name is the game name.
     * @return true if the player nickname has been removed successfully, false otherwise.
     */
    private int removeNickname(String _game_name) throws IOException, ClassNotFoundException {
        String nickname = gamesNicknames.get(gamesNames.indexOf(_game_name));
        // Removes its nickname from the game.
        String withNicknames = withNicknames(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withNicknames)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (!futureGet.isEmpty()) {
                List<String> nicknames = (List<String>) futureGet.dataMap().values().iterator().next().object();
                int index = gamesNicknames.indexOf(nickname);
                nicknames.remove(index);
                dht.put(Number160.createHash(withNicknames)).data(new Data(nicknames)).start().awaitUninterruptibly();
                return index;
            }
        }
        return -1;
    }

    /**
     * Removes a player score to a game.
     * @param _game_name is the game name.
     * @return true if the player score has been removed successfully, false otherwise.
     */
    private boolean removeScore(String _game_name, int index) throws IOException, ClassNotFoundException {
        // Removes its score from the game.
        String withScores = withScores(_game_name);
        FutureGet futureGet = dht.get(Number160.createHash(withScores)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            if (!futureGet.isEmpty()) {
                List<Integer> scores = (List<Integer>) futureGet.dataMap().values().iterator().next().object();
                scores.remove(index);
                dht.put(Number160.createHash(withScores)).data(new Data(scores)).start().awaitUninterruptibly();
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes games information for the peer.
     */
    private void init() {
        games =  new ArrayList<>();
        gamesNames =  new ArrayList<>();
        gamesNicknames =  new ArrayList<>();
    }

    /**
     * Clear the peer information about a game.
     * @param index is the index for the game information.
     */
    private void clear(int index) {
        games.remove(index);
        gamesNames.remove(index);
        gamesNicknames.remove(index);
    }

    /**
     * The peer.
     */
    private Peer peer;

    /**
     * The DHT for storing all information.
     */
    private PeerDHT dht;

    /**
     * The Sudoku for the peer.
     */
    private Sudoku sudoku;

    /**
     * Games joined by the peer.
     */
    private List<Integer[][]> games;

    /**
     * Names of the games joined by the peer.
     */
    @Getter
    private List<String> gamesNames;

    /**
     * Nicknames used by the peer to join games.
     */
    private List<String> gamesNicknames;
}
