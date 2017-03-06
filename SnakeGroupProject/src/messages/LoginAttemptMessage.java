package messages;

import java.util.ArrayList;

import common.Grid;

public class LoginAttemptMessage extends SnakeMessage {
	
	private static final long serialVersionUID = 1L;
	public String username;
	public String password;
	public boolean valid = false;
	public Grid grid;
	public ArrayList<String> users = new ArrayList<String>();
	
	public LoginAttemptMessage(String senderName, String username, String password) {
		super(senderName, Type.LoginAttemptMessage);
		this.username = username;
		this.password = password;
	}
	
}
