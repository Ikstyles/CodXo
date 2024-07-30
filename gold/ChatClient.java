import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable {
	Socket socket;
	JTextArea chatArea;
	JButton send,logout;
	JTextField chatField;
	Thread thread;
	
	DataInputStream din;
	DataOutputStream dout;
	String LoginName;
	
	public ChatClient (String login) throws UnknownHostException, IOException {
		super(login);
		LoginName = login;
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					dout.writeUTF(LoginName + " " + "LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
		chatArea = new JTextArea(18,50);
		chatField = new JTextField(50);
		chatField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() ==  KeyEvent.VK_ENTER){
					try {
						if(chatField.getText().length()>0)
						dout.writeUTF(LoginName + " " + "DATA " + chatField.getText().toString());
						chatField.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
			}
			}
		});
		send = new JButton("Send");
		logout = new JButton("Logout");
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					dout.writeUTF(LoginName + " " + "DATA " + chatField.getText().toString());
					chatField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					dout.writeUTF(LoginName + " " + "LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		socket = new Socket("localhost",5217);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(LoginName);
		dout.writeUTF(LoginName +" " +"LOGIN");
		
		thread = new Thread(this);
		thread.start();
		setup();
	}
	private void setup(){
		setSize(600,400);
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(chatArea));
		panel.add(chatField);
		panel.add(send);
		panel.add(logout);
		add(panel);
		setVisible(true);
		
	}
	public void run(){
		while(true){
			try {
				chatArea.append("\n"+ din.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}