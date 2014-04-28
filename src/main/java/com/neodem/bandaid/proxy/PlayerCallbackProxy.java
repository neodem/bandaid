package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.messaging.NetworkEntityType;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.network.ComClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vfumo on 4/27/14.
 */
public abstract class PlayerCallbackProxy implements PlayerCallback {
    private static final Logger log = LogManager.getLogger(PlayerCallbackProxy.class.getName());
    private final String playerName;

    private ServerMessageTranslator serverMessageTranslator;

    // the id of our partner on the client side of the network
    protected int clientNetworkId;

    private Thread messageHandlerThread = null;

    private final MessageHandler messageHandler = new MessageHandler("localhost", 6969);


    public PlayerCallbackProxy(int clientNetworkId, String playerName, ServerMessageTranslator serverMessageTranslator) {
        this.serverMessageTranslator = serverMessageTranslator;
        this.clientNetworkId = clientNetworkId;
        this.playerName = playerName;

        log.debug("firing off MessageHandler thread.");

        messageHandlerThread = new Thread(messageHandler);
        messageHandlerThread.setName("PlayerCallbackProxy-MessageHandler");
        messageHandlerThread.start();

        log.debug("alerting our NetworkTransport compatriot at {} that we are here", clientNetworkId);
        messageHandler.send(clientNetworkId, serverMessageTranslator.marshalHello(NetworkEntityType.playerCallbackProxy, playerName));
    }

    private class MessageHandler extends ComClient implements Runnable {

        private String mostRecentMessage = null;

        @Override
        protected String getClientName() {
            return "PlayerCallbackProxy";
        }

        public MessageHandler(String host, int port) {
            super(host, port);
        }

        public String getMostRecentMessage() {
            return mostRecentMessage;
        }

        @Override
        protected void handleMessage(int from, String msg) {

            if (serverMessageTranslator.isReply(msg)) {
                synchronized (this) {
                    mostRecentMessage = msg;
                    notify();
                }
            }
        }

        public void run() {
            init();
            log.info("Network Link Established");
        }
    }

    protected String sendAndExpectReply(int dest, String msg) {
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
    public String getPlayerName() {
        return playerName;
    }

    protected String sendGameMessageExpectReply(int to, String m) {
        String msg = serverMessageTranslator.marshalGameMessage(m);
        return sendAndExpectReply(to, msg);
    }

    protected void sendGameMessage(int to, String m) {
        String msg = serverMessageTranslator.marshalGameMessage(m);
        messageHandler.send(to, msg);
    }

}
