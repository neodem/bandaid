package com.neodem.bandaid.game;

import org.apache.commons.logging.Log;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BasePlayer<A extends Action> implements Player<A> {

    protected String myName;
    protected GameContext currentGameContext;

    public BasePlayer(String name) {
        myName = name;
    }

    protected abstract Log getLog();

    @Override
    public String toString() {
        return myName;
    }

    public void updateContext(GameContext gc) {
        currentGameContext = gc;
    }

    public String getMyName() {
        return myName;
    }

    @Override
    public void initializePlayer(GameContext g) {
        currentGameContext = g;
    }
}
