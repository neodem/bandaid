package com.neodem.bandaid.network;

import com.neodem.bandaid.network.messaging.ComMessageTranslator;
import com.neodem.bandaid.network.messaging.DefaultComMessageTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class ComClient {

    private static final Logger log = LogManager.getLogger(ComClient.class.getName());
    private final String hostName;
    private final int port;
    private final DataInputStream console = null;
    private final ComMessageTranslator mt = new DefaultComMessageTranslator();
    private Socket socket = null;
    private DataOutputStream streamOut = null;
    private volatile ComClientThread clientThread = null;

    /**
     * for handling the client stream data. It will wait until
     * a UTF datagram is received and then call handleMessage()
     */
    private class ComClientThread extends Thread {
        private DataInputStream streamIn = null;

        public void openCommunications() {
            try {
                streamIn = new DataInputStream(socket.getInputStream());
            } catch (IOException ioe) {
                log.error("Error getting input stream: " + ioe);
                stopClientThread();
            }
        }

        public void closeCommunications() {
            try {
                if (streamIn != null) streamIn.close();
            } catch (IOException ioe) {
                log.error("Error closing input stream: " + ioe);
            }
        }

        public void run() {
            Thread thisThread = Thread.currentThread();
            while (clientThread == thisThread)
                try {
                    handle(streamIn.readUTF());
                } catch (IOException ioe) {
                    log.error("Listening error: " + ioe.getMessage());
                    stopClientThread();
                }
        }
    }

    public ComClient(String host, int port) {
        this.hostName = host;
        this.port = port;
    }

    protected abstract String getClientName();

    public void init() {
        log.info("{} is Establishing connection with ComServer. Please wait ...", getClientName());
        try {
            socket = new Socket(hostName, port);
            log.info("{} is Connected to ComServer : {}", getClientName(), socket);
            streamOut = new DataOutputStream(socket.getOutputStream());
            startClientThread();
        } catch (UnknownHostException uhe) {
            log.error("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            log.error("Unexpected exception: " + ioe.getMessage());
        }
    }

    protected void startClientThread() {
        if (clientThread == null) {
            clientThread = new ComClientThread();
            clientThread.setName("ComClientThread");
            clientThread.openCommunications();
            clientThread.start();
        }
    }

    /**
     * send a message to user asynchronously
     *
     * @param destination the dest to send the message to
     * @param message     the message to send
     */
    public void send(int destination, String message) {
        String m = mt.makeMessage(message, DefaultComMessageTranslator.UNKNOWN, destination);

        log.trace("{} : Send to ComServer to route to {} : {}", getClientName(), destination, message);

        // send to ComServer to handle and pass on to the correct client
        try {
            streamOut.writeUTF(m);
            streamOut.flush();
        } catch (IOException ioe) {
            log.error("Sending error: " + ioe.getMessage());
            stopClientThread();
        }
    }

    /**
     * send a message to all other users asynchronously
     *
     * @param message     the message to send
     */
    public void broadcast(String message) {
        String m = mt.makeBroadcastMessage(message);

        log.trace("{} : Send to ComServer to broadcast : {}", getClientName(), message);

        // send to ComServer to handle and pass on to the all other clients
        try {
            streamOut.writeUTF(m);
            streamOut.flush();
        } catch (IOException ioe) {
            log.error("Sending error: " + ioe.getMessage());
            stopClientThread();
        }
    }

    private final void handle(String netMessage) {
        int id = mt.getFrom(netMessage);
        String message = mt.getPayload(netMessage);
        handleMessage(id, message);
    }

    /**
     * clients need to implement this to deal with messages that it gets
     *
     * @param from the id this message was from
     * @param msg  the message
     */
    protected abstract void handleMessage(int from, String msg);

    public void stopClientThread() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            log.error("Error closing ...");
        }
        clientThread.closeCommunications();
        clientThread = null;
    }
}