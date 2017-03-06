package messages;

import java.util.ArrayList;

import common.Grid;

public class GuestMessage extends SnakeMessage {
	
	private static final long serialVersionUID = 1L;
	public String username;
	public boolean valid = false;
	public Grid grid;
	public ArrayList<String> users = new ArrayList<String>();
	
	public GuestMessage(String senderName, String username) {
		super(senderName, Type.GuestMessage);
		this.username = username;
	}
	
}
