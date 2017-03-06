package messages;

public class RespawnMessage extends SnakeMessage{
	public RespawnMessage(String senderName){
		super(senderName,SnakeMessage.Type.RespawnMessage);
	}
}
