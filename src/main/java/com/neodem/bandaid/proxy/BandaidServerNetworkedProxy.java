package com.neodem.bandaid.proxy;

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
public abstract class BandaidServerNetworkedProxy extends ComBaseClient implements BandaidGameServer {

    private static final Logger log = LogManager.getLogger(BandaidServerNetworkedProxy.class.getName());
    private final ServerMessageTranslator serverMessageTranslator;

    public BandaidServerNetworkedProxy() {
        super("localhost", 6969);
        this.serverMessageTranslator = new JsonServerMessageTranslator();
    }

    @Override
    public void init() {
        super.init();
        // connect()
    }

    @Override
    public void handleMessage(int from, String msg) {
        log.trace("Server : handle message : " + msg);
        ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(msg);

        String replyMessage = null;
        String gameId = serverMessageTranslator.unmarshalGameId(msg);

        switch (type) {
            case serverConnect:
                String name = serverMessageTranslator.unmarshalServerConnectName(msg);
                try {
                    bandaidGameServer.connect(from, name);
                } catch (com.neodem.bandaid.gamemaster.PlayerError playerError) {
                    replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                }
                break;
            case getAvailableGames:
                Map<String, String> availableGames = bandaidGameServer.getAvailableGames();
                replyMessage = serverMessageTranslator.marshalAvailableGames(availableGames);
                break;
            case registerForGame:
                boolean result;
                try {
                    result = bandaidGameServer.registerForGame(from, gameId, comInterface);
                    replyMessage = serverMessageTranslator.marshalRegisterForGameReply(result);
                } catch (PlayerError playerError) {
                    replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                }
                break;
            case serverGameStatus:
                String gameStatus = bandaidGameServer.getGameStatus(gameId);
                replyMessage = serverMessageTranslator.marshalGameStatus(gameStatus);
                break;
            case serverStatus:
                String serverStatus = bandaidGameServer.getServerStatus();
                replyMessage = serverMessageTranslator.marshalServerStatus(serverStatus);
                break;
            case reply:
                synchronized (this) {
                    mostRecentMessage = msg;
                    notify();
                }
                break;
        }
        ;

        if (replyMessage != null) {
            send(from, replyMessage);
        }




//        log.trace("message received from {} : {}", from, serverMessage);
//
//        ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(serverMessage);
//
//        if (type == ServerMessageType.gameMessage || type == ServerMessageType.gameMessageNeedsReply) {
//            String gameMessage = serverMessageTranslator.unmarshalGameMessage(serverMessage);
//
//            if (type == ServerMessageType.gameMessageNeedsReply) {
//                String reply = handleMessageWithReply(gameMessage);
//                log.trace("replying to server : {}", reply);
//                send(ComServer.Server, reply);
//            } else {
//                handleAsynchonousMessage(gameMessage);
//            }
//        }
    }

    // TODO
    private String sendAndExpectReply(int destsination, String msg) {
        return null;
    }

//    /**
//     * for messages from the server to the client
//     *
//     * @param m
//     * @return
//     */
//    public abstract String handleMessageWithReply(String m);
//
//    /**
//     * for messages from the server to the client
//     *
//     * @param m
//     */
//    public abstract void handleAsynchonousMessage(String m);

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
        String m = serverMessageTranslator.marshalServerRequestGetServerStatus();
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
