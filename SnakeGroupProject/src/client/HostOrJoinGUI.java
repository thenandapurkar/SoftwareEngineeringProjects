package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import server.PortGUI;
import java.awt.Color;
import java.awt.Font;

public class HostOrJoinGUI extends JFrame {
	
	private JButton hostButton = new JButton("Host");
	private JButton joinButton = new JButton("Join");
	
	public HostOrJoinGUI() {
		super();
		getContentPane().setBackground(new Color(51, 204, 153));
		getContentPane().setLayout(new FlowLayout());
		setSize(200, 150);
		hostButton.setFont(new Font("Silom", Font.PLAIN, 14));
		getContentPane().add(hostButton);
		joinButton.setFont(new Font("Silom", Font.PLAIN, 14));
		getContentPane().add(joinButton);
		addActions();
		
		//quick change to look
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public void addActions() {
		hostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PortGUI().setVisible(true);
				HostOrJoinGUI.this.dispose();
			}
		});
		
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ConnectGUI().setVisible(true);
				HostOrJoinGUI.this.dispose();
			}
		});
	}
}
