package com.neodem.bandaid.proxy;

import com.neodem.bandaid.network.ComInterface;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/1/14
 */
public interface PlayerProxyFactory {
    PlayerProxy makeNewProxy(String playerName, int myNetworkId, ComInterface server);
}
