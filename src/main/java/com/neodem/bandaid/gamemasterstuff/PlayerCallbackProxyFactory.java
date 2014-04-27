package com.neodem.bandaid.gamemasterstuff;

import com.neodem.bandaid.proxy.PlayerCallbackProxy;

/**
 * Created by vfumo on 4/27/14.
 */
public interface PlayerCallbackProxyFactory {
    PlayerCallbackProxy makeNewProxy(int clientNetworkId, String playerName);
}
