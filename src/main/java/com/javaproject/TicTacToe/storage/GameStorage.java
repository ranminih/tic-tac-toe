package com.javaproject.TicTacToe.storage;

import com.javaproject.TicTacToe.models.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {

    private static Map<String, Game> games;
    private static GameStorage instance;

    //create class singleton class to return only one instance of game storage
    private GameStorage() {
        games = new HashMap<>(); //map initialized 
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }
//method to set a game
    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }
    
}
