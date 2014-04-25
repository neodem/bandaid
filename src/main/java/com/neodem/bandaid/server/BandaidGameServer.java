package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemaster.PlayerCallback;
import com.neodem.bandaid.gamemaster.PlayerError;

import java.util.Map;

/**
 * to communicate with the Bandaid Game Server
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public interface BandaidGameServer {

    /**
     * connect to the game server
     *
     * @param playerName your name/identifier
     */
    void connect(String playerName) throws PlayerError;

    /**
     * ask the server for a list of available games
     *
     * @return a map of game descriptions and their identifiers
     */
    Map<String, String> getAvailableGames();

    /**
     * register for a game
     *
     * @param playerName
     * @param gameId     the game you want to register for
     * @return if you were registered or not
     */
    PlayerCallback registerForGame(String playerName, String gameId) throws PlayerError;

    /**
     * get the status of all running games
     *
     * @return
     */
    String getServerStatus();

    /**
     * get the staus of a given game
     *
     * @param gameId
     * @return
     */
    String getGameStatus(String gameId);
}
