package q3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UDPRequests implements Runnable {
	DatagramSocket sock;
	DatagramPacket pack;
	
	public UDPRequests(DatagramSocket socket, DatagramPacket sendP) {
		// TODO Auto-generated constructor stub
		sock = socket;
		pack = sendP;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String cmd = new String(pack.getData(), 0,
				pack.getLength());
	    String[] tokens = cmd.split(" ");
	    String print = null;
	    int port = pack.getPort();
	    InetAddress addr = pack.getAddress();
	    DatagramPacket sendPack;
	    byte[] ret;
	      if (tokens[0].equals("purchase")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	print = Store.getInstance().makePurchase(tokens[1], tokens[2], Integer.valueOf(tokens[3]));
	    	ret = print.getBytes();
	    	sendPack = new DatagramPacket(ret,ret.length,addr,port);
	    	try {
				sock.send(sendPack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      } else if (tokens[0].equals("cancel")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  print = Store.getInstance().cancelPurchase(Integer.valueOf(tokens[1]));
	    	  ret = print.getBytes();
	    	  sendPack = new DatagramPacket(ret,ret.length,addr,port);
	    	  try {
					sock.send(sendPack);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      } else if (tokens[0].equals("search")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  print = Store.getInstance().search(tokens[1]);
	    	  ret = print.getBytes();
	    	  sendPack = new DatagramPacket(ret,ret.length,addr,port);
	    	  try {
					sock.send(sendPack);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      } else if (tokens[0].equals("list")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  ArrayList<String> s = Store.getInstance().list();	 
	    	  for(String x : s){
	    		print += x + "\n";
	    	  }
	    	  ret = print.getBytes();
	    	  sendPack = new DatagramPacket(ret,ret.length,addr,port);
	    	  try {
					sock.send(sendPack);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      }
		
	}

}
