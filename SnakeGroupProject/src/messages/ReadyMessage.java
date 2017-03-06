package messages;

public class ReadyMessage extends SnakeMessage{
	public ReadyMessage(String senderName){
		super(senderName,SnakeMessage.Type.ReadyMessage);
	}
}
