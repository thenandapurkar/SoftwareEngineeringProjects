package common;

import java.awt.*;
import java.io.Serializable;

public abstract class Entity implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	protected Point location;
	protected Grid grid;
	protected transient Image image;
	
	public Point getLocation() {
		return location;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public void moveTo(Point location) {
		Entity other = grid.get(location);
		if (other != null) {
			if (!hit(other)) return;
		}
		removeFromGrid();
		this.location = location;
		addToGrid();
	}
	
	public void addToGrid() {
		grid.set(location, this);
	}
	
	public void removeFromGrid() {
		grid.set(location, null);
	}
	
	public Entity(Grid grid, Point location) {
		this.grid = grid;
		this.location = location;
		addToGrid();
	}
	
	//commonly overridden methods below
	
	public void tick(int counter) {
		
	}
	
	public boolean hit(Entity other) {
		return true;
	}

	public Image getImage() {
		if (image == null) {
			loadImage();
		}
		return image;
	}
	
	abstract public void loadImage();
}
