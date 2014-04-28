package com.neodem.bandaid.messaging;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 4/1/14
 */
public enum ServerMessageType {
    //==== server related message types

    hello,

    getPlayerName,

    connect,

    getAvailableGames,

    registerForGame,

    serverGameStatus,

    serverStatus,

    error,

    //=== game message related types

    gameMessage,

    gameMessageNeedsReply,

    unknown;
}
