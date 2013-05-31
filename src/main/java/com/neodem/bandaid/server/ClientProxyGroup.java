package com.neodem.bandaid.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.neodem.bandaid.message.Message;
import com.neodem.bandaid.server.id.ClientId;
import com.neodem.bandaid.server.id.ClientIdService;
import com.neodem.bandaid.server.id.DefatultClientIdService;

public class ClientProxyGroup extends Thread {

	private Map<ClientId, ClientProxy> clients;
	private ClientIdService clientIdService;

	public ClientProxyGroup() {
		super("ClientGroup");
		clients = new HashMap<>();
		clientIdService = new DefatultClientIdService();
	}

	public void addClient(Socket s) throws IOException {
		ClientId id = clientIdService.makeNewId(s);
		ClientProxy cp = new ClientProxy(s, id, this);
		clients.put(id, cp);
		cp.start();
	}

	public void sendMessage(Message m) throws IOException {
		for (ClientProxy cp : clients.values()) {
			cp.sendMessage(m);
		}
	}

	public void sendMessage(ClientId id, Message m) throws IOException {
		ClientProxy cp = clients.get(id);
		if (cp != null)
			cp.sendMessage(m);
	}

	public void handleInput(Message m, ClientProxy cp) {
		switch(m.getMessageType()) {
		case DISCONNECT:
			cp.setStopped(true);
			break;
		case TEXT:
			System.out.println(m.getBody());
			break;
		default:
			break;
		
		}
	}

	public void cleanHouse() {
		for (ClientProxy cp : clients.values()) {
			if (cp == null || cp.isStopped()) {
				clients.remove(cp);
			}
		}
	}

	public void run() {
		while (true) {
			try {
				sleep(30000);
			} catch (Exception e) {
			}
			cleanHouse();
		}
	}
}
