package com.neodem.bandaid.gamemasterstuff;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/31/14
 */
public interface GameMaster {

    /**
     * this is called by the BandaidGameServer to initialize the game.
     * <p/>
     * this is where the GM needs to alert it's registered players of it's ID (so they can talk back)
     * and let them know the game is about to start..
     */
    public abstract void initGame();

    /**
     * this is called by the BandaidGameServer to start the game.
     * <p/>
     * note : this should not block. It should fire off a thread to run the game
     */
    void startGame();

    /**
     * this is called by the BandaidGameServer to determine if it should start the game.
     *
     * @return if the game is filled and ready to start
     */
    boolean isGameReadyToStart();

    String getGameDescription();

    /**
     * the BandaidGameServer will call this to register a player for the game
     *
     * @param playerName the name of the player
     * @return an interface the player can use to interact with the game
     */
    boolean registerPlayer(PlayerCallback playerName);

    /**
     * @return
     */
    GameStatus getGameStatus();
}
