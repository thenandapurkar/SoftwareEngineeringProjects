package common;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import client.GridPanel;

public class Grid implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final int cell_size = 30;
	
	private final Dimension size;
	private final Entity[] cells;
	private int counter = 0;
	
	private final Map<String, Integer> namesAndScores;
	
	private int getIndex(Point location) {
		return location.y * size.width + location.x;
	}
	
	private void loadImage(){
		for (int i=0;i<size.width*size.height;i++){
			cells[i].loadImage();
		}
	}
	
	// method to get the snake with a given username
	// used to determine which snake the client controls
	public Snake getSnake(String name){
		for (int i=0;i<size.width*size.height;i++){
			if (cells[i] instanceof SnakeCell){
				SnakeCell temp = (SnakeCell)cells[i];
				if (temp.isHead()){
					Snake tempSnake = temp.getSnake();
					if (tempSnake.getName().trim().equals(name.trim())){
						return tempSnake;
					}
				}
			}
		}
		return null;
	}
	
	public void spawnSnake(String snakeName){
		Random ran = new Random(System.currentTimeMillis());
		boolean validSpawn = false;
		int x=0;
		int y=0;
		while (!validSpawn){
			validSpawn = true;
			x = ran.nextInt(getWidth());
			y = ran.nextInt(getHeight());
			if (y<5){
				validSpawn = false;
				continue;
			}
			if (y>=getHeight()-5){
				validSpawn =false;
				continue;
			}
			for (int i=0;i<5;i++){
				if (get(new Point(x,i)) instanceof DroppedWall || get(new Point(x,i)) instanceof SnakeCell){
					validSpawn = false;
					continue;
				}
			}
			for (int i=0;i<3;i++){
				if (get(new Point(x,y+i))!=null){
					validSpawn = false;
					continue;
				}
			}
		}
		Snake spawned = new Snake(3,Direction.UP,this,new Point(x,y));
		spawned.setName(snakeName);
		if (snakeName.startsWith("Guest"))
			spawned.setColor("grey");
		else{
			int color = ran.nextInt(4);
			if (color == 0)
				spawned.setColor("red");
			else if (color == 1)
				spawned.setColor("blue");
			else if (color == 2)
				spawned.setColor("green");
			else if (color == 3)
				spawned.setColor("purple");
		}
	}
	
	public void set(Point location, Entity entity) {
		cells[getIndex(location)] = entity;
	}
	
	public Entity get(Point location) {
		return cells[getIndex(location)];
	}
	
	public int getWidth() {
		return size.width;
	}
	
	public int getHeight() {
		return size.height;
	}
	
	// modified this method from Myrl's original implementation
	synchronized public void tick() {
		namesAndScores.clear();
		Vector<Entity> temp = new Vector<Entity>();
		
		// you cannot directly tick in cells, because the contents of cells itself is being updated by the tick method,
		// as cells also represents the layout of the game board.
		
		// instead, make reference to the elements that needs to tick, and iterate through that
		for (Entity entity : cells) {
			if (entity != null) {
				temp.add(entity);
			}
		}
		
		for (Entity entity : temp){
			entity.tick(counter);
		}
		
		for (Entity entity : temp){
			if (entity instanceof SnakeCell){
				((SnakeCell)entity).getSnake().ticked=false;
			}
		}
		
		counter++;
		if (counter==4){
			counter=0;
		}
	}
	
	public Map<String, Integer> getNamesAndScores() {
		return namesAndScores;
	}
	
	public Grid(Dimension size) {
		this.size = size;
		int total = size.width * size.height;
		namesAndScores = new HashMap<String, Integer>();
		cells = new Entity[total];
		for (int i=0;i<size.getWidth();i++){
			Wall temp = new Wall(this,new Point(i,0));
			temp.addToGrid();
			temp = new Wall(this, new Point(i,(int)size.getHeight()-1));
			temp.addToGrid();
		}
		
		for (int i=1;i<size.getHeight()-1;i++){
			Wall temp = new Wall(this,new Point(0,i));
			temp.addToGrid();
			temp = new Wall(this, new Point((int)size.getHeight()-1,i));
			temp.addToGrid();
		}
		new Spawner(this);
	}
}
