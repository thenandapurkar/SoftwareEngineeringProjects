package server;

import java.awt.Graphics;
import java.util.Vector;

import client.GridPanel;
import common.Snake;

// Threading for the actual gameplay
// modified for server-side use
// Steven
public class GameThread extends Thread{
	private SnakeServer mServer;
	private int ticker;
	public GameThread(SnakeServer inServer){
		super();
		mServer = inServer;
	}
	
	@Override
	public void run(){
		while(true){
			ticker = 0;
			// default tick rate 33hz
			try{
				Thread.sleep(33);
			} catch (InterruptedException ie){
				ie.printStackTrace();
			}
			while (!mServer.allReceived){
				try{
					Thread.sleep(1);
					ticker++;
					if (ticker == 600){
						ticker = 0;
						break;
					}
				} catch (InterruptedException ie){
					ie.printStackTrace();
				}
			}
			mServer.gameGrid.tick();
			mServer.sendTick();
		}
	}
}
