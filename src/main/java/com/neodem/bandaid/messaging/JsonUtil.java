package com.neodem.bandaid.messaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public class JsonUtil {

    private static final String BOOL = "Boolean";
    private static final String STRING = "String";
    private static final String INTEGER = "Integer";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    public static void setBooleanIntoJSONObject(boolean bool, JSONObject j) {
        if (j != null) {
            try {
                j.put(BOOL, Boolean.valueOf(bool));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getBooleanFromJsonMessage(String m) {
        boolean result = false;

        if (m != null) {
            try {
                JSONObject j = new JSONObject(m);
                result = j.getBoolean(BOOL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static JSONArray marshalStringStringMapIntoJsonArray(Map<String, String> map) {
        JSONArray a = new JSONArray();

        for (String gameKey : map.keySet()) {
            String gameValue = map.get(gameKey);
            JSONObject keyValue = new JSONObject();
            setKeyValueStringsIntoJSONObject(gameKey, gameValue, keyValue);
            a.put(keyValue);
        }

        return a;
    }

    public static Map<String, String> getStringStringMapFromJsonArray(JSONArray array) {
        Map<String, String> result = null;
        if (array != null) {
            result = new HashMap<>();
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject element = array.getJSONObject(i);

                    String key = getKeyFromKeyValueJSONObject(element);
                    String value = getValueFromKeyValueJSONObject(element);

                    result.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String getKeyFromKeyValueJSONObject(JSONObject j) {
        String result = null;
        if (j != null) {
            try {
                result = j.getString(KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String getValueFromKeyValueJSONObject(JSONObject j) {
        String result = null;
        if (j != null) {
            try {
                result = j.getString(VALUE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public static void setKeyValueStringsIntoJSONObject(String key, String value, JSONObject j) {
        if (key != null && j != null) {
            try {
                j.put(KEY, key);
                j.put(VALUE, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStringIntoJsonObject(String key, String value, JSONObject j) {
        if (value != null && j != null) {
            try {
                j.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getStringFromJsonMessage(String key, String message) {
        String result = null;

        if (message != null) {
            try {
                JSONObject j = new JSONObject(message);
                result = j.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static void setGenericStringIntoJSONObject(String string, JSONObject j) {
        setStringIntoJsonObject(STRING, string, j);
    }

    public static String getGenericStringFromJsonMessage(String message) {
        String result = null;

        if (message != null) {
            try {
                JSONObject j = new JSONObject(message);
                result = j.getString(STRING);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
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
