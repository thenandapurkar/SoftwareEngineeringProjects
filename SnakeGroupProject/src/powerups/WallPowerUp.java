package powerups;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Entity;
import common.Grid;

// code for wall power up
// Steven
public class WallPowerUp extends PowerUp {
	public WallPowerUp(Grid grid, Point location) {
		super(grid, location);
		description = "Hold [space] to drop temporary walls. Your snake will be slowly consumed.";
		name = "Wall";
		try {
			setImage(ImageIO.read(new File("images/wallPowerUp.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} 
	}

	// method to drop a wall obstacle
	public void trigger() {
		//snake.playSound("sound/wall.wav");
		snake.startDropping();
	}

	// method for when a wall power up hits something
	// dummy method because power ups cannot actively hit anything
	public boolean hit(Entity other) {
		return false;
	}
	
	public void loadImage(){
		try {
			setImage(ImageIO.read(new File("images/wallPowerUp.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} 
	}
}
