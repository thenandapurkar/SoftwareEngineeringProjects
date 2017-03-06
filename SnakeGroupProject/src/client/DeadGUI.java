package client;

import javax.swing.JFrame;

import server.SnakeClient;

public class DeadGUI extends JFrame {
	private SnakeClient mClient;
	private DeadPanel deadPanel;
	public DeadGUI(SnakeClient inClient) {
		mClient = inClient;
		deadPanel = new DeadPanel(this,mClient);
		setSize(450, 350);
		setLocationRelativeTo(null);
		setupFrame();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void setupFrame(){
		this.setContentPane(deadPanel);
	}
	

}
