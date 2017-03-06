package common;

import java.awt.Point;
import java.util.Random;

import powerups.LaserPowerUp;
import powerups.SpeedPowerUp;
import powerups.WallPowerUp;

//spawns things appropriately
public class Spawner {
	private int counter = 0;
	private int amt_food;
	private int amt_powerups;
	private Grid grid;
	private Random ran;

	public Spawner(Grid grid) {
		this.grid = grid;
		ran = new Random(System.currentTimeMillis());
		
		// constant amt of food to be on board
		amt_food = (grid.getWidth() * grid.getHeight()) / 20;
		amt_powerups = (grid.getWidth() * grid.getHeight()) / 50;

		//start grid w amt of food
		while (counter < amt_food) {
			tryToAdd();
			counter++;
		}
		
		counter = 0;
		while (counter < amt_powerups) {
			tryToAddPowerup();
			counter++;
		}
	}

	// when food's been taken, respawn it on the grid
	public void tryToAdd(){
		int x = ran.nextInt(grid.getWidth());
		int y = ran.nextInt(grid.getHeight());
		while (grid.get(new Point(x, y)) != null) {
			// occupied; try another random cell
			x = ran.nextInt(grid.getWidth());
			y = ran.nextInt(grid.getHeight());
		}
		new Food(grid, new Point(x, y)); //add food to location
	}
	
	public void tryToAddPowerup() {
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
}
