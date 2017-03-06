package server;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import messages.LoginAttemptMessage;
import messages.ReadyMessage;
import messages.ReceivedTickMessage;
import messages.RegisterMessage;
import client.DeadGUI;
import client.GameClientGUI;
import client.GridPanel;
import client.LoginGUI;
import client.LoginPanel;
import common.Snake;
import messages.*;
import messages.SnakeMessage.Type;

public class SnakeClient extends Thread implements Serializable {
	private static final long serialVersionUID = 1L;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private SnakeMessage message;
	private Socket s = null;
	private LoginPanel loginPanel;
	private String username = null;
	private GameClientGUI gameClientGUI;
	private GridPanel gridPanel;
	public boolean dead;
	private DeadGUI deadGUI;
	private BufferedOutputStream bos;
	
	
	public SnakeClient(String hostname, int port) {
		//System.out.println("Client Constructor");
		//Scanner scan = null;
		try {
			//System.out.println("1");
			s = new Socket(hostname, port);
			//System.out.println("2");
			System.out.println("Is socket s closed (client): " + s.isClosed());
			//oos = new ObjectOutputStream(s.getOutputStream());
			//ois = new ObjectInputStream(s.getInputStream());
			bos = new BufferedOutputStream(s.getOutputStream());
			oos = new ObjectOutputStream(bos);
			ois = new ObjectInputStream(
					new BufferedInputStream(s.getInputStream()));
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe - client1: " + ioe.getMessage());
		} 
		dead = false;
	}
	
	
	public void setLoginPanel(LoginPanel inLoginPanel){
		loginPanel = inLoginPanel;
	}
	
	public void run() {
		
		try {
			while(true) {
				//reading in the messages
				SnakeMessage message = (SnakeMessage)ois.readObject();
				
				if (message.type == Type.LoginAttemptMessage) {
					if (((LoginAttemptMessage)message).valid) {
						if (username == null) {
							loginPanel.loggedIn(((LoginAttemptMessage)message).username, ((LoginAttemptMessage)message).grid, ((LoginAttemptMessage)message).users);
							username = ((LoginAttemptMessage)message).username;
							gridPanel = new GridPanel(((LoginAttemptMessage)message).grid,this);
							gridPanel.loadImage();
							gridPanel.setMySnake(((LoginAttemptMessage)message).grid.getSnake(username));
							//((LoginAttemptMessage)message).grid.getSnake(username).client = this;
							gameClientGUI = new GameClientGUI(gridPanel,((LoginAttemptMessage)message).grid.getSnake(username));
							gameClientGUI.setSnake(((LoginAttemptMessage)message).grid.getSnake(username));
							gameClientGUI.setVisible(true);
							sendMessage(new ReadyMessage(username));
						}
						else if (!dead){
							gridPanel.setGrid(((LoginAttemptMessage)message).grid);
							//gridPanel.loadImage();
							gridPanel.setMySnake(((LoginAttemptMessage)message).grid.getSnake(username));
							gridPanel.refreshGUI();
						}
						System.out.println("Valid login received in SnakeClient");
					}
					else {
						System.out.println("Invalid login received in SnakeClient");
					}
				}
				else if (message.type == Type.GuestMessage) {
					GuestMessage gm = (GuestMessage)message;
					if (username == null) {
						loginPanel.loggedIn(gm.username, gm.grid, gm.users);
						username = gm.username;
						gridPanel = new GridPanel(gm.grid,this);
						gridPanel.loadImage();
						gridPanel.setMySnake(gm.grid.getSnake(username));
						//((LoginAttemptMessage)message).grid.getSnake(username).client = this;
						gameClientGUI = new GameClientGUI(gridPanel,gm.grid.getSnake(username));
						gameClientGUI.setSnake(gm.grid.getSnake(username));
						gameClientGUI.setVisible(true);
						sendMessage(new ReadyMessage(username));
					}
					else if (!dead){
						gridPanel.setGrid(gm.grid);
						//gridPanel.loadImage();
						gridPanel.setMySnake(gm.grid.getSnake(username));
						gridPanel.refreshGUI();
					}
				}
				else if (message.type == Type.RegisterMessage) {
					if (((RegisterMessage)message).valid) {
						System.out.println("Valid register received in SnakeClient");
						/*if (username == null) {
							username = ((RegisterMessage)message).username;
						}*/
					}
					else {
						System.out.println("Invalid register received in SnakeClient");
					}
				}
				else if (message.type == Type.JoinedGameMessage) {
					System.out.println(message.senderName + " joined the game");
					if (message.senderName.equals(username)) {
						
					}
				}
				
				// when receives TickMessage, replace the grid from last tick with the updated grid, then refresh
				else if (message.type == Type.TickMessage){
					if (loginPanel.isLoggedIn()){
						sendMessage(new ReceivedTickMessage(username));
						Snake s = ((TickMessage)message).grid.getSnake(username);
						
						if (s!=null && !dead){
							s.setGameClientGUI(gameClientGUI);
							gameClientGUI.updatePowerUpPanel();
							s.setDirection(s.getDirection());
							gridPanel.setGrid(((TickMessage)message).grid);
							//gridPanel.loadImage();
							gridPanel.setMySnake(s);
							gameClientGUI.setSnake(s);
							gridPanel.refreshGUI();
						}
						else {
							if (deadGUI == null){
								dead = true;
								deadGUI = new DeadGUI(this);
								deadGUI.setVisible(true);
							}
						}
					}
				}
				else if (message.type == Type.RespawnMessage){
					if (message.senderName.trim().equals(username.trim())){
						deadGUI = null;
						dead = false;
					}
				}
			}
		} 
		catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} 
		catch (IOException ioe) {
			//System.out.println("ioe - client3: " + ioe.getMessage());
			JFrame disconnectFrame = new JFrame("disconnected");
			JButton okayButton = new JButton("Exit");
			JLabel messageLabel = new JLabel("You have disconnected from server.");
			JPanel content = new JPanel();
			content.setLayout(new GridLayout(2,1));
			content.add(messageLabel);
			content.add(okayButton);
			disconnectFrame.getContentPane().add(content);
			disconnectFrame.setSize(new Dimension(600,400));
		}
		finally {
			try {
				if (s != null) {
					s.close();
				}
			} catch (IOException ioe) {
				System.out.println("ioe - client2: " + ioe.getMessage());
			}
		}
	}
	
	public void sendMessage(SnakeMessage message) {
		try {
			//System.out.println("Trying to send message");
			oos.reset();
			oos.writeObject(message);
			oos.flush();
			bos.flush();
			//oos.flush();
			//oos.reset();
		} catch (IOException ioe) {
			System.out.println("ioe - Client send message: " + ioe.getMessage());
		}
	}
	
	public void setUsername(String u) {
		username = u;
	}
	
	public String getUsername() {
		return username;
	}
}