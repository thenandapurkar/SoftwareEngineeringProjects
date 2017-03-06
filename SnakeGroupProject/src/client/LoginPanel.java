package client;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import common.Direction;
import common.Food;
import common.Grid;
import common.Snake;
import common.Wall;
import messages.LoginAttemptMessage;
import messages.RegisterMessage;
import messages.GuestMessage;
import powerups.LaserPowerUp;
import powerups.SpeedPowerUp;
import powerups.WallPowerUp;
import server.SnakeClient;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class LoginPanel extends JPanel {
	
	//SpringLayout is for the GUI Builder
	private SpringLayout springLayout;
	
	//Buttons
	private JButton loginButton;
	private JButton registerButton;
	private JButton playAsGuestButton;
	
	//Text Fields
	private JTextField userNameField;
	private JTextField passwordField;
	
	//Labels
	private JLabel username;
	private JLabel password;
	private SnakeClient mClient;
	private String usernameString;
	private LoginGUI loginGUI;
	
	private boolean loggedIn = false;
	
	public LoginPanel(SnakeClient inClient,LoginGUI loginGUI) {
		
		this.loginGUI = loginGUI;
		//Initializing Layout
		springLayout = new SpringLayout();
		mClient = inClient;
		mClient.setLoginPanel(this);
		//Initializing JLabels
		username = new JLabel("Username");
		username.setFont(new Font("Silom", Font.PLAIN, 13));
		password = new JLabel("Password");
		springLayout.putConstraint(SpringLayout.EAST, username, 21, SpringLayout.EAST, password);
		springLayout.putConstraint(SpringLayout.WEST, password, 91, SpringLayout.WEST, this);
		password.setFont(new Font("Silom", Font.PLAIN, 13));
		
		//Initializing Text Fields
		userNameField = new JTextField();
		passwordField = new JTextField();
		
		//Initializing Buttons
		loginButton = new JButton("Login!");
		loginButton.setFont(new Font("Silom", Font.PLAIN, 13));
		loginButton.setBackground(new Color(255, 255, 255));
		registerButton = new JButton("Register");
		registerButton.setFont(new Font("Silom", Font.PLAIN, 13));
		springLayout.putConstraint(SpringLayout.NORTH, registerButton, 205, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, registerButton, -55, SpringLayout.SOUTH, this);
		playAsGuestButton = new JButton("Play As Guest");
		playAsGuestButton.setFont(new Font("Silom", Font.PLAIN, 13));
		springLayout.putConstraint(SpringLayout.NORTH, playAsGuestButton, 6, SpringLayout.SOUTH, registerButton);
		springLayout.putConstraint(SpringLayout.WEST, playAsGuestButton, 155, SpringLayout.WEST, this);
		
		//springLayout.putConstraint(SpringLayout.SOUTH, playAsGuestButton, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, playAsGuestButton, -8, SpringLayout.EAST, userNameField);
		
		setupPanel();
	}
	
	private void setupPanel(){
		this.setLayout(springLayout);
		
		//Add Labels
		this.add(username);
		this.add(password);
		
		//Add text boxes
		this.add(userNameField);
		this.add(passwordField);
		
		//AddButtons
		this.add(loginButton);
		this.add(registerButton);
		this.add(playAsGuestButton);
		
		
		
		this.setBackground(new Color(51, 204, 153));
		
		//Button Locations
		setLocations();
		addHandlers();
	}
	
	private void setLocations(){
		
		//Labels
		springLayout.putConstraint(SpringLayout.WEST, username, 91, SpringLayout.WEST, this);
		
		//Text Boxes
		springLayout.putConstraint(SpringLayout.SOUTH, userNameField, -8, SpringLayout.NORTH, password);
		springLayout.putConstraint(SpringLayout.SOUTH, username, -6, SpringLayout.NORTH, userNameField);
		springLayout.putConstraint(SpringLayout.WEST, userNameField, 142, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, userNameField, -154, SpringLayout.EAST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 117, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, password, -6, SpringLayout.NORTH, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 142, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, userNameField);
		
		springLayout.putConstraint(SpringLayout.NORTH, loginButton, 16, SpringLayout.SOUTH, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, loginButton, 0, SpringLayout.WEST, registerButton);
		springLayout.putConstraint(SpringLayout.SOUTH, loginButton, -6, SpringLayout.NORTH, registerButton);
		springLayout.putConstraint(SpringLayout.EAST, loginButton, -6, SpringLayout.EAST, registerButton);
		
		springLayout.putConstraint(SpringLayout.WEST, registerButton, 168, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, registerButton, -177, SpringLayout.EAST, this);
		
		
		
	}
	
	
	
	private void addHandlers(){
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("login button pressed");
				usernameString = userNameField.getText();
				LoginAttemptMessage message = new LoginAttemptMessage("", usernameString, passwordField.getText());
				Grid g = new Grid(new Dimension(50, 50));
				message.grid = g;
				mClient.sendMessage(message);
				loginGUI.dispose();
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("register butotn pressed");
				usernameString = userNameField.getText();
				RegisterMessage message = new RegisterMessage("", userNameField.getText(), passwordField.getText());
				mClient.sendMessage(message);
			}
		});
		
		playAsGuestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*GuestMessage message = new GuestMessage("", "");
				Grid g = new Grid(new Dimension(50, 50));
				message.grid = g;
				mClient.sendMessage(message);*/
				userNameField.setText("Guest_"+Long.toString(System.currentTimeMillis()%10000));
				passwordField.setText("guest");
				registerButton.doClick();
				loginButton.doClick();
			}
		});
		
		
	}
	
	
	
	//allows buttons to be gradient
	private static class JGradientButton extends JButton {
        private JGradientButton(String text) {
            super(text);
            setContentAreaFilled(true);
            setFocusPainted(false); 
        //   setOpaque(true);
//           setBorderPainted(false);// used for demonstration
        }

        @Override
        protected void paintComponent(Graphics g) {
            final Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0), 
                    Color.WHITE, 
                    new Point(0, getHeight()), 
                    Color.BLUE.brighter()));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }

//        public static JGradientButton newInstance() {
//            return new JGradientButton(text);
//        }
    }
	
	public void loggedIn(String username, Grid grid, ArrayList<String> users) {
		if (username.equals(usernameString)) {
			loggedIn = true;
			/*System.out.println(username + " logged in successfully");
			Snake s = new Snake(7, Direction.UP, grid, new Point(6, 5));
			s.setColor("red");
			GridPanel g= new GridPanel(grid);
			g.loadImage();
			g.setMySnake(s);
			GameClientGUI gcg = new GameClientGUI(g, s, users);
			gcg.setVisible(true);*/
		}
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	public void failedLogin(String username) {
		if (username.equals(usernameString)) {
			loggedIn = false;
			System.out.println(username + " was unable to log in");
		}
	}

}
