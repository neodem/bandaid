package com.neodem.bandaid.game;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public interface GameMasterCommunicationChannel<P extends Player> {

    /**
     * @param player
     * @return
     */
    public GameContext registerPlayerForNextGame(P player);
}
