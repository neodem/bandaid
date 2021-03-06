package com.neodem.bandaid.gamemasterstuff;

import org.apache.logging.log4j.Logger;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/31/14
 */
public abstract class BaseGameMaster implements GameMaster, Runnable {

    private Thread gameThread;

    protected abstract Logger getLog();

    /**
     * this is the main game loop. It gets run on a thread and once it's over
     * the thread/game dies
     */
    protected abstract void runGame();

    protected String getGameThreadName() {
        return "GameMaster";
    }

    public abstract void initGame();

    public final void startGame() {
        gameThread = new Thread(this);
        gameThread.setName(getGameThreadName());
        gameThread.start();
    }

    public final void run() {
        runGame();
    }
}
