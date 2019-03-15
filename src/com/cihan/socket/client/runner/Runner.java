package com.cihan.socket.client.runner;

import com.cihan.socket.client.ui.ClientFrame;
public class Runner {

	public static void main(String[] args) {
		
		java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
            	ClientFrame clientFrame=new ClientFrame();
        		clientFrame.setVisible(true);
            }
        });

	}

}
