package com.neodem.bandaid.network.messaging;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/28/14
 */
public interface ComMessageTranslator {

    int getTo(String m);

    int getFrom(String netMessage);

    String getPayload(String m);

    /**
     * @param payload the message
     * @param from    the id this is from
     * @param to      the id we want to send to
     * @return
     */
    String makeMessage(String payload, int from, int to);

    /**
     * @param payload the message
     * @return a marshaled message
     */
    String makeBroadcastMessage(String payload);

    boolean isBroadcastMessage(String payload);

    ///********

    /**
     * change the from value
     *
     * @param msg
     */
    String updateFrom(int newFrom, String msg);


    /**
     * change the to value
     *
     * @param msg
     */
    String updateTo(int newTo, String msg);
}
