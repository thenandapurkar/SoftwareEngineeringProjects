package messages;

public class ActivatePowerUpMessage extends SnakeMessage{
	public ActivatePowerUpMessage(String senderName){
		super(senderName,SnakeMessage.Type.ActivatePowerUpMessage);
	}
}
