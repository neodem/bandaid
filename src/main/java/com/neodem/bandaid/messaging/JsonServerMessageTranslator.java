package com.neodem.bandaid.messaging;

import com.neodem.bandaid.gamemaster.PlayerError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public class JsonServerMessageTranslator implements ServerMessageTranslator {

    private static final String TYPE = "ServerMessageType";
    private static final String GAMEMESSAGE = "GameMessage";
    private static final String GAMEID = "GameId";
    private static final String PLAYERNAME = "PlayerName";
    private static final String AVAILABLEGAMEMAP = "AvailableGames";
    private static final String PLAYERERROR = "PlayerError";
    private static final String REPLY = "isReply";

    @Override
    public ServerMessageType unmarshalServerMessageTypeFromMessage(String m) {
        JSONObject j;
        ServerMessageType result = ServerMessageType.unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                String type = j.getString(TYPE);
                result = ServerMessageType.valueOf(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String marshalGameMessage(String gameMessage) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.gameMessage, j);
        setGameMessageIntoJSONObject(gameMessage, j);
        return j.toString();
    }

    @Override
    public String unmarshalGameMessage(String m) {
        JSONObject j;
        String result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(GAMEMESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String marshalGameMessageExpectsReply(String gameMessage) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.gameMessageNeedsReply, j);
        setGameMessageIntoJSONObject(gameMessage, j);
        return j.toString();
    }

    @Override
    public String marshalConnectRequest(String playerName) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverConnect, j);
        JsonUtil.setGenericStringIntoJSONObject(playerName, j);
        return j.toString();
    }

    @Override
    public String unmarshalConnectRequestName(String m) {
        String result = null;

        if (m != null) {
            try {
                JSONObject j = new JSONObject(m);
                result = JsonUtil.getStringFromJsonObject(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String marshalServerConnectOkReply() {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.serverConnect, j);
        JsonUtil.setGenericStringIntoJSONObject("OK", j);
        return j.toString();
    }

    @Override
    public String marshalGetAvailableGamesRequest() {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.getAvailableGames, j);
        return j.toString();
    }

    @Override
    public String marshalGetAvailableGamesReply(Map<String, String> availableGames) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.getAvailableGames, j);

        JSONArray gameMap = JsonUtil.marshalStringStringMapIntoJsonArray(availableGames);
        try {
            j.put(AVAILABLEGAMEMAP, gameMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j.toString();
    }

    @Override
    public Map<String, String> unmarshalGetAvailableGamesReply(String m) {
        Map<String, String> availableGames = null;

        if (m != null) {
            try {
                JSONObject j = new JSONObject(m);
                JSONArray gameMap = j.getJSONArray(AVAILABLEGAMEMAP);
                availableGames = JsonUtil.getStringStringMapFromJsonArray(gameMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return availableGames;
    }

    @Override
    public String marshalRegisterForGameRequest(String playerName, String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.registerForGame, j);
        JsonUtil.setStringIntoJsonObject(PLAYERNAME, playerName, j);
        JsonUtil.setStringIntoJsonObject(GAMEID, gameId, j);
        return j.toString();
    }

    @Override
    public String unmarshalRegisterForGameRequestName(String m) {
        return JsonUtil.getStringFromJsonMessage(PLAYERNAME, m);
    }

    @Override
    public String unmarshalRegisterForGameRequestGameId(String m) {
        return JsonUtil.getStringFromJsonMessage(GAMEID, m);
    }

    @Override
    public String marshalRegisterForGameReply(boolean result) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.registerForGame, j);

        //TODO

        return j.toString();
    }

    @Override
    public String marshalGetServerStatusRequest() {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverStatus, j);
        return j.toString();
    }

    @Override
    public String marshalGetServerStatusReply(String serverStatus) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.serverStatus, j);
        JsonUtil.setGenericStringIntoJSONObject(serverStatus, j);
        return j.toString();
    }

    @Override
    public String unmarshalGetServerStatusReply(String m) {
        JSONObject j;
        String result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = JsonUtil.getStringFromJsonObject(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String marshalServerGameStatus(String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverGameStatus, j);
        JsonUtil.setGenericStringIntoJSONObject(gameId, j);
        return j.toString();
    }

    @Override
    public boolean unmarshalServerReplyRegisterForGame(String reply) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String unmarshalServerReplyGameStatus(String reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String unmarshalServerGameStatusRequestGameId(String msg) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String marshalServerReplyBoolean(boolean result) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String marshalServerGameStatusReply(String gameStatus) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String marshalPlayerError(PlayerError playerError) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.error, j);
        JsonUtil.setKeyValueStringsIntoJSONObject(PLAYERERROR, playerError.getMessage(), j);
        return j.toString();
    }

    @Override
    public void checkReplyForPlayerError(String reply) throws PlayerError {
        JSONObject j;

        if (reply != null) {
            try {
                j = new JSONObject(reply);
                String playerErrorMessage = j.getString(PLAYERERROR);
                if (playerErrorMessage != null) {
                    throw new PlayerError(playerErrorMessage);
                }
            } catch (JSONException e) {
            }
        }
    }

    protected void setReplyFlag(JSONObject j) {
        if (j != null) {
            try {
                j.put(REPLY, "true");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setGameMessageIntoJSONObject(String gameMessage, JSONObject j) {
        if (gameMessage != null && j != null) {
            try {
                j.put(GAMEMESSAGE, gameMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setMessageTypeIntoJSONObject(ServerMessageType type, JSONObject j) {
        if (type != null && j != null) {
            try {
                j.put(TYPE, type.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
