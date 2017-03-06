package messages;

import messages.SnakeMessage.Type;

public class RegisterMessage extends SnakeMessage {
	
	private static final long serialVersionUID = 1L;
	public String username;
	public String password;
	public boolean valid = false;
	
	public RegisterMessage(String senderName, String username, String password) {
		super(senderName, Type.RegisterMessage);
		this.username = username;
		this.password = password;
	}
	
}
