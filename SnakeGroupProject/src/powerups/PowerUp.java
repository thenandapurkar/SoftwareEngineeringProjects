package powerups;

import java.awt.Point;
import java.util.Random;

import common.Entity;
import common.Grid;
import common.Snake;

public abstract class PowerUp extends Entity {
	protected String description; // includes desc + how to use
	protected String name; // name of powerup
	protected Snake snake; // the snake that owns this powerUp. null if it is not picked up yet.

	public PowerUp(Grid grid, Point location) {
		super(grid, location);
		snake = null;

	}

	public String getDescription() {
		return description;
	}
	
	public void setSnake(Snake inSnake){
		snake = inSnake;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public void removeFromGrid(){
		super.removeFromGrid();
		Random ran = new Random(System.currentTimeMillis());
		int x = ran.nextInt(grid.getWidth());
		int y = ran.nextInt(grid.getHeight());
		while (grid.get(new Point(x, y)) != null) {
			// occupied; try another random cell
			x = ran.nextInt(grid.getWidth());
			y = ran.nextInt(grid.getHeight());
		}
		
		int i = ran.nextInt(3);
		if (i == 0) {
			new WallPowerUp(grid, new Point(x, y));
		}
		else if (i == 1) {
			new LaserPowerUp(grid, new Point(x, y));
		}
		else {
			new SpeedPowerUp(grid, new Point(x, y));
		}
	}

	public abstract void trigger();
}
