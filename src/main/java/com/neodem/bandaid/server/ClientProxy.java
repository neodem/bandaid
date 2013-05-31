package com.neodem.bandaid.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.neodem.bandaid.message.Message;
import com.neodem.bandaid.message.MessageHandler;
import com.neodem.bandaid.message.MessageReceiver;
import com.neodem.bandaid.server.id.ClientId;

public class ClientProxy extends Thread {

	private ClientProxyGroup parent;
	private Socket socket;
	private boolean stopped;

	private ObjectOutputStream out;
	private MessageReceiver mr;
	
	private class Handler implements MessageHandler {

		private ClientProxy owner;

		public Handler(ClientProxy clientProxy) {
			owner = clientProxy;
		}

		@Override
		public void handleMessage(Message m) {
			parent.handleInput(m, owner);
		}
	}

	public ClientProxy(Socket s, ClientId id, final ClientProxyGroup parent) throws IOException {
		this.parent = parent;
		this.socket = s;
		this.stopped = true;
		
		out = new ObjectOutputStream(s.getOutputStream());
		out.writeObject(Message.HELLO_MESSAGE);
		out.flush();
		
		mr = new MessageReceiver(s, new Handler(this), "ClientProxy" + id);
		mr.start();
	}

	@Override
	public void run() {
		while(true) {
			
		}
	}

	public void sendMessage(Message msg) throws IOException {
		out.writeObject(msg);
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			socket.close();
		} catch (Exception e) {
			socket = null;
		}
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public boolean isStopped() {
		return stopped;
	}
}
