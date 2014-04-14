package com.neodem.bandaid.messaging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public class JsonUtil {

    private static final String BOOL = "Boolean";
    private static final String STRING = "String";
    private static final String INTEGER = "Integer";

    public static void setBooleanIntoJSONObject(boolean bool, JSONObject j) {
        if (j != null) {
            try {
                j.put(BOOL, Boolean.valueOf(bool));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStringIntoJSONObject(String string, JSONObject j) {
        if (string != null && j != null) {
            try {
                j.put(STRING, string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setIntegerIntoJSONObject(Integer i, JSONObject j) {
        if (i != null && j != null) {
            try {
                j.put(INTEGER, i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getStringFromJsonObject(JSONObject j) {
        String result = null;
        if (j != null) {
            try {
                result = j.getString(STRING);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static Boolean getBooleanFromJsonObject(JSONObject j) {
        Boolean result = null;
        if (j != null) {
            try {
                result = j.getBoolean(BOOL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static Integer getIntegerFromJsonObject(JSONObject j) {
        Integer result = null;
        if (j != null) {
            try {
                result = j.getInt(INTEGER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
