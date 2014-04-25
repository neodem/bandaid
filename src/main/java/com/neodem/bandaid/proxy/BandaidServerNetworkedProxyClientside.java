package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.server.BandaidGameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Use this to communicate with the BandaidGameServer over a network. Simply call connect() to
 * establish the connection and call the methods as you normally would.
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class BandaidServerNetworkedProxyClientside implements BandaidGameServer {

    private static final Logger log = LogManager.getLogger(BandaidServerNetworkedProxyClientside.class.getName());
    private final ServerMessageTranslator serverMessageTranslator;
    private final MessageHandler messageHandler = new MessageHandler("localhost", 6969);
    private Thread messageHandlerThread = null;

    public class MessageHandler extends ComBaseClient implements Runnable {

        private String mostRecentMessage = null;

        @Override
        protected String getClientName() {
            return "BandaidServerNetworkedProxyClientside";
        }

        public MessageHandler(String host, int port) {
            super(host, port);
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(int from, String msg) {
            // todo maybe a check to see if it's a reply?
            synchronized (this) {
                mostRecentMessage = msg;
                notify();
            }
        }

        public void run() {
            init();
        }
    }

    public BandaidServerNetworkedProxyClientside() {
        this.serverMessageTranslator = new JsonServerMessageTranslator();
    }

    public void init() {
        messageHandlerThread = new Thread(messageHandler);
        messageHandlerThread.setName("BandaidServerNetworkedProxyClientside-MessageHandler");
        messageHandlerThread.start();
    }

    private String sendAndExpectReply(int dest, String msg) {
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

    @Override
    public void connect(String playerName) throws PlayerError {
        String m = serverMessageTranslator.marshalConnectRequest(playerName);
        String reply = sendAndExpectReply(ComServer.Server, m);
        serverMessageTranslator.checkReplyForPlayerError(reply);
    }

    @Override
    public Map<String, String> getAvailableGames() {
        String m = serverMessageTranslator.marshalGetAvailableGamesRequest();
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalGetAvailableGamesReply(reply);
    }

    @Override
    public boolean registerForGame(String playerName, String gameId) throws PlayerError {
        String m = serverMessageTranslator.marshalRegisterForGameRequest(playerName, gameId);
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalRegisterForGameReply(reply);
    }

    @Override
    public String getServerStatus() {
        String m = serverMessageTranslator.marshalGetServerStatusRequest();
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalGetServerStatusReply(reply);
    }

    @Override
    public String getGameStatus(String gameId) {
        String m = serverMessageTranslator.marshalGetGameStatusRequest(gameId);
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalGetGameStatusReply(reply);
    }
}
