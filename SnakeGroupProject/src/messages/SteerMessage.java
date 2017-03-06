package messages;

import common.*;

public class SteerMessage extends SnakeMessage {
	
	private static final long serialVersionUID = 1L;
	
	public final Direction direction;
	
	public SteerMessage(String senderName, Direction direction) {  // sent client -> server
		super(senderName, SnakeMessage.Type.SteerMessage);
		this.direction = direction;
	}
	
}
