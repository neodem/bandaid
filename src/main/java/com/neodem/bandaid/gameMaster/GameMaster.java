package com.neodem.bandaid.gamemaster;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/31/14
 */
public interface GameMaster {

    /**
     * this is where the GM needs to alert it's registered players of it's ID (so they can talk back)
     * and let them know the game is about to start..
     */
    public abstract void initGame();

    /**
     * this should not block. It should fire off a thread to run the game
     */
    void startGame();

    /**
     * @return if the game is filled and ready to start
     */
    boolean gameReady();

    String getGameDescription();

    /**
     * register a player for the game
     *
     * @param playerName the name of the player
     * @return an interface the player can use to interact with the game
     */
    boolean registerPlayer(String playerName);

    String getGameStatus();
}
