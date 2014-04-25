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


    // --- mixed

    String marshalGameMessageExpectsReply(String gameMessage);

    String marshalRegisterForGameReply(String playerCallbackType);

    String unmarshalServerReplyRegisterForGame(String reply);

    String marshalServerGameStatus(String gameId);

    String unmarshalServerReplyGameStatus(String reply);

    String marshalPlayerError(PlayerError playerError);

    String marshalServerReplyBoolean(boolean result);

    String marshalServerGameStatusReply(String gameStatus);

    void checkReplyForPlayerError(String reply) throws PlayerError;

    /// game messages

    String marshalGameMessage(String gameMessage);

    String unmarshalGameMessage(String m);


    String unmarshalServerGameStatusRequestGameId(String msg);
}
