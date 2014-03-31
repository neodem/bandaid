package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gameMaster.PlayerCallback;
import com.neodem.bandaid.messaging.MessageTranslator;
import com.neodem.bandaid.messaging.MessageType;
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
    private final MessageTranslator messageTranslator;
    private final PlayerCallback player;

    public ServiceProxy(PlayerCallback target, MessageTranslator messageTranslator, String host, int port) {
        super(host, port);
        this.player = target;
        this.messageTranslator = messageTranslator;
    }

    @Override
    public void init() {
        super.init();
        String m = messageTranslator.marshalRegistrationMesage(player.getPlayerName());
        log.debug("{} : registering with the server : {}", player.getPlayerName(), MessageType.reply);
        send(ComServer.Server, m);
    }

    @Override
    public void handleMessage(int from, String m) {
        log.trace("{} : message received from {} : {}", player.getPlayerName(), from, m);

        MessageType type = messageTranslator.unmarshalMessageTypeFromMessage(m);

        if (type.requiresReply()) {
            String reply = handleMessageWithReply(type, m);
            log.trace("{} : replying to server : {}", player.getPlayerName(), reply);
            send(ComServer.Server, reply);
        } else {
            handleAsynchonousMessage(type, m);
        }
    }

    public abstract String handleMessageWithReply(MessageType type, String m);

    public abstract void handleAsynchonousMessage(MessageType type, String m);
}
