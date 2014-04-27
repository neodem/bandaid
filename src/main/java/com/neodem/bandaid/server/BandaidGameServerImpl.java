package com.neodem.bandaid.server;

import com.google.common.collect.Maps;
import com.neodem.bandaid.gamemasterstuff.GameMaster;
import com.neodem.bandaid.gamemasterstuff.PlayerError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * handles managing connections from players and putting them into games and starting the games
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class BandaidGameServerImpl implements BandaidGameServer {
    private static final Logger log = LogManager.getLogger(BandaidGameServerImpl.class.getName());
    /**
     * (gameId, gm)
     */
    private Map<String, GameMaster> gameMasters;
    /**
     * (netId, name)
     */
    private Collection<String> connectedPlayers = new HashSet<>();
    /**
     * (playerName, gameId)
     */
    private Map<String, String> playersInGames = new HashMap<>();
    private Thread gameStartCheckerThread;

    /**
     * will start full games
     */
    private class GameStartChecker implements Runnable {
        public void run() {
            Thread thisThread = Thread.currentThread();
            while (gameStartCheckerThread == thisThread) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }

                for (String gameId : gameMasters.keySet()) {
                    GameMaster gm = gameMasters.get(gameId);
                    if (gm.isGameReadyToStart()) {
                        log.info("initializing Game {}", gameId);
                        gm.initGame();

                        log.info("Starting Game {}", gameId);
                        gm.startGame();
                    }
                }
            }
        }
    }

    public BandaidGameServerImpl() {
        startGameStartChecker();
    }

    @Override
    public void connect(String playerName) throws PlayerError {
        if (connectedPlayers.contains(playerName)) {
            String msg = "player already connected (try another name) : " + playerName;
            throw new PlayerError(msg);
        }

        connectedPlayers.add(playerName);
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
    public boolean registerForGame(String playerName, String gameId) throws PlayerError {

        if (playersInGames.containsKey(playerName)) {
            String msg = "player already in game : " + playersInGames.get(playerName);
            throw new PlayerError(msg);
        }

        if (connectedPlayers.contains(playerName)) {
            if (gameMasters.containsKey(gameId)) {
                GameMaster gm = gameMasters.get(gameId);

                boolean result = gm.registerPlayer(playerName);
                if (result == true) {
                    playersInGames.put(playerName, gameId);
                }

                return result;
            } else {
                String msg = "gameId does not exist : " + gameId;
                throw new PlayerError(msg);
            }
        } else {
            String msg = "This player is not connected : " + playerName;
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
            b.append(" : ");
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

    private void startGameStartChecker() {
        gameStartCheckerThread = new Thread(new GameStartChecker());
        gameStartCheckerThread.setName("BandaidGameServerImpl-gameStartChecker");
        gameStartCheckerThread.start();
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
}
