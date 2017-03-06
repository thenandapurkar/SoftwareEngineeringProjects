package client;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import common.Entity;

public class GridCell extends JPanel {
	
	private Entity currentEntity;
	
	public void setEntity(Entity entity) {
		currentEntity = entity;
	}
	
	public Entity getEntity() {
		return currentEntity;
	}
	
	public GridCell() {
		super();
		//setPreferredSize(new Dimension(30, 30));
		//setBackground(Color.black);
	}
}
