package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.messaging.ServerMessageTranslator;

/**
 * Created by vfumo on 4/27/14.
 */
public abstract class PlayerCallbackProxy implements PlayerCallback {
    private ServerMessageTranslator serverMessageTranslator;

    // the id of our partner on the client side of the network
    protected int clientNetworkId;

    public PlayerCallbackProxy(int clientNetworkId, ServerMessageTranslator serverMessageTranslator) {
        this.serverMessageTranslator = serverMessageTranslator;
        this.clientNetworkId = clientNetworkId;
    }

    @Override
    public String getPlayerName() {
        String m = serverMessageTranslator.marshalGetPlayerNameRequest();
        String reply = sendGameMessageExpectReply(clientNetworkId, m);
        return serverMessageTranslator.unmarshalGetPlayerNameReply(reply);
    }

    protected String sendGameMessageExpectReply(int to, String m) {
        return null;
    }

    protected void sendGameMessage(int to, String m) {
    }

}
