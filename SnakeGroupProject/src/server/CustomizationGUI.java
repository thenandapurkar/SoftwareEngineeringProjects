package server;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.UIManager;

public class CustomizationGUI extends JFrame {
	public CustomizationGUI() {
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(224, 255, 255));
		getContentPane().add(panel, BorderLayout.CENTER);
		
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel lblGridSize = new JLabel("Grid Size");
		sl_panel.putConstraint(SpringLayout.NORTH, lblGridSize, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblGridSize, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblGridSize, -252, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblGridSize, 106, SpringLayout.WEST, panel);
		lblGridSize.setFont(new Font("Silom", Font.PLAIN, 13));
		panel.add(lblGridSize);
		
		JSlider slider = new JSlider();
		sl_panel.putConstraint(SpringLayout.NORTH, slider, 1, SpringLayout.SOUTH, lblGridSize);
		sl_panel.putConstraint(SpringLayout.WEST, slider, 0, SpringLayout.WEST, lblGridSize);
		panel.add(slider);
		
		JLabel lblMaximumPlayers = new JLabel("Maximum Players");
		sl_panel.putConstraint(SpringLayout.NORTH, lblMaximumPlayers, 6, SpringLayout.SOUTH, slider);
		sl_panel.putConstraint(SpringLayout.WEST, lblMaximumPlayers, 0, SpringLayout.WEST, lblGridSize);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblMaximumPlayers, -200, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblMaximumPlayers, -261, SpringLayout.EAST, panel);
		lblMaximumPlayers.setFont(new Font("Silom", Font.PLAIN, 13));
		panel.add(lblMaximumPlayers);
		
		JSlider slider_1 = new JSlider();
		sl_panel.putConstraint(SpringLayout.NORTH, slider_1, 6, SpringLayout.SOUTH, lblMaximumPlayers);
		sl_panel.putConstraint(SpringLayout.WEST, slider_1, 0, SpringLayout.WEST, lblGridSize);
		panel.add(slider_1);
		
		JLabel label = new JLabel("");
		sl_panel.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, label, -135, SpringLayout.SOUTH, panel);
		panel.add(label);
		
		JLabel lblFoodSize = new JLabel("Food Count");
		sl_panel.putConstraint(SpringLayout.NORTH, lblFoodSize, 6, SpringLayout.SOUTH, slider_1);
		sl_panel.putConstraint(SpringLayout.WEST, lblFoodSize, 0, SpringLayout.WEST, lblGridSize);
		sl_panel.putConstraint(SpringLayout.EAST, lblFoodSize, 0, SpringLayout.EAST, lblGridSize);
		lblFoodSize.setFont(new Font("Silom", Font.PLAIN, 13));
		panel.add(lblFoodSize);
		
		JSlider slider_2 = new JSlider();
		sl_panel.putConstraint(SpringLayout.NORTH, slider_2, 134, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblFoodSize, -2, SpringLayout.NORTH, slider_2);
		sl_panel.putConstraint(SpringLayout.WEST, slider_2, 0, SpringLayout.WEST, lblGridSize);
		panel.add(slider_2);
		
		JLabel lblObstacleCount = new JLabel("Obstacle Count");
		sl_panel.putConstraint(SpringLayout.WEST, lblObstacleCount, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblObstacleCount, 128, SpringLayout.WEST, panel);
		lblObstacleCount.setFont(new Font("Silom", Font.PLAIN, 16));
		panel.add(lblObstacleCount);
		
		JSlider slider_3 = new JSlider();
		sl_panel.putConstraint(SpringLayout.NORTH, lblObstacleCount, 2, SpringLayout.SOUTH, slider_3);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblObstacleCount, -31, SpringLayout.NORTH, slider_3);
		sl_panel.putConstraint(SpringLayout.EAST, slider_3, 0, SpringLayout.EAST, slider);
		panel.add(slider_3);
		
		JLabel lblWallCount = new JLabel("Wall Count");
		sl_panel.putConstraint(SpringLayout.NORTH, slider_3, 6, SpringLayout.SOUTH, lblWallCount);
		lblWallCount.setFont(new Font("Silom", Font.PLAIN, 13));
		sl_panel.putConstraint(SpringLayout.NORTH, lblWallCount, 6, SpringLayout.SOUTH, slider_2);
		sl_panel.putConstraint(SpringLayout.WEST, lblWallCount, 0, SpringLayout.WEST, lblGridSize);
		panel.add(lblWallCount);
		
		JLabel lblObstacleCount_1 = new JLabel("Obstacle Count");
		lblObstacleCount_1.setFont(new Font("Silom", Font.PLAIN, 13));
		sl_panel.putConstraint(SpringLayout.NORTH, lblObstacleCount_1, 0, SpringLayout.NORTH, lblObstacleCount);
		sl_panel.putConstraint(SpringLayout.WEST, lblObstacleCount_1, 0, SpringLayout.WEST, lblGridSize);
		panel.add(lblObstacleCount_1);
		
		JSlider slider_4 = new JSlider();
		sl_panel.putConstraint(SpringLayout.NORTH, slider_4, 6, SpringLayout.SOUTH, lblObstacleCount_1);
		sl_panel.putConstraint(SpringLayout.WEST, slider_4, 0, SpringLayout.WEST, lblGridSize);
		panel.add(slider_4);
		
		JLabel lblSize = new JLabel("Size");
		sl_panel.putConstraint(SpringLayout.NORTH, lblSize, 0, SpringLayout.NORTH, slider);
		sl_panel.putConstraint(SpringLayout.WEST, lblSize, 6, SpringLayout.EAST, slider);
		panel.add(lblSize);
		
		JLabel lblOfPlayers = new JLabel("# of players");
		sl_panel.putConstraint(SpringLayout.NORTH, lblOfPlayers, 41, SpringLayout.SOUTH, lblSize);
		sl_panel.putConstraint(SpringLayout.WEST, lblOfPlayers, 1, SpringLayout.EAST, slider_1);
		panel.add(lblOfPlayers);
		
		JLabel lblSize_1 = new JLabel("Size");
		sl_panel.putConstraint(SpringLayout.SOUTH, lblSize_1, 0, SpringLayout.SOUTH, label);
		sl_panel.putConstraint(SpringLayout.EAST, lblSize_1, 0, SpringLayout.EAST, lblSize);
		panel.add(lblSize_1);
		
		JLabel lblCount = new JLabel("Count");
		sl_panel.putConstraint(SpringLayout.NORTH, lblCount, 0, SpringLayout.NORTH, slider_3);
		sl_panel.putConstraint(SpringLayout.WEST, lblCount, 6, SpringLayout.EAST, slider_3);
		panel.add(lblCount);
		
		JLabel lblCount_1 = new JLabel("Count");
		sl_panel.putConstraint(SpringLayout.NORTH, lblCount_1, 0, SpringLayout.NORTH, slider_4);
		sl_panel.putConstraint(SpringLayout.WEST, lblCount_1, 6, SpringLayout.EAST, slider_4);
		panel.add(lblCount_1);
		
		JButton btnSaveExit = new JButton("Save & Exit");
		btnSaveExit.setBackground(UIManager.getColor("desktop"));
		sl_panel.putConstraint(SpringLayout.SOUTH, btnSaveExit, 0, SpringLayout.SOUTH, lblCount_1);
		sl_panel.putConstraint(SpringLayout.EAST, btnSaveExit, -10, SpringLayout.EAST, panel);
		panel.add(btnSaveExit);
		
	}
}
