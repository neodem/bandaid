package com.neodem.bandaid.messaging;

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
    public String marshalServerRegisterForGame(int networkId, String gameId) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.registerForGame, j);
        JsonUtil.setStringIntoJSONObject(gameId, j);
        JsonUtil.setIntegerIntoJSONObject(networkId, j);
        return j.toString();
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
    public Map<String, String> unmarshalServerReplyAvailableGames(String reply) {
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
