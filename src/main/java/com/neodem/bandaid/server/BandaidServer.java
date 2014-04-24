package com.neodem.bandaid.server;

import com.google.common.collect.Maps;
import com.neodem.bandaid.gamemaster.GameMaster;
import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.network.ComInterface;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.PlayerProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * top level server!
 * <p/>
 * This handles all the server messages and passes them to and from the BandaidGameServer for processing
 * <p/>
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class BandaidServer implements BandaidGameServer {
    private static final Logger log = LogManager.getLogger(BandaidServer.class.getName());
    private ComServer comServer;
    /**
     * (gameId, gm)
     */
    private Map<String, GameMaster> gameMasters;
    /**
     * (netId, name)
     */
    private Map<Integer, String> connectedPlayers = Maps.newHashMap();
    /**
     * (netId, gameId)
     */
    private Map<Integer, String> playersInGames = Maps.newHashMap();

    public void start() {

        // start main communications system
        comServer.startComServer();
    }

    @Override
    public void connect(int networkId, String name) throws PlayerError {
        if (connectedPlayers.containsKey(networkId)) {
            String msg = "player already connected : " + networkId;
            throw new PlayerError(msg);
        }

        connectedPlayers.put(networkId, name);
    }

    @Override
    public Map<String, String> getAvailableGames() {
        Map<String, String> games = Maps.newHashMap();
        for (String gameId : gameMasters.keySet()) {
            games.put(gameId, gameMasters.get(gameId).getGameDescription());
        }

        return games;
    }

    @Override
    public boolean registerForGame(int networkId, String gameId, ComInterface comInterface) throws PlayerError {

        if (playersInGames.containsKey(networkId)) {
            String msg = "player already in game : " + playersInGames.get(networkId);
            throw new PlayerError(msg);
        }

        if (connectedPlayers.containsKey(networkId)) {
            String playerName = connectedPlayers.get(networkId);
            if (gameMasters.containsKey(gameId)) {
                GameMaster gm = gameMasters.get(gameId);
                PlayerProxy proxy = gm.makeNewProxy(playerName, networkId, comInterface);
                boolean result = gm.registerPlayer(networkId, proxy);

                if (gm.gameReady()) {
                    initAndStartGame(gm);
                }

                if (result == true) {
                    playersInGames.put(networkId, gameId);
                }

                return result;
            } else {
                String msg = "gameId does not exist : " + gameId;
                throw new PlayerError(msg);
            }
        } else {
            String msg = "This network ID is not connected : " + networkId;
            throw new PlayerError(msg);
        }
    }

    @Override
    public String getServerStatus() {
        StringBuffer b = new StringBuffer();
        b.append(connectedPlayers.size());
        b.append(" players connected.\n");
        b.append(gameMasters.size());
        b.append(" games available.\n");
        b.append("--------------------------\n");

        for (String gameId : gameMasters.keySet()) {
            GameMaster gm = gameMasters.get(gameId);
            b.append("game : ");
            b.append(gameId);
            b.append('\n');
            b.append(gm.getGameStatus());
            b.append('\n');
        }

        return b.toString();
    }

    @Override
    public String getGameStatus(String gameId) {
        GameMaster gm = gameMasters.get(gameId);
        if (gm != null) {
            return gm.getGameStatus();
        }

        return "This game is not registered";
    }

    private void initAndStartGame(GameMaster gm) {

        log.info("initializing Game");
        gm.initGame();

        log.info("Starting Game");
        gm.startGame();
    }

    public void setGameMasters(Map<String, GameMaster> gms) {
        this.gameMasters = gms;
    }

    public void addGameMaster(String key, GameMaster gameMaster) {
        if (gameMasters == null) {
            gameMasters = new HashMap<>();
        }
        gameMasters.put(key, gameMaster);
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }


}
