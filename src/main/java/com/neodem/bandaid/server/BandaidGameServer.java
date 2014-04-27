package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.gamemasterstuff.PlayerError;

import java.util.Map;

/**
 * to communicate with the Bandaid Game Server
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public interface BandaidGameServer {

    /**
     * connect to the game server
     *
     * @param player
     */
    void connect(PlayerCallback player) throws PlayerError;

    /**
     * ask the server for a list of available games
     *
     * @return a map of game descriptions and their identifiers <id, description>
     */
    Map<String, String> getAvailableGames();

    /**
     * register for a game
     *
     * @param player
     * @param gameId     the game you want to register for
     * @return if you were registered or not
     */
    boolean registerForGame(PlayerCallback player, String gameId) throws PlayerError;

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
