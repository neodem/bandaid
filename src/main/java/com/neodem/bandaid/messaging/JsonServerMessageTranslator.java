package com.neodem.bandaid.messaging;

import com.neodem.bandaid.gamemaster.PlayerError;
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

    private static final String PLAYERERROR = "PlayerError";

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
    public String marshalServerConnect(int networkId, String name) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverConnect, j);
        JsonUtil.setStringIntoJSONObject(name, j);
        JsonUtil.setIntegerIntoJSONObject(networkId, j);
        return j.toString();
    }

    @Override
    public String marshalServerGetAvailableGames() {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.getAvailableGames, j);
        return j.toString();
    }

    @Override
    public String marshalRegisterForGameRequest(int networkId, String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.registerForGame, j);
        JsonUtil.setStringIntoJSONObject(gameId, j);
        JsonUtil.setIntegerIntoJSONObject(networkId, j);
        return j.toString();
    }

    @Override
    public String marshalRegisterForGameReply(boolean result) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String marshalServerServerStatus() {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverStatus, j);
        return j.toString();
    }

    @Override
    public String marshalServerGameStatus(String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.serverGameStatus, j);
        JsonUtil.setStringIntoJSONObject(gameId, j);
        return j.toString();
    }

    @Override
    public String marshalAvailableGames(Map<String, String> availableGames) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> unmarshalAvailableGames(String reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean unmarshalServerReplyRegisterForGame(String reply) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String unmarshalServerReplyServerStatus(String reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String unmarshalServerReplyGameStatus(String reply) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String unmarshalGameId(String msg) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String unmarshalServerConnectName(String msg) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String marshalPlayerError(PlayerError playerError) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.reply, j);
        setKeyValueStringsIntoJSONObject(PLAYERERROR, playerError.getMessage(), j);
        return j.toString();
    }



    @Override
    public String marshalServerReplyBoolean(boolean result) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String marshalGameStatus(String gameStatus) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String marshalServerStatus(String serverStatus) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void checkReplyForPlayerError(String reply) throws PlayerError {
        JSONObject j;

        if (reply != null) {
            try {
                j = new JSONObject(reply);
                String playerErrorMessage = j.getString(PLAYERERROR);
                if(playerErrorMessage != null) {
                    throw new PlayerError(playerErrorMessage);
                }
            } catch (JSONException e) {
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

    protected void setKeyValueStringsIntoJSONObject(String key, String value, JSONObject j) {
        if (key != null && j != null) {
            try {
                j.put(key, value);
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
