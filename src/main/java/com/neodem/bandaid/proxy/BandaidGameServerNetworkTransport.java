package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.gamemasterstuff.PlayerCallbackProxyFactory;
import com.neodem.bandaid.gamemasterstuff.PlayerError;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.NetworkEntityType;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComClient;
import com.neodem.bandaid.server.BandaidGameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the network enabled proxy. It sits server side and communicates with clients
 * over the network and proxies the BandaidGameServer. There should be only one running at once.
 * <p/>
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/27/14
 */
public final class BandaidGameServerNetworkTransport extends ComClient {

    private static final Logger log = LogManager.getLogger(BandaidGameServerNetworkTransport.class.getName());
    private final ServerMessageTranslator serverMessageTranslator;

    private BandaidGameServer bandaidGameServer;

    private PlayerCallbackProxyFactory playerCallbackProxyFactory;

    private Map<Integer, PlayerCallback> players = new HashMap<>();

    public BandaidGameServerNetworkTransport() {
        super("localhost", 6969);
        this.serverMessageTranslator = new JsonServerMessageTranslator();
    }

    @Override
    protected String getClientName() {
        return "BandaidGameServerNetworkTransport";
    }

    @Override
    public synchronized void handleMessage(int from, String msg) {
        log.trace("Server : handle message : " + msg);
        ServerMessageType serverMessageType = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(msg);

        String replyMessage = null;

        String gameId;

        switch (serverMessageType) {
            case hello:
                if (!players.containsKey(from)) {
                    NetworkEntityType networkEntityType = serverMessageTranslator.unmarshalNetworkEntityType(msg);
                    if (networkEntityType == NetworkEntityType.playerCallbackNetworkTransport) {

                        log.info("A PlayerCallbackNetworkTransport is up at networkId={} and needs a Server Side Proxy.", from);

                        String playerName = serverMessageTranslator.unmarshalPlayerName(msg);

                        // a new client side transport is up. We need a server side partner for it
                        log.debug("Constructing a PlayerCallbackProxy");
                        PlayerCallbackProxy pc = playerCallbackProxyFactory.makeNewProxy(from, playerName);

                        log.debug("Associating the PlayerCallbackProxy to networkId {}", from);
                        players.put(from, pc);
                    }
                }
                break;
            case connect:
                try {
                    bandaidGameServer.connect(players.get(from));
                    replyMessage = serverMessageTranslator.marshalServerConnectOkReply();
                } catch (PlayerError playerError) {
                    replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                }
                break;
            case getAvailableGames:
                Map<String, String> availableGames = bandaidGameServer.getAvailableGames();
                replyMessage = serverMessageTranslator.marshalGetAvailableGamesReply(availableGames);
                break;
            case registerForGame:
                gameId = serverMessageTranslator.unmarshalRegisterForGameRequestGameId(msg);
                try {
                    boolean result = bandaidGameServer.registerForGame(players.get(from), gameId);
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

    public void setPlayerCallbackProxyFactory(PlayerCallbackProxyFactory playerCallbackProxyFactory) {
        this.playerCallbackProxyFactory = playerCallbackProxyFactory;
    }
}
