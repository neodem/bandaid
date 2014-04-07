package com.neodem.bandaid.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public interface ServerMessageTranslator {
    ServerMessageType unmarshalServerMessageTypeFromMessage(String msg);

    String unmarshalPlayerNameFromMessage(String msg);

    public String marshalRegistrationMesage(String playerName);

    String getGameMessage(String m);

    String marshalGameMessage(String gameMessage);

    String marshalGameMessageExpectsReply(String gameMessage);
}
