package com.neodem.bandaid.server;

import com.neodem.bandaid.network.ComServer;
import com.neodem.bandaid.proxy.BandaidGameServerNetworkTransport;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/25/14
 */
public class BandaidServer {

    private BandaidGameServerNetworkTransport bandaidGameServerNetworkTransport;
    private ComServer comServer;

    public void start() {
        comServer.startComServer();
        bandaidGameServerNetworkTransport.init();
    }

    public void stop() {
    }

    public void setBandaidGameServerNetworkTransport(BandaidGameServerNetworkTransport bandaidGameServerNetworkTransport) {
        this.bandaidGameServerNetworkTransport = bandaidGameServerNetworkTransport;
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }


}
