package com.neodem.bandaid.gamemaster;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
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
