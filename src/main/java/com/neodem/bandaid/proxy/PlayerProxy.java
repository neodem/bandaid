package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemaster.PlayerCallback;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.network.ComInterface;

/**
 * This class will communicate with it's partner on the client side
 * via messages
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class PlayerProxy implements PlayerCallback {
    protected final String playerName;
    protected final int networkId;
    private final ComInterface comInterface;
    private final ServerMessageTranslator serverMessageTranslator;

    public PlayerProxy(String playerName, int networkId, ComInterface server) {
        this.playerName = playerName;
        this.networkId = networkId;
        this.comInterface = server;
        this.serverMessageTranslator = new JsonServerMessageTranslator();
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerProxy)) return false;

        PlayerProxy that = (PlayerProxy) o;

        if (networkId != that.networkId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return networkId;
    }

    @Override
    public String toString() {
        return playerName;
    }

    protected void sendGameMessage(int dest, String gameMessage) {
        String m = serverMessageTranslator.marshalGameMessage(gameMessage);
        comInterface.sendMessage(dest, m);
    }

    protected String sendGameMessageExpectReply(int dest, String gameMessage) {
        String m = serverMessageTranslator.marshalGameMessageExpectsReply(gameMessage);
        String reply = comInterface.sendAndGetReply(dest, m);
        return serverMessageTranslator.getGameMessage(reply);
    }

}
