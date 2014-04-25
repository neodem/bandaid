package com.neodem.bandaid.proxy;

import com.neodem.bandaid.gamemaster.PlayerCallback;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/25/14
 */
public interface PlayerCallbackProxyFactory {
    PlayerCallback makePlayerCallbackProxy(String playerCallbackType);
}
