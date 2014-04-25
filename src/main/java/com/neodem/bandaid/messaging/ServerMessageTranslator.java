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

    // connect
    String marshalConnectRequest(String playerName);

    String unmarshalConnectRequestName(String msg);

    String marshalServerConnectOkReply();

    // getServerStatus
    String marshalGetServerStatusRequest();

    String marshalGetServerStatusReply(String serverStatus);

    String unmarshalGetServerStatusReply(String reply);

    // getAvailableGames
    String marshalGetAvailableGamesRequest();

    String marshalGetAvailableGamesReply(Map<String, String> availableGames);

    Map<String, String> unmarshalGetAvailableGamesReply(String reply);

    //registerForGame
    String marshalRegisterForGameRequest(String playerName, String gameId);

    String unmarshalRegisterForGameRequestName(String msg);

    String unmarshalRegisterForGameRequestGameId(String msg);

    String marshalRegisterForGameReply(boolean result);

    boolean unmarshalRegisterForGameReply(String reply);

    //getGameStatus
    String marshalGetGameStatusRequest(String gameId);

    String unmarshalGetGameStatusReply(String reply);

    String marshalServerGameStatusReply(String gameStatus);

    String unmarshalServerGameStatusRequestGameId(String msg);

    // -- misc
    String marshalPlayerError(PlayerError playerError);

    void checkReplyForPlayerError(String reply) throws PlayerError;


    /// game messages

    String marshalGameMessage(String gameMessage);

    String unmarshalGameMessage(String m);

    String marshalGameMessageExpectsReply(String gameMessage);

}
