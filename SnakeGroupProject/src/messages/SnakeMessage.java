package messages;

import java.io.Serializable;

public class SnakeMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public String senderName;
	public enum Type {	LoginAttemptMessage, RegisterMessage, JoinedGameMessage, TickMessage, SteerMessage, ActivatePowerUpMessage, StopDroppingWallMessage, 
						DeadMessage, ReceivedTickMessage, RespawnMessage, ReadyMessage, GuestMessage};
	public Type type;
	
	public SnakeMessage(String senderName, Type type) {
		this.senderName = senderName;
		this.type = type;
	}
}
