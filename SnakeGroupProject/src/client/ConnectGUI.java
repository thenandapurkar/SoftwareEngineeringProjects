package client;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import server.SnakeClient;

public class ConnectGUI extends JFrame {
	JButton connectButton;
	JTextField portField;
	JTextField ipField;
	public ConnectGUI(){
		super("connect to host");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		connectButton = new JButton("connect");
		portField = new JTextField();
		ipField = new JTextField();
		ipField.setText("Host IP");
		portField.setText("Port");
		connectButton.setEnabled(false);
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(1,2));
		textPanel.add(ipField);
		textPanel.add(portField);
		this.setLayout(new GridLayout(2,1));
		this.add(textPanel);
		this.add(connectButton);
		addListeners();
		this.setSize(300,200);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void addListeners(){
		ipField.getDocument().addDocumentListener(new textListener());
		ipField.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent fe){
				ipField.setText("");
			}
			public void focusLost(FocusEvent fe){
				
			}
		});
		portField.getDocument().addDocumentListener(new textListener());
		portField.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent fe){
				portField.setText("");
			}
			public void focusLost(FocusEvent fe){
				
			}
		});
		connectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				boolean parsable = true;
				int portNumber = -1;
				try{
					portNumber = Integer.parseInt(portField.getText());
				} catch (NumberFormatException ife){
					parsable = false;
				}
				
				if (!parsable){
					JOptionPane.showMessageDialog(null,"Port needs to be a number.","Port error",JOptionPane.ERROR_MESSAGE);
					portField.setText("");
					connectButton.setEnabled(false);
				}
				else{
					SnakeClient newClient = new SnakeClient(ipField.getText().trim(),portNumber);
					ConnectGUI.this.dispose();
					new LoginGUI(newClient).setVisible(true);;
				}
			}
		});
	}
	
	private class textListener implements DocumentListener{
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
			if ((!ipField.getText().trim().equals("")) && (!portField.getText().trim().equals(""))){
				connectButton.setEnabled(true);
			}
		}
	}
}
