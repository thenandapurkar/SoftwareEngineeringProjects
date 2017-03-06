package common;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//Permanent wall
public class Wall extends Entity {
	
	
	private static final long serialVersionUID = 1L;
	
	public Wall(Grid grid, Point point) {
		super(grid, point);
		
		addToGrid();
		
		//"TIMER" MOVED TO DroppedWall ; This is a permanent wall
		//timer (wall is temp)
		/*delay = 100000; // how long until wall is deleted
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run() {
				die(); //schedule to die after delay
			}
		}, delay);*/
	}

	@Override
	public boolean hit(Entity other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return image;
	}
	
	public void loadImage() {
		try {
			setImage(ImageIO.read(new File("images/wall.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
}
