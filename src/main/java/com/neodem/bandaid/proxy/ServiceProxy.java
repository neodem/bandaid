package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemaster.PlayerCallback;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.network.ComServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public abstract class ServiceProxy extends ComBaseClient {

    private static final Logger log = LogManager.getLogger(ServiceProxy.class.getName());
    private final ServerMessageTranslator messageTranslator;
    private final PlayerCallback player;

    public ServiceProxy(PlayerCallback target, String host, int port) {
        super(host, port);
        this.player = target;
        this.messageTranslator = new JsonServerMessageTranslator();
    }

    @Override
    public void init() {
        super.init();
        String m = messageTranslator.marshalRegistrationMesage(player.getPlayerName());
        log.debug("{} : registering with the server : {}", player.getPlayerName(), m);
        send(ComServer.Server, m);
    }

    @Override
    public void handleMessage(int from, String serverMessage) {
        log.trace("{} : message received from {} : {}", player.getPlayerName(), from, serverMessage);

        ServerMessageType type = messageTranslator.unmarshalServerMessageTypeFromMessage(serverMessage);

        if (type == ServerMessageType.gameMessage || type == ServerMessageType.gameMessageNeedsReply) {
            String gameMessage = messageTranslator.getGameMessage(serverMessage);

            if (type == ServerMessageType.gameMessageNeedsReply) {
                String reply = handleMessageWithReply(gameMessage);
                log.trace("{} : replying to server : {}", player.getPlayerName(), reply);
                send(ComServer.Server, reply);
            } else {
                handleAsynchonousMessage(gameMessage);
            }
        }
    }

    public abstract String handleMessageWithReply(String m);

    public abstract void handleAsynchonousMessage(String m);
}