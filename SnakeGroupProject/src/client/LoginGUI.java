package client;

import javax.swing.JFrame;
import server.SnakeClient;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginGUI extends JFrame {
	
	private LoginPanel loginPanel;
	
	public LoginGUI(SnakeClient inClient) {
		super("Login");
		loginPanel = new LoginPanel(inClient,this);
		setSize(450, 350);
		setupFrame();
		this.setLocationRelativeTo(null);
	}
	
	private void setupFrame(){
		this.setContentPane(loginPanel);
	}

}
