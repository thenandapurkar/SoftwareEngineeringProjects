package client;

import javax.swing.*;

import java.awt.Font;
import java.util.*;
import java.awt.Color;

public class ScorePanel extends JPanel {
	private GridPanel gridPanel;
	private JLabel[] labels;
	private JLabel info;
	public ScorePanel(GridPanel gridPanel, int len) {
		setBackground(new Color(255, 255, 255));
		
		
		this.gridPanel = gridPanel;
		info = new JLabel("Players");
		info.setFont(new Font("Silom", Font.PLAIN, 24));
		this.labels = new JLabel[len];
		add(info);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (int i = 0; i < len; i++) {
			Random rand = new Random();
			int R = (int)(Math.random()*256);
			int G = (int)(Math.random()*256);
			int B= (int)(Math.random()*256);
			
			labels[i] = new JLabel();
			labels[i].setFont(new Font("Silom", Font.PLAIN, 24));
			labels[i].setForeground(new Color(R, G, B));
			add(labels[i]);
		}
	}
	
	public void refreshGUI() {
		Map<String, Integer> scores = gridPanel.getGrid().getNamesAndScores();
		TreeMap<Integer, String> scoreTree = new TreeMap<Integer, String>();
		for (Map.Entry<String, Integer> e : scores.entrySet()) {
			scoreTree.put(e.getValue(), e.getKey());
		}
		String[] sortedScores = scoreTree.values().toArray(new String[0]);
		for (int i = 0; i < labels.length; i++) {
			int j = sortedScores.length - 1 - i;
			if (i >= sortedScores.length) labels[i].setText("");
			else labels[i].setText(
					sortedScores[j] + ": " + scores.get(sortedScores[j])
				);
		}
		revalidate();
		repaint();
	}
}
