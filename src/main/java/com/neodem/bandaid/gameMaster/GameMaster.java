package com.neodem.bandaid.gamemaster;

import com.neodem.bandaid.proxy.PlayerProxyFactory;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/31/14
 */
public interface GameMaster extends PlayerProxyFactory {

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
     * @param networkId
     * @param player
     * @return if ok
     */
    boolean registerPlayer(int networkId, PlayerCallback player);

    String getGameStatus();
}
