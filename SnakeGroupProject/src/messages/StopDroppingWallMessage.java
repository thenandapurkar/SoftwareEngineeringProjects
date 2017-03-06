package messages;

public class StopDroppingWallMessage extends SnakeMessage{
	public StopDroppingWallMessage(String senderName){
		super(senderName,SnakeMessage.Type.StopDroppingWallMessage);
	}
}
