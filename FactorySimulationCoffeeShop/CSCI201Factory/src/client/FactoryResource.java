package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import libraries.ImageLibrary;
import resource.Resource;

public class FactoryResource extends FactoryObject implements FactoryReporter{
	
	private final Resource mResource;
	private int startAmount;
	
	FactoryResource(Resource inResource) {
		super(new Rectangle(inResource.getX(),inResource.getY(),1,1));
		mResource = inResource;
		mLabel = inResource.getName();
		mImage = ImageLibrary.getImage(Constants.resourceFolder + inResource.getName() + Constants.png);
		startAmount = mResource.getQuantity();
	}

	@Override
	public void draw(Graphics g, Point mouseLocation) {
		super.draw(g, mouseLocation);
		g.setColor(Color.BLACK);
		g.drawString(mResource.getQuantity()+"", centerTextX(g,mResource.getQuantity()+""), centerTextY(g));
	}
	
	public void takeResource(int amount) {
		mResource.deductFromQuantity(amount);
	}

	public int getX() {
		return mResource.getX();
	}
	public int getY() {
		return mResource.getY();
	}

	public String getName() {
		return mResource.getName();
	}

	@Override
	public void report() {
		// TODO Auto-generated method stub
		System.out.println("Total Resources: "+ mResource.getQuantity() + "/" + startAmount
				+ "Taken: " + (startAmount- mResource.getQuantity()));
	}
}
