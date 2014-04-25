package com.neodem.bandaid.server;

import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.BandaidGameServerNetworkProxy;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/25/14
 */
public class BandaidServer {

    private BandaidGameServerNetworkProxy bandaidGameServerNetworkProxy;
    private ComServer comServer;

    public void start() {
        comServer.startComServer();
        bandaidGameServerNetworkProxy.init();
    }

    public void stop() {
    }

    public void setBandaidGameServerNetworkProxy(BandaidGameServerNetworkProxy bandaidGameServerNetworkProxy) {
        this.bandaidGameServerNetworkProxy = bandaidGameServerNetworkProxy;
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }


}
