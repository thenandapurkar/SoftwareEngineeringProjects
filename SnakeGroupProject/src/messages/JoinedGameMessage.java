package messages;

import messages.SnakeMessage.Type;

public class JoinedGameMessage extends SnakeMessage {
	
	private static final long serialVersionUID = 1L;
	
	public JoinedGameMessage(String senderName) {
		super(senderName, Type.JoinedGameMessage);
	}
	
}