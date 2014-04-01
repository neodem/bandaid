package com.neodem.bandaid.server;

import com.neodem.bandaid.gameMaster.GameMaster;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.network.ComInterface;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.PlayerProxy;
import com.neodem.bandaid.proxy.PlayerProxyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public final class BandaidGameServer implements ComInterface {

    private static final Logger log = LogManager.getLogger(BandaidGameServer.class.getName());
    private final MessageHandler messageHandler = new MessageHandler("localhost", 6969, this);
    //
    // networked players registerd with this server. In the future They may or may not not be in a game
    private final Map<Integer, PlayerProxy> registeredPlayers = new HashMap<>();
    private GameMaster gameMaster;
    private ServerMessageTranslator smt = new JsonServerMessageTranslator();
    private PlayerProxyFactory playerProxyFactory;
    private ComServer comServer;

    public class MessageHandler extends ComBaseClient implements Runnable {

        private final BandaidGameServer server;
        private String mostRecentMessage = null;

        public MessageHandler(String host, int port, BandaidGameServer server) {
            super(host, port);
            this.server = server;
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(int from, String msg) {
            log.trace("Server : handle message : " + msg);
            ServerMessageType type = smt.unmarshalServerMessageTypeFromMessage(msg);
            if (type == ServerMessageType.register) {
                String playerName = smt.unmarshalPlayerNameFromMessage(msg);
                PlayerProxy proxy = playerProxyFactory.makeNewProxy(playerName, from, server);
                registeredPlayers.put(from, proxy);
                checkForGameStart();
            } else if (type == ServerMessageType.reply) {
                synchronized (this) {
                    mostRecentMessage = msg;
                    notify();
                }
            }
        }

        public void run() {
            init();
        }
    }

    // TODO
    private void checkForGameStart() {

        // note this code is temp. It starts the game when there are 4 players registerd.
        // in the future I'd like to enable players to register with the server and then
        // wait until a game is available for them. eg. the server will wait for 4 people,
        // put them into a game and fire it off and then wait for 4 more, etc.
        if (registeredPlayers.size() == 4) {
            startGame();
        }
    }

    public void start() {
        comServer.startComServer();
        startMessageHandler();
    }

    private void startMessageHandler() {
        Thread mt = new Thread(messageHandler);
        mt.setName("MessageHandler");
        mt.start();
    }

    @Override
    public void sendMessage(int dest, String msg) {
        messageHandler.send(dest, msg);
    }

    @Override
    public String sendAndGetReply(int dest, String msg) {
        messageHandler.send(dest, msg);

        synchronized (messageHandler) {
            try {
                messageHandler.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return messageHandler.getMostRecentMessage();
    }

    private void startGame() {
        log.info("initializing Game");
        gameMaster.initGame(new ArrayList(registeredPlayers.values()));

        log.info("Starting Game");
        gameMaster.startGame();
    }

    public void setGameMaster(GameMaster cgm) {
        this.gameMaster = cgm;
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }

    public void setPlayerProxyFactory(PlayerProxyFactory playerProxyFactory) {
        this.playerProxyFactory = playerProxyFactory;
    }
}
