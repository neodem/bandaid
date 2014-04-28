package com.neodem.bandaid.network.messaging;

import com.neodem.bandaid.network.ComServer;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Vincent Fumo (vfumo) : neodem@gmail.com
 * Created Date: 3/28/14
 */
public class DefaultComMessageTranslator implements ComMessageTranslator {

    private static final String TO = "to";
    private static final String FROM = "from";
    private static final String PAYLOAD = "p";

    private static final int BROADCAST = -999;
    public static final int UNKNOWN = -9999;

    @Override
    public int getTo(String m) {
        JSONObject j;
        int result = ComServer.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getInt(TO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public int getFrom(String m) {
        JSONObject j;
        int result = ComServer.Unknown;

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getInt(FROM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String getPayload(String m) {
        JSONObject j;
        String result = "<unknown>";

        if (m != null) {
            try {
                j = new JSONObject(m);
                result = j.getString(PAYLOAD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String makeMessage(String payload, int from, int to) {
        JSONObject j = new JSONObject();
        setDest(to, j);
        setFrom(from, j);
        setPayload(payload, j);
        return j.toString();
    }

    @Override
    public String makeBroadcastMessage(String payload) {
        return makeMessage(payload, UNKNOWN, BROADCAST);
    }

    @Override
    public boolean isBroadcastMessage(String payload) {
        int to = getTo(payload);
        return to == BROADCAST;
    }

    @Override
    public String updateFrom(int newFrom, String msg) {
        String payload = getPayload(msg);
        int to = getTo(msg);

        return makeMessage(payload, newFrom, to);
    }

    @Override
    public String updateTo(int newTo, String msg) {
        String payload = getPayload(msg);
        int from = getFrom(msg);

        return makeMessage(payload, from, newTo);
    }

    protected void setFrom(int f, JSONObject j) {
        if (j != null) {
            try {
                j.put(FROM, f);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setDest(int d, JSONObject j) {
        if (j != null) {
            try {
                j.put(TO, d);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setPayload(String payload, JSONObject j) {
        if (payload != null && j != null) {
            try {
                j.put(PAYLOAD, payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
