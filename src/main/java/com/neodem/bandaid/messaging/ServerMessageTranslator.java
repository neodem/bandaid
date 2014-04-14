package com.neodem.bandaid.messaging;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public interface ServerMessageTranslator {


    ServerMessageType unmarshalServerMessageTypeFromMessage(String msg);

    // server messages

    String marshalGameMessageExpectsReply(String gameMessage);

    String marshalServerConnect(int networkId, String name);

    String marshalServerGetAvailableGames();

    Map<String, String> unmarshalServerReplyAvailableGames(String reply);

    String marshalServerRegisterForGame(int networkId, String gameId);

    boolean unmarshalServerReplyRegisterForGame(String reply);

    String marshalServerServerStatus();

    String unmarshalServerReplyServerStatus(String reply);

    String marshalServerGameStatus(String gameId);

    String unmarshalServerReplyGameStatus(String reply);


    /// game messages

    String unmarshalGameMessage(String m);

    String marshalGameMessage(String gameMessage);

    void checkReplyForPlayerError(String reply) throws PlayerError;
}
