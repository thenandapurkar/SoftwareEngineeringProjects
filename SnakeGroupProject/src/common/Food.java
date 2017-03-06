package common;
// class for food
// Steven

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import powerups.LaserPowerUp;
import powerups.SpeedPowerUp;
import powerups.WallPowerUp;

public class Food extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// constructor
	public Food(Grid grid, Point location){
		super(grid,location);
		
	}
	// method for when food hits something
	// dummy method because a food never actively hits something
	public boolean hit(Entity other){
		return false;
	}
	
	// method for setting image as food
	public void loadImage(){
		try {
			setImage(ImageIO.read(new File("images/food.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
}
