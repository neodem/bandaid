package com.neodem.bandaid.gamemasterstuff;

/**
 * Created by vfumo on 4/27/14.
 */
public interface PlayerCallbackProxyFactory {
    PlayerCallback makeNewProxy(int clientNetworkId);
}
