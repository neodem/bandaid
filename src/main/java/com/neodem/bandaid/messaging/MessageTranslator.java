package com.neodem.bandaid.messaging;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public interface MessageTranslator {

    String marshalMessage(MessageType type);

    String marshalMessage(MessageType type, String message);

    String marshalMessage(MessageType type, boolean bool);

    String marshalPlayerMessage(MessageType type, String playerName);

    String marshalRegistrationMesage(String playerName);

    Boolean unmarshalBooleanFromMessage(String m);

    String unmarshalStringFromMessage(String m);

    String unmarshalPlayerNameFromMessage(String m);

    MessageType unmarshalMessageTypeFromMessage(String m);
}
