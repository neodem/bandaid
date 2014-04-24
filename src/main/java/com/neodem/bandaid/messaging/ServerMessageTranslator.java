package com.neodem.bandaid.messaging;

import com.neodem.bandaid.gamemaster.PlayerError;

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


    String marshalAvailableGames(Map<String,String> availableGames);
    Map<String, String> unmarshalAvailableGames(String reply);

    String marshalRegisterForGameRequest(int networkId, String gameId);

    String marshalRegisterForGameReply(boolean result);
    boolean unmarshalServerReplyRegisterForGame(String reply);


    String marshalServerRequestGetServerStatus();

    String unmarshalServerReplyServerStatus(String reply);

    String marshalServerGameStatus(String gameId);

    String unmarshalServerReplyGameStatus(String reply);

    String unmarshalGameId(String msg);

    String unmarshalServerConnectName(String msg);

    String marshalPlayerError(PlayerError playerError);



    String marshalServerReplyBoolean(boolean result);

    String marshalGameStatus(String gameStatus);

    String marshalServerStatus(String serverStatus);

    void checkReplyForPlayerError(String reply) throws PlayerError;

    /// game messages

    String marshalGameMessage(String gameMessage);

    String unmarshalGameMessage(String m);


}
