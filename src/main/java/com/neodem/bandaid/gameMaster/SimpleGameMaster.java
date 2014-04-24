package com.neodem.bandaid.gamemaster;

import com.neodem.bandaid.network.ComInterface;
import com.neodem.bandaid.proxy.PlayerProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class SimpleGameMaster extends BaseGameMaster {
    private static final Logger log = LogManager.getLogger(SimpleGameMaster.class.getName());

    @Override
    protected Logger getLog() {
        return log;
    }

    @Override
    protected void runGame() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initGame() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean gameReady() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getGameDescription() {
        return "Simple Game Master";
    }

    @Override
    public boolean registerPlayer(int networkId, PlayerCallback player) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getGameStatus() {
        return "Ready to Play";
    }

    @Override
    public PlayerProxy makeNewProxy(String playerName, int myNetworkId, ComInterface server) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
