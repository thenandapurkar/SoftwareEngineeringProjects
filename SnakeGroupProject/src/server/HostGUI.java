package server;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollBar;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class HostGUI extends JFrame {
	
	private JButton players;
	public HostGUI() {
		setFont(new Font("Silom", Font.PLAIN, 12));
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 204, 153));
		getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel lblGameSessions = new JLabel("Game Session");
		sl_panel.putConstraint(SpringLayout.NORTH, lblGameSessions, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblGameSessions, 0, SpringLayout.WEST, panel);
		lblGameSessions.setFont(new Font("Silom", Font.PLAIN, 24));
		panel.add(lblGameSessions);
		
		
		
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(51, 102, 102));
		setJMenuBar(menuBar);
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.setFont(new Font("Silom", Font.PLAIN, 14));
		menuBar.add(mnAbout);
		
		JMenu mnExit = new JMenu("Exit");
		mnExit.setFont(new Font("Silom", Font.PLAIN, 14));
		menuBar.add(mnExit);
		
		
		SpringLayout sl_temp = new SpringLayout();
		JPanel temp = new JPanel(sl_temp);
		sl_panel.putConstraint(SpringLayout.NORTH, temp, 67, SpringLayout.SOUTH, lblGameSessions);
		sl_panel.putConstraint(SpringLayout.WEST, temp, 35, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, temp, -103, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, temp, 409, SpringLayout.WEST, panel);
		temp.setBackground(new Color(51, 102, 102));
		
		JButton start = new JButton("Start");
		start.setFont(new Font("Silom", Font.PLAIN, 13));
		sl_temp.putConstraint(SpringLayout.NORTH, start, 0, SpringLayout.NORTH, temp);
		sl_temp.putConstraint(SpringLayout.SOUTH, start, 0, SpringLayout.SOUTH, temp);
		sl_temp.putConstraint(SpringLayout.EAST, start, -267, SpringLayout.EAST, temp);
		start.setBackground(Color.GREEN);
		
		JButton end = new JButton("End");
		end.setFont(new Font("Silom", Font.PLAIN, 13));
		sl_temp.putConstraint(SpringLayout.NORTH, end, 0, SpringLayout.NORTH, temp);
		sl_temp.putConstraint(SpringLayout.SOUTH, end, 0, SpringLayout.SOUTH, start);
		end.setBackground(Color.RED);
		
		players = new JButton("Players");
		players.setFont(new Font("Silom", Font.PLAIN, 13));
		sl_temp.putConstraint(SpringLayout.EAST, end, -25, SpringLayout.WEST, players);
		sl_temp.putConstraint(SpringLayout.NORTH, players, 0, SpringLayout.NORTH, temp);
		sl_temp.putConstraint(SpringLayout.WEST, players, 246, SpringLayout.WEST, temp);
		sl_temp.putConstraint(SpringLayout.SOUTH, players, 0, SpringLayout.SOUTH, start);
		players.setBackground(Color.BLUE);
		
		JButton trash = new JButton("Delete");
		temp.add(start);
		temp.add(end);
		temp.add(players);
		
		panel.add(temp);
	}
	
	private void addActionListeners(){
			
			
			players.addActionListener(new ActionListener(){
				ArrayList<String> playerlist = new ArrayList<>();

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFrame playerFrame = new JFrame();
					playerFrame.setSize(400, 600);
					JScrollPane players = new JScrollPane();
					JPanel playerPanel = new JPanel();
					
					
					for(String temp : playerlist){
						JButton kick = new JButton();
						kick.setBackground(Color.ORANGE);
						playerPanel.add(new JLabel(temp));
						playerPanel.add(kick);
					}
					
					players.add(playerPanel);
					playerFrame.getContentPane().add(players);
					playerFrame.setVisible(true);
					
				}
				
			});
			
//			c.addActionListener(new ActionListener(){
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					new CustomizationGUI().setVisible(true);
//				}
//				
//			});
			
			
		
	
		
	}
}
