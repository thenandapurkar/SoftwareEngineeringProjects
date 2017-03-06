package messages;

public class ReceivedTickMessage extends SnakeMessage{
	public ReceivedTickMessage(String sendername){
		super(sendername,SnakeMessage.Type.ReceivedTickMessage);
	}
}
