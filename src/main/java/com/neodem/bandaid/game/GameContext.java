package com.neodem.bandaid.game;

import org.apache.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: vfumo
 * Date: 2/28/14
 */
public abstract class GameContext<P extends Player> {

    protected abstract Log getLog();

    // a list of the players in the game (ordered by their position in the game)
    protected List<P> players = new ArrayList<>();

    public void addPlayer(P p) {
        players.add(p);
    }

    public List<P> getPlayers() {
        return players;
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();
        b.append("Game Context\n");
        b.append("------------\n");
        b.append(players.size());
        b.append(" players: ");

        for (Player p : players) {
            b.append(p);
            b.append(' ');
        }

        b.append('\n');

        return b.toString();
    }
}
