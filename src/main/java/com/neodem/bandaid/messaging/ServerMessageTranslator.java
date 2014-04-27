package com.neodem.bandaid.messaging;

import com.neodem.bandaid.gamemasterstuff.PlayerError;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public interface ServerMessageTranslator {

    ServerMessageType unmarshalServerMessageTypeFromMessage(String serverMessage);

    // server messages

    // connect
    String marshalConnectRequest(String playerName);

    String unmarshalConnectRequestName(String serverMessage);

    String marshalServerConnectOkReply();

    // getServerStatus
    String marshalGetServerStatusRequest();

    String marshalGetServerStatusReply(String serverStatus);

    String unmarshalGetServerStatusReply(String serverMessage);

    // getAvailableGames
    String marshalGetAvailableGamesRequest();

    String marshalGetAvailableGamesReply(Map<String, String> availableGames);

    Map<String, String> unmarshalGetAvailableGamesReply(String serverMessage);

    //registerForGame
    String marshalRegisterForGameRequest(String playerName, String gameId);

    String unmarshalRegisterForGameRequestName(String serverMessage);

    String unmarshalRegisterForGameRequestGameId(String serverMessage);

    String marshalRegisterForGameReply(boolean result);

    boolean unmarshalRegisterForGameReply(String serverMessage);

    //getGameStatus
    String marshalGetGameStatusRequest(String gameId);

    String unmarshalGetGameStatusReply(String serverMessage);

    String marshalServerGameStatusReply(String gameStatus);

    String unmarshalServerGameStatusRequestGameId(String serverMessage);

    // -- misc
    String marshalPlayerError(PlayerError playerError);

    void checkReplyForPlayerError(String serverMessage) throws PlayerError;

    boolean isReply(String serverMessage);

    /// game messages

    String marshalGameMessage(String gameMessage);

    String unmarshalGameMessage(String gameMessage);

    String marshalGameMessageExpectsReply(String gameMessage);

}
