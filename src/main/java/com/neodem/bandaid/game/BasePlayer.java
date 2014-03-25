package com.neodem.bandaid.game;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BasePlayer<A extends Action> implements Player<A> {
    protected String myName;
    protected GameMasterCommunicationChannel gameMaster;
    protected GameContext currentGameContext;

    @Override
    public String toString() {
        return myName;
    }

    public void updateContext(GameContext gc) {
        currentGameContext = gc;
    }

    public void startPlayer() {

        GameContext currentGameContext = gameMaster.registerPlayerForNextGame(this);

        initializePlayer(currentGameContext);
    }

    protected abstract void initializePlayer(GameContext g);

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setGameMaster(GameMasterCommunicationChannel gm) {
        this.gameMaster = gm;
    }
}
