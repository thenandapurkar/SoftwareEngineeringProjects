package client;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import messages.RespawnMessage;
import server.SnakeClient;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeadPanel extends JPanel {
	
	//layout 
	private SpringLayout springLayout;
	private JLabel lblYouDied;
	private JLabel lblGlobalScore;
	private JLabel lblBestOfThis;
	
	private JButton respawnButton;
	private JButton customizeButton;
	private JButton quitButton;
	
	private DeadGUI deadGUI;
	private SnakeClient mClient;
	
	
	public DeadPanel(DeadGUI inGUI, SnakeClient inClient) {
		
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		deadGUI = inGUI;
		mClient = inClient;
		
		//Initialize Labels
		lblYouDied = new JLabel("You Died!");
		springLayout.putConstraint(SpringLayout.NORTH, lblYouDied, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblYouDied, 133, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, lblYouDied, -215, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblYouDied, -116, SpringLayout.EAST, this);
		lblGlobalScore = new JLabel("Global Score: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblGlobalScore, 6, SpringLayout.SOUTH, lblYouDied);
		springLayout.putConstraint(SpringLayout.WEST, lblGlobalScore, 10, SpringLayout.WEST, lblYouDied);
		springLayout.putConstraint(SpringLayout.SOUTH, lblGlobalScore, -177, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblGlobalScore, -136, SpringLayout.EAST, this);
		lblBestOfThis = new JLabel("Best of This Game:  ");
		springLayout.putConstraint(SpringLayout.NORTH, lblBestOfThis, 6, SpringLayout.SOUTH, lblGlobalScore);
		springLayout.putConstraint(SpringLayout.WEST, lblBestOfThis, 0, SpringLayout.WEST, lblYouDied);
		
		//Initialize Buttons
		respawnButton = new JButton("Respawn");
		springLayout.putConstraint(SpringLayout.NORTH, respawnButton, 22, SpringLayout.SOUTH, lblYouDied);
		springLayout.putConstraint(SpringLayout.WEST, respawnButton, 150, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, respawnButton, 67, SpringLayout.SOUTH, lblYouDied);
		springLayout.putConstraint(SpringLayout.EAST, respawnButton, -168, SpringLayout.EAST, this);
		respawnButton.setFont(new Font("Silom", Font.PLAIN, 13));
		//customizeButton = new JButton("Customize");
		//springLayout.putConstraint(SpringLayout.NORTH, customizeButton, 6, SpringLayout.SOUTH, respawnButton);
		//springLayout.putConstraint(SpringLayout.WEST, customizeButton, 0, SpringLayout.WEST, respawnButton);
		//springLayout.putConstraint(SpringLayout.SOUTH, customizeButton, -89, SpringLayout.SOUTH, this);
		//springLayout.putConstraint(SpringLayout.EAST, customizeButton, 0, SpringLayout.EAST, respawnButton);
		//customizeButton.setFont(new Font("Silom", Font.PLAIN, 13));
		quitButton = new JButton("Quit");
		springLayout.putConstraint(SpringLayout.NORTH, quitButton, 6, SpringLayout.SOUTH, respawnButton);
		springLayout.putConstraint(SpringLayout.WEST, quitButton, 0, SpringLayout.WEST, respawnButton);
		springLayout.putConstraint(SpringLayout.SOUTH, quitButton, -89, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, quitButton, 0, SpringLayout.EAST, respawnButton);
		quitButton.setFont(new Font("Silom", Font.PLAIN, 13));
		
		addListeners();
		setupPanel();
		//customizeButton.setVisible(false);
	}
	
	private void setupPanel(){
		//Background
		setBackground(new Color(51, 204, 153));
		
		//Set Fonts
		lblYouDied.setFont(new Font("Silom", Font.PLAIN, 33));
//		lblGlobalScore.setFont(new Font("Silom", Font.PLAIN, 16));
//		lblBestOfThis.setFont(new Font("Silom", Font.PLAIN, 16));
//		
//		lblGlobalScore.setVisible(false);
//		lblBestOfThis.setVisible(false);
		//Add to panel
		this.add(lblYouDied);
//		this.add(lblGlobalScore);
//		this.add(lblBestOfThis);
		this.add(respawnButton);
		//this.add(customizeButton);
		this.add(quitButton);
		
		setLocations();
	}
	
	private void setLocations(){
		
		
		
		
	}
	
	private void addListeners(){
		
		respawnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mClient.dead = false;
				mClient.sendMessage(new RespawnMessage(mClient.getUsername()));
				System.out.println("Sent Respawn message");
				deadGUI.dispose();
			}
		});
		
		/*customizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});*/
		
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		
		
	}
	
	
	
	private static class JGradientButton extends JButton {
		private Color color;
		
        private JGradientButton(String text, Color c) {
            super(text);
            color = c;
            setContentAreaFilled(true);
            setFocusPainted(false); 
        //   setOpaque(true);
//           setBorderPainted(false);// used for demonstration
        }

        @Override
        protected void paintComponent(Graphics g) {
            final Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0), 
                    Color.WHITE, 
                    new Point(0, getHeight()), 
                    color.brighter()));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }

//        public static JGradientButton newInstance() {
//            return new JGradientButton(text);
//        }
    }
	
	
}
