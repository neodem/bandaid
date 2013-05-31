package com.neodem.bandaid.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.neodem.bandaid.message.Message;
import com.neodem.bandaid.message.MessageHandler;
import com.neodem.bandaid.message.MessageReceiver;
import com.neodem.bandaid.message.Message.MessageType;

public class TestClient {

	private MessageReceiver mr;
	private ObjectOutputStream out;

	TestClient() throws UnknownHostException, IOException {
		Socket s = new Socket(InetAddress.getLocalHost(), 6969);
		turnOnMessageReceiver(s);

		out = new ObjectOutputStream(s.getOutputStream());

		writeText("test");
		writeText("1");
		writeText("2");
		writeText("3");

		out.writeObject(Message.BYE_MESSAGE);
		out.flush();

		out.close();
		s.close();
		s = null;
	}

	/**
	 * @param out
	 * @throws IOException
	 */
	private void writeText(String txt) throws IOException {
		Message m = new Message(MessageType.TEXT, txt);
		out.writeObject(m);
		out.flush();
	}

	/**
	 * @param s
	 * @throws IOException
	 */
	private void turnOnMessageReceiver(Socket s) throws IOException {
		MessageHandler mh = new MessageHandler() {

			@Override
			public void handleMessage(Message m) {
				if (m.getMessageType() == MessageType.TEXT) {
					System.out.println(m.getBody());
				}
			}
		};

		mr = new MessageReceiver(s, mh, "TestClient");
		mr.start();
	}

	public static void main(String[] args) throws Exception {
		new TestClient();
	}
}
