package messages;

import java.util.Map;

import common.*;

public class TickMessage extends SnakeMessage {
	
	private static final long serialVersionUID = 1L;
	
	public final Grid grid;
	
	public TickMessage(String senderName, Grid grid) { // sent server -> client
		super(senderName, SnakeMessage.Type.TickMessage);
		this.grid = grid; // lazy way to do networking: send entire grid
	}
	
}
