package com.neodem.bandaid.network;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/30/14
 */
@Deprecated
public interface ComInterface {
    String sendAndGetReply(int id, String m);

    void sendMessage(int id, String m);
}
