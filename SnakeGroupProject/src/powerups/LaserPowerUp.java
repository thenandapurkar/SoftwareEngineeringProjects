package powerups;

// Codes for LaserPowerUp by Steven. Ask Steven if have any question

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Direction;
import common.Entity;
import common.Food;
import common.Grid;
import common.Laser;
import common.SnakeCell;

public class LaserPowerUp extends PowerUp{
	
	// constructor and set the instructions to use
	public LaserPowerUp(Grid grid, Point Location){
		super(grid,Location);
		description = "Tap [space] to fire a laser that can damage enemy snakes.";
		name = "Laser";
		try {
			setImage(ImageIO.read(new File("images/laserPowerUp.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	
	// method to do stuff when this powerup is being activated by user
	public void trigger(){
		//snake.playSound("sound/laser.wav");
		Entity front = grid.get(snake.getDirection().moveFrom(snake.getHeadPosition()));
		if (front != null){
			if (front instanceof SnakeCell){
				((SnakeCell) front).getSnake().wasHit();
			}
			else if (front instanceof PowerUp || front instanceof Food){
				front.removeFromGrid();
				Laser newLaser = new Laser(grid,snake.getDirection().moveFrom(snake.getHeadPosition()),snake.getDirection());
				newLaser.addToGrid();
				newLaser.moveTo(newLaser.getDirection().moveFrom(newLaser.getLocation()));
			}
		}
		else{
			Laser newLaser = new Laser(grid,snake.getDirection().moveFrom(snake.getHeadPosition()),snake.getDirection());
			newLaser.addToGrid();
			newLaser.moveTo(newLaser.getDirection().moveFrom(newLaser.getLocation()));
		}
	}
	
	// method to process the event when it hits something
	// this method is a dummy method that never gets called
	// because powerUps don't move. It cannot actively hit something.
	public boolean hit(Entity other){
		return false;
	}
	
	public void loadImage(){
		try {
			setImage(ImageIO.read(new File("images/laserPowerUp.png")));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
}
