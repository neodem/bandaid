package com.neodem.bandaid.testGame;

import com.neodem.bandaid.gamemasterstuff.BaseGameMaster;
import com.neodem.bandaid.gamemasterstuff.PlayerCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/24/14
 */
public class SimpleGameMaster extends BaseGameMaster {
    private static final Logger log = LogManager.getLogger(SimpleGameMaster.class.getName());

    private boolean ready = false;

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
    public boolean isGameReadyToStart() {
        return ready;
    }

    @Override
    public String getGameDescription() {
        return "Simple Game Master desctiption";
    }

    @Override
    public boolean registerPlayer(PlayerCallback player) {
        ready = true;
        return true;
    }

    @Override
    public String getGameStatus() {
        return "Ready to Play";
    }

}
