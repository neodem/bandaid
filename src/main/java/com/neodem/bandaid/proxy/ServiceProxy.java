package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemaster.PlayerCallback;
import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.server.BandaidGameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public abstract class ServiceProxy extends ComBaseClient implements BandaidGameServer {

    private static final Logger log = LogManager.getLogger(ServiceProxy.class.getName());
    private final ServerMessageTranslator serverMessageTranslator;
    private final PlayerCallback player;

    public ServiceProxy(PlayerCallback target, String host, int port) {
        super(host, port);
        this.player = target;
        this.serverMessageTranslator = new JsonServerMessageTranslator();
    }

    @Override
    public void init() {
        super.init();
        // connect()
    }

    @Override
    public void handleMessage(int from, String serverMessage) {
        log.trace("{} : message received from {} : {}", player.getPlayerName(), from, serverMessage);

        ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(serverMessage);

        if (type == ServerMessageType.gameMessage || type == ServerMessageType.gameMessageNeedsReply) {
            String gameMessage = serverMessageTranslator.unmarshalGameMessage(serverMessage);

            if (type == ServerMessageType.gameMessageNeedsReply) {
                String reply = handleMessageWithReply(gameMessage);
                log.trace("{} : replying to server : {}", player.getPlayerName(), reply);
                send(ComServer.Server, reply);
            } else {
                handleAsynchonousMessage(gameMessage);
            }
        }
    }

    // TODO
    private String sendAndExpectReply(int destsination, String msg) {
        return null;
    }

    /**
     * for messages from the server to the client
     *
     * @param m
     * @return
     */
    public abstract String handleMessageWithReply(String m);

    /**
     * for messages from the server to the client
     *
     * @param m
     */
    public abstract void handleAsynchonousMessage(String m);

    @Override
    public void connect(int networkId, String name) throws PlayerError {
        String m = serverMessageTranslator.marshalServerConnect(networkId, name);
        String reply = sendAndExpectReply(ComServer.Server, m);
        serverMessageTranslator.checkReplyForPlayerError(reply);
    }

    @Override
    public Map<String, String> getAvailableGames() {
        String m = serverMessageTranslator.marshalServerGetAvailableGames();
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalAvailableGames(reply);
    }

    @Override
    public boolean registerForGame(int networkId, String gameId) throws PlayerError {
        String m = serverMessageTranslator.marshalRegisterForGameRequest(networkId, gameId);
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalServerReplyRegisterForGame(reply);
    }

    @Override
    public String getServerStatus() {
        String m = serverMessageTranslator.marshalServerServerStatus();
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalServerReplyServerStatus(reply);
    }

    @Override
    public String getGameStatus(String gameId) {
        String m = serverMessageTranslator.marshalServerGameStatus(gameId);
        String reply = sendAndExpectReply(ComServer.Server, m);
        return serverMessageTranslator.unmarshalServerReplyGameStatus(reply);
    }
}
