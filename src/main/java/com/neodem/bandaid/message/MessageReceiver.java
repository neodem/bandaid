package com.neodem.bandaid.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MessageReceiver extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private MessageHandler handler;

	public MessageReceiver(Socket s, MessageHandler mh, String name) throws IOException {
		super("MessageReceiver:" + name);
		this.socket = s;
		this.handler = mh;
		in = new ObjectInputStream(s.getInputStream());
	}

	@Override
	public void run() {
		while (!socket.isClosed()) {
			try {
				Message m = (Message) in.readObject();
				handler.handleMessage(m);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
