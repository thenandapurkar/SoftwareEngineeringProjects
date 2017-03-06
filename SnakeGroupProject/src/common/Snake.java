package common;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import client.GameClientGUI;
import powerups.PowerUp;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Snake implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//last is the tail, first is the head
	private Deque<SnakeCell> cells = new LinkedList<SnakeCell>();
	private Direction direction;
	private PowerUp activePowerUp;
	private transient Image headImage;
	private transient Image tailImage;
	private Grid grid;
	public boolean ticked;
	// location of the last cell of the snake. Used to grow the snake when a food is eaten.
	private Point tailLocation;
	// ideally, this should be the username who controls this snake. Use this to tell which snake is whose
	private String snakeName;
	// member to keep track of whether the snake is dead.
	private boolean dead;
	// member to keep track of the position where the snake dies. Used for centering the GridPanel
	private Point deadPosition;
	// member to keep track of how much speed boost time is left for this snake
	private int speedBoost;
	// member to keep track of whether the snake is currently dropping obstacles
	private boolean droppingWall;
	//public SnakeClient client;
	
	private String color;
	
	private boolean usePowerUp;
	
	private Direction toChangeTo;
	
	private GameClientGUI gameClientGUI;
	
	private String eatSound;
	private String deathSound;
	
	// constructor
	public Snake(int len, Direction direction, Grid grid, Point location) {
		this.direction = direction;
		gameClientGUI = null;
		Direction opposite = direction.getOpposite();
		Point cell = location;
		this.grid = grid;
		for (int i = 0; i < len; i++) {
			cells.add(new SnakeCell(this, grid, cell));
			cell = opposite.moveFrom(cell);
		}
		cells.getFirst().setHead();
		tailLocation = cells.getLast().getLocation();
		dead = false;
		speedBoost = 0;
		droppingWall = false;
		try{
			headImage = ImageIO.read(new File("images/headUp.png"));
		} catch (IOException ioe){}
		ticked = false;
		usePowerUp = false;
		toChangeTo = this.direction;
		
		// SOUND FX
	    eatSound = "sound/eat.wav";
	    deathSound  = "sound/death.wav";
	}
	
	public void playSound(String s){
		InputStream in;
		AudioStream audioStream;
		try {
			// open sound file w input stream
			in = new FileInputStream(s);
			// make an audiostream from inputstream
			try {
				audioStream = new AudioStream(in);
				// play audio 
			    AudioPlayer.player.start(audioStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setGameClientGUI(GameClientGUI gameClientGUI){
		this.gameClientGUI = gameClientGUI;
	}
	
	public void setName(String inName){
		snakeName = inName;
	}
	
	public String getName(){
		return snakeName;
	}
	
	public Image getHeadImage() {
		if (headImage == null) {
			setHeadImage(direction.name());
		}
		return headImage;
	}
	
	public Image getTailImage() {
		if (tailImage == null) {
			try {
				tailImage = ImageIO.read(new File("images/"+color+".png"));
			} catch (IOException ioe) {}
		}
		return tailImage;
	}
	
	public Point getDeadLocation(){
		return deadPosition;
	}
	
	// method to change the color of the snake.
	// currently only supports "red" "blue" "green"
	public void setColor(String color) {
		this.color = color;
	}
	
	// get the position of the head of the snake. Used to determine where the laser spawns when a laser power up is used
	public Point getHeadPosition(){
		if (cells.size()!=0){
			SnakeCell head = cells.getFirst();
			return head.getLocation();
		}
		// return null when the snake is dead, i.e. size = 0
		else return null;
	}
	
	public void tick(int counter) {
		ticked = true;
		grid.getNamesAndScores().put(snakeName, cells.size());
		if (counter%2 == 1){
			// for every other tick, move the snakes that are on speed boost
			if (isSpeedBoosted()) {
				fireSetDirection();
				move();
				tickDownSpeedBoost();
			}
			// for every 4 ticks, move the snakes that are not on speed boost
			else if (counter%4 == 3){
				if (isDropping()){
					dropObstacle();
					Random ran = new Random();
					int x = ran.nextInt(grid.getWidth());
					int y = ran.nextInt(grid.getHeight());
					while (grid.get(new Point(x, y)) != null) {
						// occupied; try another random cell
						x = ran.nextInt(grid.getWidth());
						y = ran.nextInt(grid.getHeight());
					}
					new Food(grid,new Point(x,y));
				}
				fireSetDirection();
				move();
			}
		}
		if (usePowerUp){
			usePowerUp = false;
			if (activePowerUp!=null){
				activePowerUp.trigger();
				removePowerUp(); //consumed powerup
			}
		}
	}
	
	// method to move the snake.
	// takes the tail of the snake and moves it to head
	public void move() {
		if (!dead && cells.size()!=0){
			cells.getFirst().setTail();
			Point location = cells.getFirst().getLocation();
			SnakeCell head = cells.removeLast();
			head.moveTo(direction.moveFrom(location));
			// if snake didn't run into anything and die on the last line, keep moving the snake
			if (droppingWall){
				new DroppedWall(grid,tailLocation);
			}
			if (!dead){
				head.setHead();
				cells.addFirst(head);
				tailLocation=cells.getLast().getLocation();
			}
			
			// if it died, in die() method all cells except the last one will be turned into food
			// the last one was removed from cells before die() is called, so it does not get turned into food
			// so we turn it into food here
			else{
				Food newFood = new Food(grid,head.getLocation());
				head.removeFromGrid();
				newFood.addToGrid();
			}
		}
	}
	
	void setActivePowerUp(PowerUp powerup){
		activePowerUp = powerup;
	}
	
	// method to handle when the snake is being hit by a laser
	// Steven
	public void wasHit() {
		// is size is smaller than 6, snake dies
		if (cells.size()<=6){
			die();
		}
		// else remove 6 cells from tail and turn them into food
		else {
			for (int i=0;i<6;i++){
				Grid grid = cells.getFirst().getGrid();
				SnakeCell toRemove = cells.removeLast();
				toRemove.removeFromGrid();
				Food newFood = new Food(grid,toRemove.getLocation());
				//TODO: set Image of food
				newFood.addToGrid();
			}
		}
	}
	
	
	// method to drop the snake's 1 tail cell as obstacle
	public void dropObstacle(){
		if (cells.size()>2){
			SnakeCell toDrop = cells.removeLast();
			tailLocation = cells.getLast().getLocation();
			toDrop.removeFromGrid();
			DroppedWall newWall = new DroppedWall(toDrop.getGrid(),toDrop.getLocation());
			newWall.addToGrid();
		}
		else
			droppingWall = false;
	}
	
	public Direction getDirection(){
		return direction;
	}
	
	public void setDirection(Direction d){
		toChangeTo = d;
	}
	
	public void fireSetDirection() {
		if (toChangeTo != direction.getOpposite()){
			direction = toChangeTo;
			
			// changes headImage to the corresponding direction
			if (toChangeTo == Direction.UP)
				setHeadImage("Up");
			else if (toChangeTo == Direction.DOWN)
				setHeadImage("Down");
			else if (toChangeTo == Direction.LEFT)
				setHeadImage("Left");
			else
				setHeadImage("Right");
		}
	}
	
	// method to handle when a snake dies
	public void die(){
		//playSound(deathSound);
		// TODO: client server interaction not implemented yet 
		dead = true;
//		DeadMessage message = new DeadMessage(client.getUsername());
//		client.sendMessage(message);
		
		deadPosition = cells.getFirst().getLocation();
		// remove the snake from grid, then add food
		for (int i = 0; i < cells.size(); i++) {
			SnakeCell temp = ((LinkedList<SnakeCell>) cells).get(i);
			Food newFood = new Food(grid,temp.getLocation());
			temp.removeFromGrid();
			newFood.addToGrid();
		}
		//new DeadGUI().setVisible(true);
		cells = new LinkedList<SnakeCell>();
		
		/*if (client != null){
			if (client.getUsername().trim().equals(snakeName.trim())){
				new DeadGUI().setVisible(true);
			}
		}*/
	}
	
	public boolean hasPowerUp(){
		return activePowerUp!=null;
	}
	
	
	public void usePowerUp(){
		usePowerUp = true;
	}
	
	public void removePowerUp(){
		activePowerUp = null;
	}
	
	// turn on speed boost for the snake
	// gets called in the trigger() method of SpeedPowerUp
	public void speedBoostOn(){
		// sets the remaining speed boost time to full
		// actual active time is related to the tick rate, set in GameThread
		// speedBoost * tick rate /2 will give you the actual time
		speedBoost = 60;
	}
	
	// turn on obstacle drop for the snake
	// gets called in the trigger() method of WallPowerUp
	public void startDropping(){
		droppingWall = true;
	}
	
	// turn off obstacle drop for the snake
	public void stopDropping(){
		droppingWall = false;
	}
	
	public boolean isDropping(){
		return droppingWall;
	}
	
	// tick down the remaining speed boost time
	// gets called in GameThread when a speed boosted snake is moved
	public void tickDownSpeedBoost(){
		if (speedBoost!=0)
			speedBoost--;
	}
	
	// returns whether a snake has speed boost activated
	public boolean isSpeedBoosted(){
		return (speedBoost > 0);
	}
	
	public PowerUp getActivePowerUp(){
		return activePowerUp;
	}
	
	public void setHeadImage(String direction){
		try{
			headImage = ImageIO.read(new File("images/head"+direction+".png"));
		} catch (IOException ioe){}
	}
	
	
	// method to handle when a snake eats food
	// adds 1 cell to the end of the snake after next movement
	public void ateFood(){
		SnakeCell toAdd = new SnakeCell(this,grid,tailLocation);
		cells.addLast(toAdd);
		toAdd.addToGrid();
		//playSound(eatSound);
	}
}
