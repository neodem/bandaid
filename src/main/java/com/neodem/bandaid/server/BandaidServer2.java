package com.neodem.bandaid.server;

import com.neodem.bandaid.network.ComInterface;

/**
 * top level server!
 * <p/>
 * This handles all the server messages and passes them to and from the BandaidGameServer for processing
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public final class BandaidServer2 implements ComInterface {

    //    private static final Logger log = LogManager.getLogger(BandaidServer2.class.getName());
//    private MessageProcesser messageHandler;
//    private ServerMessageTranslator serverMessageTranslator;
//    private ComServer comServer;
//    private BandaidGameServer bandaidGameServer;
//
//    public class MessageProcesser extends ComBaseClient implements Runnable {
//
//        private ComInterface comInterface;
//        private String mostRecentMessage = null;
//
//        public MessageProcesser(String host, int port, ComInterface comInterface) {
//            super(host, port);
//            this.comInterface = comInterface;
//        }
//
//        public String getMostRecentMessage() {
//            return mostRecentMessage;
//        }
//
//        @Override
//        protected void handleMessage(int from, String msg) {
//            log.trace("Server : handle message : " + msg);
//            ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(msg);
//
//            String replyMessage = null;
//            String gameId = serverMessageTranslator.unmarshalGameId(msg);
//
//            switch (type) {
//                case serverConnect:
//                    String name = serverMessageTranslator.unmarshalConnectRequestName(msg);
//                    try {
//                        bandaidGameServer.connect(from, name);
//                    } catch (com.neodem.bandaid.gamemaster.PlayerError playerError) {
//                        replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
//                    }
//                    break;
//                case getAvailableGames:
//                    Map<String, String> availableGames = bandaidGameServer.getAvailableGames();
//                    replyMessage = serverMessageTranslator.marshalGetAvailableGamesReply(availableGames);
//                    break;
//                case registerForGame:
//                    boolean result;
//                    try {
//                        result = bandaidGameServer.registerForGame(from, gameId, comInterface);
//                        replyMessage = serverMessageTranslator.marshalRegisterForGameReply(result);
//                    } catch (PlayerError playerError) {
//                        replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
//                    }
//                    break;
//                case serverGameStatus:
//                    String gameStatus = bandaidGameServer.getGameStatus(gameId);
//                    replyMessage = serverMessageTranslator.marshalServerGameStatusReply(gameStatus);
//                    break;
//                case serverStatus:
//                    String serverStatus = bandaidGameServer.getServerStatus();
//                    replyMessage = serverMessageTranslator.marshalGetServerStatusReply(serverStatus);
//                    break;
//                case reply:
//                    synchronized (this) {
//                        mostRecentMessage = msg;
//                        notify();
//                    }
//                    break;
//            }
//            ;
//
//            if (replyMessage != null) {
//                send(from, replyMessage);
//            }
//        }
//
//        public void run() {
//            init();
//        }
//    }
//
//    @Override
    public void sendMessage(int dest, String msg) {
//        messageHandler.send(dest, msg);
    }

    //
//    @Override
    public String sendAndGetReply(int dest, String msg) {
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
        return null;
    }
//
//    public void start() {
//
//        // start main communications system
//        comServer.startComServer();
//
//        // set up our message processor
//        messageHandler = new MessageProcesser("localhost", 6969, this);
//        Thread mt = new Thread(messageHandler);
//        mt.setName("BandaidServer2-MessageProcessor");
//        mt.start();
//    }
//
//    public void setServerMessageTranslator(ServerMessageTranslator serverMessageTranslator) {
//        this.serverMessageTranslator = serverMessageTranslator;
//    }
//
//    public void setBandaidGameServer(BandaidGameServer bandaidGameServer) {
//        this.bandaidGameServer = bandaidGameServer;
//    }
//
//    public void setComServer(ComServer comServer) {
//        this.comServer = comServer;
//    }
}
