package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.gamemasterstuff.PlayerError;
import com.neodem.bandaid.messaging.NetworkEntityType;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComClient;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.server.BandaidGameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Use this to communicate with the BandaidGameServer over a network. Simply call connect() to
 * establish the connection and call the methods as you normally would. This sits client side
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public abstract class PlayerCallbackNetworkTransport implements BandaidGameServer {

    private static final Logger log = LogManager.getLogger(PlayerCallbackNetworkTransport.class.getName());
    private final ServerMessageTranslator serverMessageTranslator;
    private final MessageHandler messageHandler = new MessageHandler("localhost", 6969);
    private final PlayerCallback player;
    private Thread messageHandlerThread = null;

    public PlayerCallbackNetworkTransport(PlayerCallback player, ServerMessageTranslator serverMessageTranslator) {
        this.player = player;
        this.serverMessageTranslator = serverMessageTranslator;
    }

    private class MessageHandler extends ComClient implements Runnable {

        private String mostRecentMessage = null;

        @Override
        protected String getClientName() {
            return "PlayerCallbackNetworkTransport";
        }

        public MessageHandler(String host, int port) {
            super(host, port);
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(int from, String msg) {
            ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(msg);
            if (type == ServerMessageType.gameMessage || type == ServerMessageType.gameMessageNeedsReply) {
                String gameMessage = serverMessageTranslator.unmarshalGameMessage(msg);

                if (type == ServerMessageType.gameMessage) {
                    handleGameMessage(from, gameMessage);
                } else {
                    String reply = handleGameMessageWithReply(from, gameMessage);
                    String replyMessage = serverMessageTranslator.marshalGameMessageReply(reply);
                    send(from, replyMessage);
                }
            }

            if (serverMessageTranslator.isReply(msg)) {
                synchronized (this) {
                    mostRecentMessage = msg;
                    notify();
                }
            }
        }

        public void run() {
            init();
            broadcast(serverMessageTranslator.marshalHello(NetworkEntityType.playerCallbackNetworkTransport, player.getPlayerName()));
            log.info("Network Link Established");
        }
    }

    protected abstract void handleGameMessage(int from, String gameMessage);

    protected abstract String handleGameMessageWithReply(int from, String gameMessage);

    public void init() {
        messageHandlerThread = new Thread(messageHandler);
        messageHandlerThread.setName("PlayerCallbackNetworkTransport-MessageHandler");
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
    public void connect(PlayerCallback player) throws PlayerError {
        String m = serverMessageTranslator.marshalConnectRequest();
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
    public boolean registerForGame(PlayerCallback player, String gameId) throws PlayerError {
        String m = serverMessageTranslator.marshalRegisterForGameRequest(gameId);
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
