package com.neodem.bandaid.testGame;

import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.proxy.PlayerCallbackProxy;

/**
 * Created by vfumo on 4/27/14.
 */
public class SimpleGamePlayerCallbackProxy extends PlayerCallbackProxy implements SimpleGamePlayerCallback {

    public SimpleGamePlayerCallbackProxy(int clientNetworkId, String playerName, ServerMessageTranslator serverMessageTranslator) {
        super(clientNetworkId, playerName, serverMessageTranslator);
    }

    @Override
    public String getPlayerName() {
        return null;
    }
}
