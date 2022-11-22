package com.javaproject.TicTacToe.services;

import com.javaproject.TicTacToe.exceptions.InvalidGameException;
import com.javaproject.TicTacToe.exceptions.InvalidParamException;
import com.javaproject.TicTacToe.exceptions.NotFoundException;
import com.javaproject.TicTacToe.models.Player;
import com.javaproject.TicTacToe.models.Game;
import com.javaproject.TicTacToe.models.GamePlay;
import com.javaproject.TicTacToe.models.TicToe;
import com.javaproject.TicTacToe.storage.GameStorage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import static com.javaproject.TicTacToe.models.GameStatus.*;

@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(START);
        GameStorage.getInstance().setGame(game); // create game storage
        return game;
    }

    // Algorithm to enable AI win
    /**
     * @param board
     * @param depth
     * @param isMax
     * @return
     */
    public static int markForAi(int[][] board, int depth, boolean isMax) {
        // Maximising player, find the maximum attainable value.
        if (isMax) {
            int highestVal = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (isTileMarked(board, row, col)) {
                        board[row][col] = 1;
                        highestVal = Math.max(highestVal, markForAi(board,
                                depth - 1, false));
                        board[row][col] = 1;
                    }
                }
            }
            return highestVal;
            // Minimising player, find the minimum attainable value;
        } else {
            int lowestVal = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (!isTileMarked(board, row, col)) {
                        lowestVal = Math.min(lowestVal, markForAi(board,
                                depth - 1, true));
                    }
                }
            }
            return lowestVal;
        }

    }

    // method to connect the game using gameId
    public Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Invalid Input");
        }
        // get game with gameId
        Game game = GameStorage.getInstance().getGames().get(gameId);

        // If the game already contains two players, the game is invalid for another
        // player to enter.
        if (game.getPlayer2() != null) {
            throw new InvalidGameException("Invalid Input");
        }

        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    // method to connect player to a random game on the webapp
    public Game connectToRandomGame(Player player2) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(START))
                .findFirst().orElseThrow(() -> new NotFoundException("Game not found"));
        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    // method to
    public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(ENDED)) { // if game has ended, throw exception
            throw new InvalidGameException("Game ENDED.");
        }
        // get board and get X and Y coordinate
        int[][] board = game.getBoard();
        // marking for player with 2
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = 2;

        // determining the winner
        Boolean xWinner = checkWinner(game.getBoard(), TicToe.X);
        Boolean oWinner = checkWinner(game.getBoard(), TicToe.O);

        if (xWinner) {
            game.setWinner(TicToe.X);
        } else if (oWinner) {
            game.setWinner(TicToe.O);
        }

        GameStorage.getInstance().setGame(game);
        return game;
    }

    private Boolean checkWinner(int[][] board, TicToe ticToe) {
        int[] boardArray = new int[9]; // 9 elements on the board, create 1D array and insert within 2D array
        int counterIndex = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[counterIndex] = board[i][j];
                counterIndex++;
            }
        }
        // win combinations within game logic
        int[][] winCombinations = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 } };
        for (int i = 0; i < winCombinations.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                if (boardArray[winCombinations[i][j]] == ticToe.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        if (ticToe.getValue() == 2 && anyMovesAvailable(board)) {
            markForAi(board, getDepth(board), true);
        }
        return false;
    }

    public static boolean isTileMarked(int[][] board, int x, int y) throws IndexOutOfBoundsException {
        return board[x][y] != 0;
    }

    public boolean anyMovesAvailable(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!isTileMarked(board, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getDepth(int[][] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isTileMarked(board, i, j)) {
                    count++;
                }
            }
        }
        return count % 3;
    }
}
