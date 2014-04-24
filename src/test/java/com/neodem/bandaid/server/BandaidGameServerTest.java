package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemaster.GameMaster;
import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.gamemaster.SimpleGameMaster;
import com.neodem.bandaid.messaging.JsonServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.network.ComInterface;
import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.BandaidServerNetworkedProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class BandaidGameServerTest {

    private BandaidServer server;

    private BandaidServerNetworkedProxy serviceProxy;

    private class TestBandaidServerNetworkedProxy extends BandaidServerNetworkedProxy {
        @Override
        public String handleMessageWithReply(String m) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void handleAsynchonousMessage(String m) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean registerForGame(int networkId, String gameId, ComInterface comInterface) throws PlayerError {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private ServerMessageTranslator serverMessageTranslator;
    private ComServer comServer;
    private BandaidGameServer mockBandaidGameServer;

    private GameMaster gameMaster;

    @Before
    public void before() {

        gameMaster = new SimpleGameMaster();

        comServer = new ComServer();

        server = new BandaidServer();
        server.setComServer(comServer);
        server.addGameMaster("simpleGameMaster", gameMaster);

        server.start();

//        mockBandaidGameServer = mock(BandaidGameServer.class);
//        server.setBandaidGameServer(mockBandaidGameServer);
//          serverMessageTranslator = new JsonServerMessageTranslator();
//        server.setServerMessageTranslator(serverMessageTranslator);



        serviceProxy = new TestBandaidServerNetworkedProxy();
        serviceProxy.init();
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
    public void testSomething() {

        System.out.println(serviceProxy.getServerStatus());

    }

}
