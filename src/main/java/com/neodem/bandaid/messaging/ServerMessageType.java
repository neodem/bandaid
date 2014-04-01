package com.neodem.bandaid.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public enum ServerMessageType {
    //==== server related message types

    reply,

    // called from client : used to see the current status of the server
    status,

    // called from client : used to resiger your name with the server
    register,

    // called from server : used to tell a client the current status
    serverStatus,

    gameMessage,

    gameMessageNeedsReply,

    unknown;
}
