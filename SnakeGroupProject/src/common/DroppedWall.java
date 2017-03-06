package common;

import java.awt.Point;

public class DroppedWall extends Wall{
	private int timer;
	public DroppedWall(Grid inGrid, Point inLocation){
		super(inGrid,inLocation);
		timer = 600;
	}
	public void tickDown(){
		timer--;
		if (timer == 0){
			removeFromGrid();
		}
	}
	
	@Override
	public void tick(int counter) {
		tickDown();
	}
}
