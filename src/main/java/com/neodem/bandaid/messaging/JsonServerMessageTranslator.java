package com.neodem.bandaid.messaging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public class JsonServerMessageTranslator implements ServerMessageTranslator {

    private static final String TYPE = "ServerMessageType";
    private static final String PLAYER = "PlayerName";
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
    public String unmarshalPlayerNameFromMessage(String m) {
        JSONObject j;
        String result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(PLAYER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String marshalRegistrationMesage(String playerName) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.register, j);
        setPlayerNameIntoJSONObject(playerName, j);
        return j.toString();
    }

    @Override
    public String getGameMessage(String m) {
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
    public String marshalGameMessage(String gameMessage) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.gameMessage, j);
        setGameMessageIntoJSONObject(gameMessage, j);
        return j.toString();
    }

    @Override
    public String marshalGameMessageExpectsReply(String gameMessage) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(ServerMessageType.gameMessageNeedsReply, j);
        setGameMessageIntoJSONObject(gameMessage, j);
        return j.toString();
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

    protected void setPlayerNameIntoJSONObject(String playerName, JSONObject j) {
        if (playerName != null && j != null) {
            try {
                j.put(PLAYER, playerName);
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
