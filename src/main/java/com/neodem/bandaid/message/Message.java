package com.neodem.bandaid.message;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Message HELLO_MESSAGE = new Message(MessageType.HELLO, "Hello");
	public static final Message BYE_MESSAGE = new Message(MessageType.DISCONNECT, "Bye");

	public enum MessageType {
		DISCONNECT, TEXT, OBJECT, HELLO
	};

	private MessageType messageType;
	private Serializable body;

	public Message(MessageType messageType, Serializable body) {
		super();
		this.messageType = messageType;
		this.body = body;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Serializable getBody() {
		return body;
	}
}
