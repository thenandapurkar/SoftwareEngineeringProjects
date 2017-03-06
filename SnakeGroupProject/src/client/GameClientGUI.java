package client;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import common.Snake;
import powerups.PowerUp;

public class GameClientGUI extends JFrame {
	
	private GridPanel gridPanel;
	private JPanel eastPanel;
	private Snake snake;
	private ArrayList<String> people;
	private JPanel powerUpPanel;
	private JLabel label;
	private JLabel imageIcon;
	private ScorePanel scorePanel;
	//private JLabel instructionLabel;
	public GameClientGUI(GridPanel gPanel, Snake snake) {
		getContentPane().setLayout(new BorderLayout());
		powerUpPanel = new JPanel();
		eastPanel = new JPanel();
		this.snake = snake;
		people = new ArrayList<String>();
		
		//people.add("Frank");
		//people.add("John");
		
		this.gridPanel = gPanel;
		scorePanel = new ScorePanel(gPanel, 5);
		scorePanel.setBorder(new LineBorder(Color.BLACK));
		gridPanel.setScorePanel(scorePanel);
		powerUpPanel.setBackground(Color.WHITE);
		powerUpPanel.setLayout(new BorderLayout());
		powerUpPanel.setPreferredSize(new Dimension(300,450));
		powerUpPanel.setBorder(new LineBorder(Color.BLACK));
		label = new JLabel("No PowerUp");
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageIcon = new JLabel();
		imageIcon.setHorizontalAlignment(JLabel.CENTER);
		imageIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setFont(new Font("Silom", Font.PLAIN, 24));
		//instructionLabel = new JLabel("");
		powerUpPanel.add(label,BorderLayout.NORTH);
		powerUpPanel.add(imageIcon,BorderLayout.CENTER);
		//powerUpPanel.add(instructionLabel,BorderLayout.SOUTH);
		setUpEastPanel();
		this.setSize(new Dimension(1150,900));
		add(gPanel, BorderLayout.CENTER);
		add(eastPanel, BorderLayout.EAST);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	
	/*private JPanel setUpLeaderBoardPanel(ArrayList<String> users){
		return new ScorePanel(gridPanel, 5);
		leaderBoardPanel.setLayout(new GridLayout(0,1));
		for(int i=0; i<users.size(); i++){
			JLabel j = new JLabel(users.get(i));
			leaderBoardPanel.add(j);
			leaderBoardPanel.repaint();
			leaderBoardPanel.revalidate();
		}
		
		return leaderBoardPanel;
	}*/
	
//	private JPanel setUpPowerUpPanel(Snake snake){
//		JPanel powerUpPanel = new JPanel();
//		powerUpPanel.setLayout(new BorderLayout());
//		//snake.getActivePowerUp().getName()
//		powerUpPanel.add(new JLabel("Wall"), BorderLayout.NORTH);
//		//snake.getActivePowerUp().getImage()
//		powerUpPanel.add(new JLabel("------"), BorderLayout.SOUTH);
//		
//		return powerUpPanel;
//	}
	
	public void updatePowerUpPanel(){
		
		
		if(snake.hasPowerUp()){
			snake.getActivePowerUp().loadImage();
			label.setText(snake.getActivePowerUp().getName());
			//powerUpPanel.add(label, BorderLayout.NORTH);
			imageIcon.setIcon(new ImageIcon(snake.getActivePowerUp().getImage()));
			imageIcon.setVisible(true);
			//instructionLabel.setText(snake.getActivePowerUp().getDescription());
			/*this.remove(imageIcon);
			imageIcon = new JLabel(new ImageIcon(snake.getActivePowerUp().getImage()));
			this.add(imageIcon,BorderLayout.CENTER);*/
		}
		else{
			label.setText("No PowerUp");
			imageIcon.setVisible(false);
			//instructionLabel.setText("Pick up a power up.");
			/*this.remove(imageIcon);
			imageIcon = new JLabel();
			this.add(imageIcon, BorderLayout.CENTER);*/
		}
		powerUpPanel.revalidate();
		powerUpPanel.repaint();
		/*this.remove(eastPanel);
		this.add(eastPanel,BorderLayout.EAST);*/
	}
	
	public void setSnake(Snake inSnake){
		snake = inSnake;
	}
	

	private void setUpEastPanel(){
		
		eastPanel.setLayout(new GridLayout(2, 1));
		JScrollPane leaderScroll = new JScrollPane(scorePanel);
		//leaderScroll.add();
		
		eastPanel.add(leaderScroll);
		eastPanel.add(powerUpPanel);
		
	}

}
