package com.neodem.bandaid.game;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public interface Player<A extends Action> {

    /**
     * called by the GameMaster when this Player has a turn to process.
     * 'turn' may not be a game turn but it is at least a time that
     * the client has to do something
     *
     * @param gc the current game context
     * @return
     */
    public A yourTurn(GameContext gc);

    /**
     * something changed, the GameMaster wants to let you know
     *
     * @param gc
     */
    public void updateContext(GameContext gc);

    /**
     * called by the GameMaster to alert other players to an action
     * by another player.
     *
     * @param player    the player who initiated the action
     * @param hisAction the action initiated
     * @param gc        the current game context
     * @return
     */
    public A actionHappened(Player player, A hisAction, GameContext gc);

    /**
     * The players action was rejected and they will be called to try again
     *
     * @param reason
     */
    public void tryAgain(String reason);

    /**
     * get the id of the player
     *
     * @return
     */
    public String getPlayerName();
}
