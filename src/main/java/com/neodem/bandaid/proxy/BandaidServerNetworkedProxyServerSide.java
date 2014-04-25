package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemaster.PlayerCallback;
import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.server.BandaidGameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * This is the network enabled proxy. It sits server side and communicates with clients
 * over the network and proxies the BandaidGameServer
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class BandaidServerNetworkedProxyServerSide extends ComBaseClient {

    private static final Logger log = LogManager.getLogger(BandaidServerNetworkedProxyServerSide.class.getName());
    private final ServerMessageTranslator serverMessageTranslator;
    private BandaidGameServer bandaidGameServer;

    private static final String OK_MESSSAGE = "{\"OK\"}";

    public BandaidServerNetworkedProxyServerSide() {
        super("localhost", 6969);
        this.serverMessageTranslator = new JsonServerMessageTranslator();
    }

    @Override
    protected String getClientName() {
        return "BandaidServerNetworkedProxyServerSide";
    }

    @Override
    public void handleMessage(int from, String msg) {
        log.trace("Server : handle message : " + msg);
        ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(msg);

        String replyMessage = null;

        String gameId;

        String playerName;

        switch (type) {
            case serverConnect:
                playerName = serverMessageTranslator.unmarshalConnectRequestName(msg);
                replyMessage = serverMessageTranslator.marshalServerConnectOkReply();
                try {
                    bandaidGameServer.connect(playerName);
                } catch (com.neodem.bandaid.gamemaster.PlayerError playerError) {
                    replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                }
                break;
            case getAvailableGames:
                Map<String, String> availableGames = bandaidGameServer.getAvailableGames();
                replyMessage = serverMessageTranslator.marshalGetAvailableGamesReply(availableGames);
                break;
            case registerForGame:
                PlayerCallback result;
                playerName = serverMessageTranslator.unmarshalRegisterForGameRequestName(msg);
                gameId = serverMessageTranslator.unmarshalRegisterForGameRequestGameId(msg);
                try {
                    result = bandaidGameServer.registerForGame(playerName, gameId);
                    replyMessage = serverMessageTranslator.marshalRegisterForGameReply(result);
                } catch (PlayerError playerError) {
                    replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                }
                break;
            case serverGameStatus:
                gameId = serverMessageTranslator.unmarshalServerGameStatusRequestGameId(msg);
                String gameStatus = bandaidGameServer.getGameStatus(gameId);
                replyMessage = serverMessageTranslator.marshalServerGameStatusReply(gameStatus);
                break;
            case serverStatus:
                String serverStatus = bandaidGameServer.getServerStatus();
                replyMessage = serverMessageTranslator.marshalGetServerStatusReply(serverStatus);
                break;
        }
        ;

        if (replyMessage != null) {
            send(from, replyMessage);
        }

    }

    public void setBandaidGameServer(BandaidGameServer bandaidGameServer) {
        this.bandaidGameServer = bandaidGameServer;
    }
}
