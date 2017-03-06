package common;

// class for laser
// Steven

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import powerups.PowerUp;

public class Laser extends Entity {
	private static final long serialVersionUID = 1L;
	private Direction direction; // the direction where the laser is fired at
	
	// constructor
	public Laser(Grid grid, Point location, Direction inDirection){
		super(grid,location);
		direction = inDirection;
	}
	
	@Override
	public void tick(int counter) {
		moveTo(direction.moveFrom(getLocation()));
	}
	
	// method to handle when a laser hits something
	public boolean hit(Entity other){
		// if hits a wall, do nothing. Remove laser from grid.
		if (other instanceof Wall){
			removeFromGrid();
		}
		// if hits another laser, two lasers clash and both disappear
		else if (other instanceof Laser){
			other.removeFromGrid();
			removeFromGrid();
		}
		// if hits a powerUp, destroy that powerUp and keep going
		else if (other instanceof PowerUp){
			other.removeFromGrid();
			return true;
		}
		// if hits a snake, let that snake lose length and turn into food
		else if (other instanceof SnakeCell){
			((SnakeCell) other).getSnake().wasHit();
			removeFromGrid();
		}
		// if hits a food, destroy that powerUp and keep going
		else if (other instanceof Food){
			other.removeFromGrid();
			Random ran = new Random(System.currentTimeMillis());
			int x = ran.nextInt(grid.getWidth());
			int y = ran.nextInt(grid.getHeight());
			while (grid.get(new Point(x, y)) != null) {
				// occupied; try another random cell
				x = ran.nextInt(grid.getWidth());
				y = ran.nextInt(grid.getHeight());
			}
			new Food(grid,new Point(x,y));
			return true;
		}
		return false;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void loadImage(){
		if ((direction == Direction.UP) || (direction == Direction.DOWN)){
			try {
				setImage(ImageIO.read(new File("images/laserVertical.png")));
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
		else{
			try {
				setImage(ImageIO.read(new File("images/laserHorizontal.png")));
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
	}
}
