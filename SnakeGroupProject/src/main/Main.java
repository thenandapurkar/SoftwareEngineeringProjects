/*
 *  CSCI201_FinalProject_PowerSnake
 *  Team members: Qingzhou Tang, Maureen Wang, Quinn Ellis, Myrl Marmarelis, Zach Nandapurkar
 * */

package main;

import java.awt.*;
import java.util.ArrayList;

import client.*;
import common.*;
import powerups.LaserPowerUp;
import powerups.SpeedPowerUp;
import powerups.WallPowerUp;
import server.PortGUI;
import server.SnakeClient;

public class Main {
	
	public static void main(String args[]) {
		// forces OpenGL for accelerated 2d drawing
		System.setProperty("sun.java2d.opengl","True");
		
		new HostOrJoinGUI().setVisible(true);
	}
}
