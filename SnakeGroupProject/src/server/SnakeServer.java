package server;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import common.Direction;
import common.Grid;
import common.Snake;
import messages.*;

public class SnakeServer extends Thread {
	private List<ServerThread> serverThreads;
	private ServerSocket ss;
	private int port;
	// the grid of the game, being run on the server
	public Grid gameGrid;
	// the new GameThread class is modified for server to run
	private GameThread gameThread;
	//private Vector<String> teamNames = new Vector<String>();
	public ArrayList<String> users = new ArrayList<String>();
	private volatile int receivedCounter;
	public volatile boolean allReceived;
	private volatile int readyToPlay;
	
	
	public SnakeServer(int port) {
		serverThreads = Collections.synchronizedList(new ArrayList<ServerThread>());
		this.port = port;
		try {
			System.out.println("PORT: " + port);
			ss = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ioe - Server 1" + e.getMessage());
		}
		gameGrid = new Grid(new Dimension(50,50));
		gameThread = new GameThread(this);
		receivedCounter = 0;
		allReceived = false;
	}
	
	synchronized public void removeServerThread(ServerThread st){
		readyToPlay --;
		if (readyToPlay == receivedCounter)
			allReceived = true;
		serverThreads.remove(st);
	}
	
	public void run() {
		try {
			while (true) {
				//waiting for people to join server
				//System.out.println("waiting for connection...");
				Socket s = ss.accept();
				System.out.println("connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe - server: " + ioe.getMessage());
		}
	}
	
	
	// method to notify all clients to tick
	// sends an entire Grid class through oos
	synchronized public void sendTick() {
		TickMessage message = new TickMessage("", gameGrid);
		synchronized (serverThreads) {
			for (ServerThread st : serverThreads) {
				st.sendMessage(message);
			}
		}
		allReceived = false;
		receivedCounter = 0;
	}
	
	synchronized public void parseMessage(SnakeMessage message) {
		//System.out.println("Server threads count: " + serverThreads.size());
		
		if (message instanceof LoginAttemptMessage) {
			System.out.println("LoginAttemptMessage");
			LoginAttemptMessage lMessage = (LoginAttemptMessage)message;
			MySQLDriver sqlDriver = new MySQLDriver();
			sqlDriver.connect();
			if (sqlDriver.userExist(lMessage.username)) {
				if (sqlDriver.validLogin(lMessage.username, lMessage.password)) {
					lMessage.valid = true;
					/*Snake snake = new Snake(3, Direction.DOWN, gameGrid, new Point(5,5));
					snake.setName(lMessage.username);
					snake.setColor("red");*/
					gameGrid.spawnSnake(lMessage.username);
					lMessage.grid = gameGrid;
					users.add(lMessage.username);
					//lMessage.users = users;
				}
				else {
					lMessage.valid = false;
					lMessage.grid = null;
				}
			}
		}
		else if (message instanceof RegisterMessage) {
			System.out.println("RegisterMessage");
			RegisterMessage rMessage = (RegisterMessage)message;
			MySQLDriver sqlDriver = new MySQLDriver();
			sqlDriver.connect();
			if (sqlDriver.userExist(rMessage.username)) {
				rMessage.valid = false;
			}
			else {
				sqlDriver.add(rMessage.username, rMessage.password, "red");
				rMessage.valid = true;
			}
		}
		else if (message instanceof GuestMessage) {
			System.out.println("GuestMessage");
			GuestMessage gm = (GuestMessage)message;
			String guest = "Player " + users.size();
			gm.username = guest;
			gameGrid.spawnSnake(gm.username);
			gm.grid = gameGrid;
			users.add(gm.username);
			//gm.users = users;
		}
		else if (message instanceof SteerMessage) {
			Snake s = gameGrid.getSnake(message.senderName);
			if (s!=null)
				s.setDirection(((SteerMessage)message).direction);
		}
		else if (message instanceof ActivatePowerUpMessage){
			Snake s = gameGrid.getSnake(message.senderName);
			if (s!=null)
				s.usePowerUp();
		}
		else if (message instanceof StopDroppingWallMessage){
			Snake s = gameGrid.getSnake(message.senderName);
			if (s!=null)
				s.stopDropping();
		}
		else if (message instanceof ReadyMessage){
			readyToPlay++;
		}
		else if (message instanceof ReceivedTickMessage){
			receivedCounter++;
			//System.out.println(receivedCounter);
			if (receivedCounter >= readyToPlay){
				//receivedCounter = 0;
				allReceived = true;
			}
		}
		else if (message instanceof RespawnMessage){
			System.out.println("Received Respawn Message");
			gameGrid.spawnSnake(((RespawnMessage)message).senderName);
		}
		
		if (message instanceof LoginAttemptMessage || message instanceof RegisterMessage || message instanceof RespawnMessage || message instanceof GuestMessage){
			for (ServerThread st : serverThreads) {
				System.out.println("Sending message through thread");
				st.sendMessage(message);
			}
		}
		
		// if users have connected and the game has not started yet, start the game
		if ((!gameThread.isAlive()) && (readyToPlay!=0)){
			gameThread.start();
			sendTick();
		}
	}
	
	public Map<String, Integer> getTeamNamesAndScores() {
		return gameGrid.getNamesAndScores();
	}
}