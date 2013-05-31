package com.neodem.bandaid.server.id;

import java.net.Socket;

public class DefatultClientIdService implements ClientIdService {

	private int index = 0;
	
	public ClientId makeNewId(Socket s) {
		return new ClientId(index++);
	}

}
