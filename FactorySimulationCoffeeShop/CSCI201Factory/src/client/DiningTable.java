package client;

import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.Semaphore;

import resource.Resource;

public class DiningTable extends FactoryResource {

	Semaphore spots;
	Resource r;
	DiningTable(Resource inResource) {
		super(inResource);
		r=inResource;
		spots = new Semaphore(3);
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void draw(Graphics g, Point mouseLocation){
		super.draw(g, mouseLocation);
	}
	
	public void headToTable(){
		
	}
	
	public void enterTable() throws InterruptedException{
		spots.acquire();
		super.takeResource(1);
	}
	
	public void leaveTable(){
		super.takeResource(-1);
		spots.release();
	}
	
	public boolean isFull(){
		return r.getQuantity() == 0;
	}
	
	

}
