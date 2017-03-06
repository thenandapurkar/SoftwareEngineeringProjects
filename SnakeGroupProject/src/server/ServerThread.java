package server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import messages.SnakeMessage;

public class ServerThread extends Thread {

	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private SnakeServer server;
	public ServerThread(Socket s, SnakeServer snakeServer) {
		try {
			this.server = snakeServer;
			oos = new ObjectOutputStream(s.getOutputStream());
			//ois = new ObjectInputStream(s.getInputStream());
			ois = new ObjectInputStream(
					new BufferedInputStream(s.getInputStream()));
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe - Thread:" + ioe.getMessage());
		}
	}
	
	public void sendMessage(SnakeMessage message) {
		try {
			oos.writeObject(message);
			oos.reset();
			oos.flush();
			
		} catch (IOException ioe) {
			System.out.println("ioe - Thread: " + ioe.getMessage());
		}
	}

	public void run() {
		try {
			while(true) {
				SnakeMessage message = (SnakeMessage)ois.readObject();
				if (message != null) {
					server.parseMessage(message);
				} else {
					throw(new IOException());
				}
			}
		} 
		catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe in run - Thread: " + cnfe.getMessage());
		} catch (IOException ioe) {
			try{
				ois.close();
				oos.close();
			} catch (IOException ioe2){}
			server.removeServerThread(this);
		}
	}
	
	public Map<String, Integer> getTeamNamesAndScores() {
		return server.getTeamNamesAndScores();
	}
}