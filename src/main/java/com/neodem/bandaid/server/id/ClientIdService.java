package com.neodem.bandaid.server.id;

import java.net.Socket;

public interface ClientIdService {

	ClientId makeNewId(Socket s);

}
