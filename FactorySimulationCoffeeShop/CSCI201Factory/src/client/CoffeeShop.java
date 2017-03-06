package client;

import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import resource.Resource;

public class CoffeeShop extends FactoryResource{

	Socket s;
	BufferedWriter bw;
	ArrayList<FactoryWorker> workersInShop;
	String timeStamp;
	CoffeeShop(Resource inResource) throws UnknownHostException, IOException {
		super(inResource);
		// TODO Auto-generated constructor stub
		s = new Socket("localhost", 6789);
		workersInShop = new ArrayList<FactoryWorker>();
		bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		
	}
	
	@Override
	public void draw(Graphics g, Point mouseLocation) {
		super.draw(g, mouseLocation);
		
	}
	
	
	public synchronized void enterShop(FactoryWorker f) throws IOException{
		//workersInShop.add(f);
		timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		bw.write(f.getName() + " ordered coffee at " + timeStamp);
		bw.newLine();
		bw.flush();
		System.out.println("Name Sent");
	}
	
	public synchronized void leaveShop(FactoryWorker f){
		//workersInShop.remove(f);
	}
	
	

}
