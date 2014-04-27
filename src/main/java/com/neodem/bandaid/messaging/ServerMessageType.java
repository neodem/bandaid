package com.neodem.bandaid.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
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
