package com.neodem.bandaid.testGame;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.proxy.PlayerCallbackNetworkTransport;

/**
 * Created by vfumo on 4/27/14.
 */
public class SimpleGamePlayerCallbackNetworkTransport extends PlayerCallbackNetworkTransport {

    public SimpleGamePlayerCallbackNetworkTransport(PlayerCallback player, ServerMessageTranslator serverMessageTranslator) {
        super("localhost", player, serverMessageTranslator);
    }

    @Override
    protected void handleGameMessage(int from, String gameMessage) {

    }

    @Override
    protected String handleGameMessageWithReply(int from, String gameMessage) {
        return null;
    }
}
