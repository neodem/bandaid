package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemaster.GameMaster;
import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.gamemaster.SimpleGameMaster;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.BandaidServerNetworkedProxyClientside;
import com.neodem.bandaid.proxy.BandaidServerNetworkedProxyServerSide;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class BandaidGameServerTest {

    private DefaultBandaidGameServer server;

    private BandaidServerNetworkedProxyServerSide serviceProxy;
    private BandaidServerNetworkedProxyClientside clientProxy;


    private ServerMessageTranslator serverMessageTranslator;
    private ComServer comServer;
    private BandaidGameServer mockBandaidGameServer;

    private GameMaster gameMaster;

    @Before
    public void before() {

        gameMaster = new SimpleGameMaster();

        comServer = new ComServer();
        comServer.startComServer();

        server = new DefaultBandaidGameServer();
        server.addGameMaster("simpleGameMaster", gameMaster);

        serviceProxy = new BandaidServerNetworkedProxyServerSide();
        serviceProxy.setBandaidGameServer(server);
        serviceProxy.init();

        clientProxy = new BandaidServerNetworkedProxyClientside();
        clientProxy.init();
    }

    @After
    public void after() {
        serviceProxy = null;

        server = null;
        serverMessageTranslator = null;
        comServer = null;
        mockBandaidGameServer = null;
    }

    @Test
    public void testSomething() throws PlayerError {
        System.out.println(clientProxy.getServerStatus());
        clientProxy.connect("testName");
        System.out.println(clientProxy.getServerStatus());

        Map<String, String> availableGames = clientProxy.getAvailableGames();

        clientProxy.registerForGame("testName", availableGames.keySet().iterator().next());

    }

}
