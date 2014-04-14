package com.neodem.bandaid.server;

import com.neodem.bandaid.gamemaster.PlayerError;
import com.neodem.bandaid.messaging.ServerMessageTranslator;
import com.neodem.bandaid.messaging.ServerMessageType;
import com.neodem.bandaid.network.ComBaseClient;
import com.neodem.bandaid.network.ComServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * top level server!
 *
 * This handles all the server messages and passes them to and from the BandaidGameServer for processing
 *
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 4/9/14
 */
public class BandaidServer {

    private static final Logger log = LogManager.getLogger(BandaidServer.class.getName());

    private MessageProcesser messageHandler;
    private ServerMessageTranslator serverMessageTranslator;
    private ComServer comServer;
    private BandaidGameServer bandaidGameServer;

    public void start() {

        // start main communications system
        comServer.startComServer();

        // set up our message processor
        messageHandler = new MessageProcesser("localhost", 6969);
        Thread mt = new Thread(messageHandler);
        mt.setName("BandaidServer-MessageProcessor");
        mt.start();
    }

    public class MessageProcesser extends ComBaseClient implements Runnable {

        public MessageProcesser(String host, int port) {
            super(host, port);
        }

        @Override
        protected void handleMessage(int from, String msg) {
            log.trace("Server : handle message : " + msg);
            ServerMessageType type = serverMessageTranslator.unmarshalServerMessageTypeFromMessage(msg);

            String replyMessage = null;
            String gameId = serverMessageTranslator.unmarshalGameId(msg);

            switch (type) {
                case serverConnect:
                    String name = serverMessageTranslator.unmarshalServerConnectName(msg);
                    try {
                        bandaidGameServer.connect(from, name);
                    } catch (PlayerError playerError) {
                        replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                    }
                    break;
                case getAvailableGames:
                    Map<String,String> availableGames = bandaidGameServer.getAvailableGames();
                    replyMessage = serverMessageTranslator.marshalAvailableGames(availableGames);
                    break;
                case registerForGame:
                    boolean result;
                    try {
                        result = bandaidGameServer.registerForGame(from, gameId);
                        replyMessage = serverMessageTranslator.marshalServerReplyBoolean(result);
                    } catch (PlayerError playerError) {
                        replyMessage = serverMessageTranslator.marshalPlayerError(playerError);
                    }
                    break;
                case serverGameStatus:
                    String gameStatus = bandaidGameServer.getGameStatus(gameId);
                    replyMessage = serverMessageTranslator.marshalGameStatus(gameStatus);
                    break;
                case serverStatus:
                    String serverStatus = bandaidGameServer.getServerStatus();
                    replyMessage = serverMessageTranslator.marshalServerStatus(serverStatus);
                    break;
            };

            if(replyMessage != null) {
                send(from, replyMessage);
            }
        }

        public void run() {
            init();
        }
    }

    public void setServerMessageTranslator(ServerMessageTranslator serverMessageTranslator) {
        this.serverMessageTranslator = serverMessageTranslator;
    }

    public void setBandaidGameServer(BandaidGameServer bandaidGameServer) {
        this.bandaidGameServer = bandaidGameServer;
    }

    public void setComServer(ComServer comServer) {
        this.comServer = comServer;
    }
}
