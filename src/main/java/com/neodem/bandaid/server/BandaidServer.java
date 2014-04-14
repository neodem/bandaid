package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.PlayerProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public class BandaidServer {

    private static final Logger log = LogManager.getLogger(BandaidServer.class.getName());

    private final MessageHandler messageHandler = new MessageHandler("localhost", 6969, this);
    private ServerMessageTranslator smt = new JsonServerMessageTranslator();
    private ComServer comServer;

    private BandaidGameServer bandaidGameServer;

    public void start() {
        comServer.startComServer();
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }

    public class MessageHandler extends ComBaseClient implements Runnable {

        private final BandaidGameServerImpl server;
        private String mostRecentMessage = null;

        public MessageHandler(String host, int port, BandaidGameServerImpl server) {
            super(host, port);
            this.server = server;
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        // serverConnect, getAvailableGames, registerForGame, serverGameStatus,  serverStatus

        @Override
        protected void handleMessage(int from, String msg) {
            log.trace("Server : handle message : " + msg);
            ServerMessageType type = smt.unmarshalServerMessageTypeFromMessage(msg);

            switch (type) {
                case serverConnect:
                    Integer netId = smt.unmarshalServerConnectNetId(msg);
                    String name = smt.unmarshalServerConnectName(msg);
                    try {
                        bandaidGameServer.connect(netId, name);
                    } catch (PlayerError playerError) {
                        playerError.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    break;


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

    private void startMessageHandler() {
        Thread mt = new Thread(messageHandler);
        mt.setName("MessageHandler");
        mt.start();
    }

//    @Override
//    public void sendMessage(int dest, String msg) {
//        messageHandler.send(dest, msg);
//    }
//
//    @Override
//    public String sendAndGetReply(int dest, String msg) {
//        messageHandler.send(dest, msg);
//
//        synchronized (messageHandler) {
//            try {
//                messageHandler.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return messageHandler.getMostRecentMessage();
//    }

}
