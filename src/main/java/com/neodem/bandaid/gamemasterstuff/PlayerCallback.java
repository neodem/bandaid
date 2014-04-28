package com.neodem.bandaid.gamemasterstuff;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/31/14
 */
public interface PlayerCallback {

    /**
     * get the name of the player
     *
     * @return the name of the player (should never change)
     */
    public String getPlayerName();
}
