package powerups;

// Codes for LaserPowerUp by Steven. Ask Steven if have any question

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Entity;
import common.Grid;

public class SpeedPowerUp extends PowerUp{
	
	// constructor and set the instructions to use
	public SpeedPowerUp(Grid grid, Point Location){
		super(grid,Location);
		description = "Tap [space] to move twice the normal speed for a short period of time.";
		name = "Speed";
		try {
			setImage(ImageIO.read(new File("images/speedPowerUp.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} 
	}
	
	// method to handle when this powerup is activated by a snake
	public void trigger(){
		//snake.playSound("sound/speed.wav");
		//set the movement speed of the snake to the power up's speed (decrease update intervals)
		snake.speedBoostOn();
	}
	
	// method to process the event when it hits something
	// this method is a dummy method that never gets called
	// because powerUps don't move. It cannot actively hit something.
	public boolean hit(Entity other){
		return false;
	}
	
	public void loadImage(){
		try {
			setImage(ImageIO.read(new File("images/speedPowerUp.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} 
	}
}