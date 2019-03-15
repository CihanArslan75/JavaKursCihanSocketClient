package com.cihan.socket.client.ui;

import javax.swing.JFrame;
import javax.swing.JTextField;
import com.sun.corba.se.pept.transport.ListenerThread;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientFrame extends JFrame{
	private JTextField txtAddress;
	private JTextField txtPort;
	private JTextField txtUserName;
	private JTextField txtSend;
	protected JTextArea textClient;
	
	String username;
	String address;
	int port;
	protected List<String> users= new ArrayList<>();
	Boolean isConnected=false;
	protected Socket sock;
	protected BufferedReader reader;
	protected PrintWriter writer;
	
	
	public ClientFrame() {
		setTitle("CLIENT");
		clientInitialize();	
	}
	public void clientInitialize() {
		getContentPane().setLayout(null);
		setBounds(100,100,600,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        
		JPanel panel = new JPanel();
		panel.setBounds(12, 0, 550, 400);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblAddress = new JLabel("Adres :");
		lblAddress.setBounds(38, 19, 42, 16);
		panel.add(lblAddress);
		
		txtAddress = new JTextField();
		txtAddress.setText("localhost");
		txtAddress.setBounds(115, 16, 116, 22);
		txtAddress.setColumns(10);
		panel.add(txtAddress);
		
		JLabel lblPort = new JLabel("Port :");
		lblPort.setBounds(249, 16, 56, 16);
		panel.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setText("2222");
		txtPort.setBounds(296, 16, 116, 22);
		panel.add(txtPort);
		txtPort.setColumns(10);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(115, 55, 116, 22);
		panel.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setBounds(38, 55, 75, 16);
		panel.add(lblUsername);
		
		address=txtAddress.getText();
		port=Integer.parseInt(txtPort.getText());
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b_connectActionPerformed(e);
			}
		});
		
		btnConnect.setBounds(441, 13, 97, 25);
		panel.add(btnConnect);
		
		JButton btnDisConnect = new JButton("Disconnect");
		btnDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b_disconnectActionPerformed(e);
			}

		});
		btnDisConnect.setBounds(441, 55, 97, 25);
		panel.add(btnDisConnect);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 90, 526, 268);
		panel.add(scrollPane);
		
		textClient = new JTextArea();
		scrollPane.setViewportView(textClient);
		
		txtSend = new JTextField();
		txtSend.setBounds(12, 371, 400, 22);
		panel.add(txtSend);
		txtSend.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   b_sendActionPerformed(e);
			}
		});
		btnSend.setBounds(424, 370, 97, 25);
		panel.add(btnSend);
	}
	
	public void ListenThread() {
		   Thread incomingReader = new Thread(new IncomingReader());
		   incomingReader.start();
		} 
	

	private void b_disconnectActionPerformed(ActionEvent e) {
		 sendDisconnect();
	     Disconnect();
		
	}
	
	  public void sendDisconnect() 
	    {
	        String bye = (username + ": Disconnect");
	        try
	        {
	            writer.println(bye); 
	            writer.flush(); 
	        } catch (Exception e) 
	        {
	        	textClient.append("Could not send Disconnect message.\n");
	        }
	    }

	    //--------------------------//
	    
	    public void Disconnect() 
	    {
	        try 
	        {
	        	textClient.append("Disconnected.\n");
	            sock.close();
	        } catch(Exception ex) {
	        	textClient.append("Failed to disconnect. \n");
	        }
	        isConnected = false;
	        txtUserName.setEditable(true);

	    }
	    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendActionPerformed
	        String nothing = "";
	        if ((txtSend.getText()).equals(nothing)) {
	            txtSend.setText("");
	            txtSend.requestFocus();
	        } else {
	            try {
	               writer.println(username + ":" + txtSend.getText() + ":" + "Chat");
	               writer.flush(); // flushes the buffer
	            } catch (Exception ex) {
	            	textClient.append("Message was not sent. \n");
	            }
	            txtSend.setText("");
	            txtSend.requestFocus();
	        }

	        txtSend.setText("");
	        txtSend.requestFocus();
	    }

	     
	     private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_connectActionPerformed
	    
	         if (isConnected == false) 
	         {
	             username = txtUserName.getText();
	             txtUserName.setEditable(false);
	            
	             try 
	             {    
	                 sock = new Socket(address, port);
	                
	                 InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
	                 reader = new BufferedReader(streamreader);
	                 writer = new PrintWriter(sock.getOutputStream());
	                 
	                 writer.println(username + ":has connected.:Connect");
	                 writer.flush(); 
	                 isConnected = true; 
	             } 
	             catch (Exception ex) 
	             {
	            	 textClient.append("Cannot Connect! Try Again. \n");
	            	 txtUserName.setEditable(true);
	             }
	             
	             ListenThread();
	             
	         } else if (isConnected == true) 
	         {
	        	 textClient.append("You are already connected. \n");
	         }
	     }//GEN-LAST:event_b_connectActionPerformed
	     
	     public class IncomingReader   implements Runnable{

	    		@Override
	    		public void run() {
	    			String[] data;
	    			String stream;
	    			String done="Done";
	    			String connect="Connect";
	    			String disconnect ="Disconnect";
	    			String chat="Chat";
	    			try {
	    				while((stream =reader.readLine())!=null)
	    				{
	    					data=stream.split(":");
	    					if(data[2].equals(chat)) {
	    						textClient.append(data[0] + " : "+data[1]+"\n");
	    						textClient.setCaretPosition(textClient.getDocument().getLength());
	    					}
	    					else if(data[2].equals(connect))
	    					{
	    						textClient.removeAll();
	    						userAdd(data[0]);
	    					}
	    					else if(data[2].equals(disconnect)) 
	    					{
	    						userRemove(data[0]);
	    					}
	    					else if(data[2].equals(done))
	    					{
	    						writeUser();
	    						users.clear();
	    					}
	    				}
	    			} catch (Exception e) {
	    				
	    			}
	    		}

	    		private void writeUser() {
	    			String[] tempList = new String[users.size()];
	    			users.toArray(tempList);
	    			for (String token : tempList) {
	    				textClient.append(token +"\n");
	    			}
	    			
	    		}

	    		private void userRemove(String data) {
	    			textClient.append(data+" is OFFLINE .\n");
	    		}

	    		private void userAdd(String data) {
	    			users.add(data);
	    			
	    		}
	 
}
}
