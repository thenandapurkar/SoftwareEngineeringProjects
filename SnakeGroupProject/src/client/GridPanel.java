package client;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

import common.*;
import messages.ActivatePowerUpMessage;
import messages.SteerMessage;
import messages.StopDroppingWallMessage;
import powerups.LaserPowerUp;
import powerups.SpeedPowerUp;
import powerups.WallPowerUp;
import server.SnakeClient;

public class GridPanel extends JPanel {	
	private Grid grid;
	private Snake mySnake;
	private String myName;
	private SnakeClient mClient;
	private BufferedImage food;
	private BufferedImage laserHorizontal;
	private BufferedImage laserPowerUp;
	private BufferedImage laserVertical;
	private BufferedImage speedPowerUp;
	private BufferedImage wall;
	private BufferedImage wallPowerUp;
	private BufferedImage buffer;
	private ScorePanel scorePanel;
	
	// constructor
	public GridPanel(Grid grid, SnakeClient inClient) {
		super();
		mClient = inClient;
		setPreferredSize(new Dimension(
				grid.getWidth() * Grid.cell_size,
				grid.getHeight() * Grid.cell_size
			));
		this.grid = grid;
		setFocusable(true);
		requestFocusInWindow();
		addListeners();
		this.setBackground(Color.WHITE);
		try{
			food = ImageIO.read(new File("images/food.png"));
			laserHorizontal = ImageIO.read(new File("images/laserHorizontal.png"));
			laserPowerUp = ImageIO.read(new File("images/laserPowerUp.png"));
			laserVertical = ImageIO.read(new File("images/laserVertical.png"));
			speedPowerUp = ImageIO.read(new File("images/speedPowerUp.png"));
			wall = ImageIO.read(new File("images/wall.png"));
			wallPowerUp = ImageIO.read(new File("images/wallPowerUp.png"));
			food = toCompatible(food);
			laserHorizontal = toCompatible(laserHorizontal);
			laserPowerUp = toCompatible(laserPowerUp);
			laserVertical = toCompatible(laserVertical);
			speedPowerUp = toCompatible(speedPowerUp);
			wall = toCompatible(wall);
			wallPowerUp = toCompatible(wallPowerUp);
			
		} catch (IOException ioe){}
	}
	
	public void setScorePanel(ScorePanel scorePanel) {
		this.scorePanel = scorePanel;
	}
	
	
	// method to set the grid member
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public void loadImage(){
		for (int i=0;i<grid.getHeight();i++){
			for (int j=0;j<grid.getWidth();j++){
				if (grid.get(new Point(i,j))!=null){
					grid.get(new Point(i,j)).loadImage();
				}
			}
		}
	}
	
	// method to move all lasers that has been fired. This method is not used anymore.
	// Steven
	/*
	public void updateNonPlayerObjects(){
		Vector<Laser> lasers = new Vector<Laser>();
		for (int i=0;i<grid.getHeight();i++){
			for (int j=0;j<grid.getWidth();j++){
				Entity temp = grid.get(new Point(i,j));
				if (temp instanceof Laser){
					lasers.add((Laser)temp);
				}
				else if (temp instanceof DroppedWall){
					((DroppedWall)temp).tickDown();
				}
			}
		}
		for (Laser laser:lasers){
			laser.moveTo(laser.getDirection().moveFrom(laser.getLocation()));
		}
	}*/
	
	// method to set the snake that the user controls
	public void setMySnake(Snake inSnake){
		mySnake = inSnake;
	}
	
	// method to refresh the panel after the grid has been changed
	public void refreshGUI(){
		scorePanel.refreshGUI();
		Graphics g = getGraphics();
		if (g != null) {
			paintComponent(g);
		}
		else {
			repaint();
		}
	}
	
	// method to tick the grid
	public void tick(){
		grid.tick();
	}
	
	
	// override paintComponent method.
	// now the gameboard draws consistently a 20x20 grid, and scales with the window
	@Override
	public void paintComponent(Graphics g) {
			GraphicsConfiguration gfx_config = GraphicsEnvironment.
			        getLocalGraphicsEnvironment().getDefaultScreenDevice().
			        getDefaultConfiguration();
			buffer = gfx_config.createCompatibleImage(getWidth()-getWidth()%20,
		            getHeight()-getHeight()%20);
			Graphics2D g2d = (Graphics2D)buffer.getGraphics();
			
			//g2d.clearRect(0, 0, getWidth(), getHeight());
			//super.paintComponent(g);
			Point snakePosition = mySnake.getHeadPosition();
			if (snakePosition == null)
				snakePosition = mySnake.getDeadLocation();
			int left = (int)snakePosition.getX()-10;
			int right = (int)snakePosition.getX()+10;
			int up = (int)snakePosition.getY()-10;
			int down = (int)snakePosition.getY()+10;
			if (left < 0){
				right = right - left;
				left = 0;
			}
			if (right >= grid.getWidth()){
				right = grid.getWidth();
				left = right - 20;
			}
			if (up < 0){
				down = down - up;
				up = 0;
			}
			if (down >= grid.getHeight()){
				down = grid.getHeight();
				up = down - 20;
			}

			int cellsizeX = (int)this.getSize().getWidth()/20;
			int cellsizeY = (int)this.getSize().getHeight()/20;
			
			
			for (int x = left; x < right; x++) {
				for (int y = up; y < down; y++) {
					Entity entity = grid.get(new Point(x, y));
					if (entity != null) {
						if (entity instanceof Wall){
							g2d.drawImage(
									wall,
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
						else if (entity instanceof Food){
							g2d.drawImage(
									food,
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
						else if (entity instanceof WallPowerUp){
							g2d.drawImage(
									wallPowerUp,
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
						else if (entity instanceof LaserPowerUp){
							g2d.drawImage(
									laserPowerUp,
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
						else if (entity instanceof SpeedPowerUp){
							g2d.drawImage(
									speedPowerUp,
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
						else if (entity instanceof DroppedWall){
							g2d.drawImage(
									wall,
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
						else if (entity instanceof Laser){
							if (((Laser)entity).getDirection() == Direction.LEFT || ((Laser)entity).getDirection()==Direction.RIGHT){
								g2d.drawImage(
										laserHorizontal,
										(x-left) * cellsizeX, (y-up) * cellsizeY,
										cellsizeX, cellsizeY, null
									);
							}
							else{
								g2d.drawImage(
										laserVertical,
										(x-left) * cellsizeX, (y-up) * cellsizeY,
										cellsizeX, cellsizeY, null
									);
							}
						}
						else if (entity instanceof SnakeCell){
							g2d.drawImage(
									entity.getImage(),
									(x-left) * cellsizeX, (y-up) * cellsizeY,
									cellsizeX, cellsizeY, null
								);
						}
					}
					else {
						g2d.setColor(Color.WHITE);
						g2d.fillRect(
								(x-left) * cellsizeX, (y-up) * cellsizeY,
								cellsizeX, cellsizeY
							);
					}
				}
			}
			
			g.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
	}
	
	private BufferedImage toCompatible(BufferedImage image){
		GraphicsConfiguration gfx_config = GraphicsEnvironment.
		        getLocalGraphicsEnvironment().getDefaultScreenDevice().
		        getDefaultConfiguration();
		if (image.getColorModel().equals(gfx_config.getColorModel()))
	        return image;
		BufferedImage new_image = gfx_config.createCompatibleImage(
	            image.getWidth(), image.getHeight(), image.getTransparency());
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return new_image; 
	}
	
	// method to add key listeners
	// TODO: modify for server-client communication
	private void addListeners() {
		addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'w') {
					if (mySnake.getDirection() != Direction.DOWN) {
						/*mySnake.setDirection(Direction.UP);
						//snake.move();
						Graphics g = getGraphics();
						if (g != null) {
							paintComponent(g);
						}
						else {
							repaint();
						}*/
						mClient.sendMessage(new SteerMessage(mySnake.getName(),Direction.UP));
					}
				}
				else if (e.getKeyChar() == 'd') {
					//System.out.println("Key: " + e.getKeyChar());
					/*if (mySnake.getDirection() != Direction.LEFT) {
						mySnake.setDirection(Direction.RIGHT);
						//mySnake.move();
						Graphics g = getGraphics();
						if (g != null) {
							paintComponent(g);
						}
						else {
							repaint();
						}
					}*/
					mClient.sendMessage(new SteerMessage(mySnake.getName(),Direction.RIGHT));
				}
				else if (e.getKeyChar() == 'a') {
					//System.out.println("Key: " + e.getKeyChar());
					/*if (mySnake.getDirection() != Direction.RIGHT) {
						mySnake.setDirection(Direction.LEFT);
						//mySnake.move();
						Graphics g = getGraphics();
						if (g != null) {
							paintComponent(g);
						}
						else {
							repaint();
						}
					}*/
					mClient.sendMessage(new SteerMessage(mySnake.getName(),Direction.LEFT));
				}
				else if (e.getKeyChar() == 's') {
					/*if (mySnake.getDirection() != Direction.UP) {
						mySnake.setDirection(Direction.DOWN);
						//mySnake.move();
						Graphics g = getGraphics();
						if (g != null) {
							paintComponent(g);
						}
						else {
							repaint();
						}
					}*/
					mClient.sendMessage(new SteerMessage(mySnake.getName(),Direction.DOWN));
				}
				else if (e.getKeyChar() == ' ') {
					//mySnake.usePowerUp();
					mClient.sendMessage(new ActivatePowerUpMessage(mySnake.getName()));
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					//mySnake.stopDropping();
					mClient.sendMessage(new StopDroppingWallMessage(mySnake.getName()));
				}
			}
		});
	}
}
