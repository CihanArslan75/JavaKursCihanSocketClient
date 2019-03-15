package com.cihan.socket.client.logic;

import com.cihan.socket.client.ui.ClientFrame;

public class IncomingReader  extends ClientFrame implements Runnable{

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
