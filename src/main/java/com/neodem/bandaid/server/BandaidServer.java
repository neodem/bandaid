package com.neodem.bandaid.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class BandaidServer {

	private static class BandaidServerThread extends Thread {
		private static Logger log = Logger.getLogger(BandaidServerThread.class);
		private static final int PORT = 6969;
		private ServerSocket serverSocket = null;
		private ClientProxyGroup clients;

		public BandaidServerThread() {
			super("BandaidServer");
			try {
				serverSocket = new ServerSocket(PORT);
			} catch (IOException e) {
				log.error("could not initialize socket : " + e.getMessage());
				e.printStackTrace();
			}

			clients = new ClientProxyGroup();
			clients.start();

			log.info("Server Initialized and Ready for Connections");
		}

		@Override
		public void run() {
			while (serverSocket != null) {
				Socket s;
				try {
					s = serverSocket.accept();
					log.info("Received new connection");
					clients.addClient(s);
				} catch (IOException e) {
					String msg = "issue with new connection : " + e.getMessage();
					log.error(msg);
					throw new RuntimeException(msg, e);
				}
			}
		}

		@Override
		protected void finalize() throws Throwable {
			serverSocket.close();
		}
	}

	public static void main(String[] args) {
		new BandaidServerThread().start();
	}
}
