package server;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


// GUI for host to start listening on a port

public class PortGUI extends JFrame {
	JButton listenButton;
	JTextField portField;
	
	public PortGUI(){
		super ("port");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		portField = new JTextField();
		portField.setText("port number");
		this.setLayout(new GridLayout(2,0));
		this.add(portField);
		listenButton = new JButton("Start Listening");
		this.add(listenButton);
		listenButton.setEnabled(false);
		addListeners();
		this.setSize(300,200);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void addListeners(){
		portField.getDocument().addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent de){
				check();
			}
			
			public void removeUpdate(DocumentEvent de){
				check();
			}
			
			public void insertUpdate(DocumentEvent de){
				check();
			}
			
			public void check(){
				if (!portField.getText().trim().equals("")){
					listenButton.setEnabled(true);
				}
			}
		});
		
		portField.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent fe){
				portField.setText("");
			}
			public void focusLost(FocusEvent fe){
				
			}
		});
		
		listenButton.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent ae){
				int portNumber = 0;
				boolean parsable = true;
				
				try{
					portNumber = Integer.parseInt(portField.getText());
				} catch (NumberFormatException nfe){
					parsable = false;
				}
				
				if (!parsable){
					portField.setText("");
					listenButton.setEnabled(false);
					JOptionPane.showMessageDialog(null,"Port needs to be a number.","Port error",JOptionPane.ERROR_MESSAGE);
				}
				else{
					new SnakeServer(portNumber);
					//new HostGUI();
					PortGUI.this.dispose();
				}
			}
		});
	}
	
}
