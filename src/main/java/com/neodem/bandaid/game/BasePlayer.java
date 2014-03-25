package com.neodem.bandaid.game;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class BasePlayer<A extends Action> implements Player<A> {
    protected String playerName;
    protected GameMasterCommunicationChannel gameMaster;
    protected GameContext currentGameContext;

    @Override
    public String toString() {
        return playerName;
    }

    public void updateContext(GameContext gc) {
        currentGameContext = gc;
    }

    public void startPlayer() {

        GameContext currentGameContext = gameMaster.registerPlayerForNextGame(this);

        initializePlayer(currentGameContext);
    }

    protected abstract void initializePlayer(GameContext g);

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setGameMaster(GameMasterCommunicationChannel gm) {
        this.gameMaster = gm;
    }
}
