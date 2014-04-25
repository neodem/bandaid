package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.BandaidGameServerNetworkProxy;
import com.neodem.bandaid.proxy.BandaidGameServerNetworkedClientProxy;
import com.neodem.bandaid.testGame.SimpleGameMaster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class BandaidServerTest {


    private BandaidGameServerNetworkedClientProxy clientProxy;
    private BandaidServer server;

    @Before
    public void before() {

        BandaidGameServerImpl bandaidGameServer = new BandaidGameServerImpl();
        bandaidGameServer.addGameMaster("simpleGameMaster", new SimpleGameMaster());

        BandaidGameServerNetworkProxy bandaidGameServerNetworkProxy = new BandaidGameServerNetworkProxy();
        bandaidGameServerNetworkProxy.setBandaidGameServer(bandaidGameServer);

        server = new BandaidServer();
        server.setBandaidGameServerNetworkProxy(bandaidGameServerNetworkProxy);
        server.setComServer(new ComServer());
        server.start();

        clientProxy = new BandaidGameServerNetworkedClientProxy();
        clientProxy.init();
    }

    @After
    public void after() {
        server.stop();
        server = null;

        clientProxy = null;
    }

    @Test
    public void testSomething() throws PlayerError {
        System.out.println(clientProxy.getServerStatus());
        clientProxy.connect("testName");
        System.out.println(clientProxy.getServerStatus());

        Map<String, String> availableGames = clientProxy.getAvailableGames();

        String firstGameId = availableGames.keySet().iterator().next();
        System.out.println(clientProxy.getGameStatus(firstGameId));


        clientProxy.registerForGame("testName", firstGameId);

    }

}
