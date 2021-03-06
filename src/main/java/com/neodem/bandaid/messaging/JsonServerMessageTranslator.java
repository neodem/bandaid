package com.neodem.bandaid.messaging;

import com.neodem.bandaid.gamemasterstuff.PlayerError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
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
    private static final String CLIENTTYPE = "ClientType";

    @Override
    public ServerMessageType unmarshalServerMessageTypeFromMessage(String m) {
        ServerMessageType result = ServerMessageType.unknown;

        if (m != null) {
            try {
                JSONObject j = new JSONObject(m);
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
    public String marshalHello(NetworkEntityType clientType, String identifier) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.hello, j);
        JsonUtil.setStringIntoJsonObject(CLIENTTYPE, clientType.name(), j);
        JsonUtil.setStringIntoJsonObject(PLAYERNAME, identifier, j);
        return j.toString();
    }

    @Override
    public NetworkEntityType unmarshalNetworkEntityType(String m) {
        NetworkEntityType result = NetworkEntityType.unknown;

        if (m != null) {
            try {
                JSONObject j = new JSONObject(m);
                String type = j.getString(CLIENTTYPE);
                result = NetworkEntityType.valueOf(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String marshalGameMessageReply(String gameMessage) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
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
    public String marshalConnectRequest() {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.connect, j);
        return j.toString();
    }


    @Override
    public String marshalServerConnectOkReply() {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.connect, j);
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
    public String unmarshalPlayerName(String msg) {
        return JsonUtil.getStringFromJsonMessage(PLAYERNAME, msg);
    }

    @Override
    public String marshalRegisterForGameRequest(String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.registerForGame, j);
        JsonUtil.setStringIntoJsonObject(GAMEID, gameId, j);
        return j.toString();
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

        JsonUtil.setBooleanIntoJSONObject(result, j);

        return j.toString();
    }

    @Override
    public boolean unmarshalRegisterForGameReply(String m) {
        return JsonUtil.getBooleanFromJsonMessage(m);
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
    public String marshalGetGameStatusRequest(String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverGameStatus, j);
        JsonUtil.setStringIntoJsonObject(GAMEID, gameId, j);
        return j.toString();
    }

    @Override
    public String unmarshalServerGameStatusRequestGameId(String m) {
        return JsonUtil.getStringFromJsonMessage(GAMEID, m);
    }

    @Override
    public String marshalServerGameStatusReply(String gameStatus) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.serverGameStatus, j);
        JsonUtil.setGenericStringIntoJSONObject(gameStatus, j);
        return j.toString();
    }

    @Override
    public String unmarshalGetGameStatusReply(String m) {
        return JsonUtil.getGenericStringFromJsonMessage(m);
    }

    @Override
    public String marshalPlayerError(PlayerError playerError) {
        JSONObject j = new JSONObject();
        setReplyFlag(j);
        setMessageTypeIntoJSONObject(ServerMessageType.error, j);
        JsonUtil.setStringIntoJsonObject(PLAYERERROR, playerError.getMessage(), j);
        return j.toString();
    }

    @Override
    public void checkReplyForPlayerError(String reply) throws PlayerError {
        if (reply != null) {
            try {
                JSONObject j = new JSONObject(reply);
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


    @Override
    public boolean isReply(String m) {
        if (m != null) {
            try {
                JSONObject j = new JSONObject(m);
                String replyFlag = j.getString(REPLY);
                if ("true".equals(replyFlag)) {
                    return true;
                }
            } catch (JSONException e) {
            }
        }
        return false;
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
