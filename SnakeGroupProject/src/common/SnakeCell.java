package common;

import java.awt.Image;
import java.awt.Point;

import powerups.PowerUp;

public class SnakeCell extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isHead = false;
	private Snake snake;
	
	public void setHead() {
		isHead = true;
	}
	
	public void setTail() {
		isHead = false;
	}
	
	public Snake getSnake(){
		return snake;
	}
	
	public void setLocation(Point p) {
		location = p;
	}
	
	public boolean isHead() {
		return isHead;
	}
	
	public boolean hit(Entity other) {
		if (other instanceof SnakeCell) {
			//it is another snake our our own self
			//locally we only care if its our own... if someone else hits us they handle it
			//SnakeCell sc = (SnakeCell)other;
			//if (sc.snake == snake) {
				//it's us
				snake.die();
				return false;
			//}
		}
		else if (other instanceof Wall) {
			snake.die();
			return false;
		}
		else if (other instanceof Food) {
			//System.out.println("Location of food: " + other.location.x + ", " + other.location.y);
			other.removeFromGrid();
			snake.ateFood();
			return true;
		}
		else if (other instanceof PowerUp) {
			snake.setActivePowerUp((PowerUp)other);
			((PowerUp)other).setSnake(snake);
			other.removeFromGrid();
			return true;
		}
		else if (other instanceof Laser){
			snake.die();
			return false;
		}
		
		//hitting laser is handled by Laser class / wasHit
		return false;//todo
	}
	
	@Override
	public void tick(int counter) {
		if (isHead() && snake.ticked==false) {
			snake.tick(counter);
			snake.ticked = true;
		}
	}
	
	@Override
	public Image getImage() {
		return isHead? snake.getHeadImage() : snake.getTailImage();
	}
	
	public SnakeCell(Snake snake, Grid grid, Point location) {
		super(grid, location);
		this.snake = snake;
	}
	
	public void loadImage(){}
}
