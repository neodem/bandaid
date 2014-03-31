package com.neodem.bandaid.messaging;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Will create/read messages using JSON
 * <p/>
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/27/14
 */
public class JsonMessageTranslator implements MessageTranslator {

    private static final String TYPE = "MessageType";
    private static final String MESSAGE = "Message";
    private static final String PLAYER = "PlayerName";
    private static final String BOOL = "TrueFalse";

    @Override
    public String marshalMessage(MessageType type) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(MessageType type, String message) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setStringIntoJSONObject(message, j);
        return j.toString();
    }

    @Override
    public String marshalPlayerMessage(MessageType type, String playerName) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setPlayerNameIntoJSONObject(playerName, j);
        return j.toString();
    }

    @Override
    public String marshalRegistrationMesage(String playerName) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(MessageType.register, j);
        setPlayerNameIntoJSONObject(playerName, j);
        return j.toString();
    }

    @Override
    public String marshalMessage(MessageType type, boolean bool) {
        JSONObject j = new JSONObject();
        setMessageTypeIntoJSONObject(type, j);
        setBooleanIntoJSONObject(bool, j);
        return j.toString();
    }

    @Override
    public Boolean unmarshalBooleanFromMessage(String m) {
        JSONObject j;
        Boolean result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getBoolean(BOOL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String unmarshalStringFromMessage(String m) {
        JSONObject j;
        String result = null;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(MESSAGE);
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
    public MessageType unmarshalMessageTypeFromMessage(String m) {
        JSONObject j;
        MessageType result = MessageType.unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                String type = j.getString(TYPE);
                result = MessageType.valueOf(type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected String unmarshalPlayerNameFromJSONObject(JSONObject j) {
        String result = null;
        if (j != null) {
            try {
                result = j.getString(PLAYER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void setBooleanIntoJSONObject(boolean bool, JSONObject j) {
        if (j != null) {
            try {
                j.put(BOOL, Boolean.valueOf(bool));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setStringIntoJSONObject(String message, JSONObject j) {
        if (message != null && j != null) {
            try {
                j.put(MESSAGE, message);
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

    protected void setMessageTypeIntoJSONObject(MessageType type, JSONObject j) {
        if (type != null && j != null) {
            try {
                j.put(TYPE, type.name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
