package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import com.neodem.bandaid.gamemasterstuff.PlayerCallbackProxyFactory;
import com.neodem.bandaid.gamemasterstuff.PlayerError;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.BandaidGameServerNetworkTransport;
import com.neodem.bandaid.proxy.PlayerCallbackNetworkTransport;
import com.neodem.bandaid.proxy.PlayerCallbackProxy;
import com.neodem.bandaid.testGame.SimpleGameMaster;
import com.neodem.bandaid.testGame.SimpleGamePlayerCallback;
import com.neodem.bandaid.testGame.SimpleGamePlayerCallbackNetworkTransport;
import com.neodem.bandaid.testGame.SimpleGamePlayerCallbackProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class BandaidServerTest {


    private PlayerCallbackNetworkTransport clientProxy;
    private BandaidServer server;

    private PlayerCallback playerCallback;

    private ServerMessageTranslator serverMessageTranslator;

    @Before
    public void before() {
        serverMessageTranslator = new JsonServerMessageTranslator();

        BandaidGameServerImpl bandaidGameServer = new BandaidGameServerImpl();
        bandaidGameServer.addGameMaster("simpleGameMaster", new SimpleGameMaster());

        BandaidGameServerNetworkTransport bandaidGameServerNetworkTransport = new BandaidGameServerNetworkTransport();
        bandaidGameServerNetworkTransport.setBandaidGameServer(bandaidGameServer);

        PlayerCallbackProxyFactory proxyFactory = new PlayerCallbackProxyFactory() {
            @Override
            public PlayerCallbackProxy makeNewProxy(int clientNetworkId, String playerName) {
                return new SimpleGamePlayerCallbackProxy(clientNetworkId, playerName, serverMessageTranslator);
            }
        };
        bandaidGameServerNetworkTransport.setPlayerCallbackProxyFactory(proxyFactory);

        server = new BandaidServer();
        server.setBandaidGameServerNetworkTransport(bandaidGameServerNetworkTransport);
        server.setComServer(new ComServer());
        server.start();

        playerCallback = new SimpleGamePlayerCallback() {
            @Override
            public String getPlayerName() {
                return "name";
            }
        };

        clientProxy = new SimpleGamePlayerCallbackNetworkTransport(playerCallback, serverMessageTranslator);
        clientProxy.init();
    }

    @After
    public void after() {
        server.stop();
        server = null;

        clientProxy = null;
        playerCallback = null;
    }

    @Test
    public void testSomething() throws PlayerError {
        System.out.println(clientProxy.getServerStatus());
        clientProxy.connect(playerCallback);
        System.out.println(clientProxy.getServerStatus());

        Map<String, String> availableGames = clientProxy.getAvailableGames();

        String firstGameId = availableGames.keySet().iterator().next();
        System.out.println(clientProxy.getGameStatus(firstGameId));


        clientProxy.registerForGame(playerCallback, firstGameId);
    }

}
