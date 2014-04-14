package com.neodem.bandaid.gamemaster;

import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.proxy.PlayerProxy;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/31/14
 */
public abstract class BaseGameMaster implements GameMaster, Runnable {

    private Thread gameThread;

    public class MessageHandler extends ComBaseClient implements Runnable {

        private String mostRecentMessage = null;

        public MessageHandler(String host, int port) {
            super(host, port);
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(int from, String msg) {
            getLog().trace("GameMaster : handle message : " + msg);
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

    protected abstract Logger getLog();

    /**
     * this is the main game loop. It gets run on a thread and once it's over
     * the thread/game dies
     */
    protected abstract void runGame();

    protected String getGameThreadName() {
        return "GameMaster";
    }

    public abstract void initGame();

    public final void startGame() {
        gameThread = new Thread(this);
        gameThread.setName(getGameThreadName());
        gameThread.start();
    }

    public final void run() {
        runGame();
    }
}
