package q3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TCPRequests implements Runnable{
	Socket socket;
	
	public TCPRequests(Socket s) {
		// TODO Auto-generated constructor stub
		this.socket = s;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PrintWriter MyClientT = null;
		Scanner sc = null;
		try{
			sc = new Scanner(socket.getInputStream());
			MyClientT= new PrintWriter(socket.getOutputStream());
			while(sc.hasNextLine()) {
	    	      String cmd = sc.nextLine();
	    	      String[] tokens = cmd.split(" ");
	    	      String print;
	    	      if (tokens[0].equals("purchase")) {
	      	        // TODO: send appropriate command to the server and display the
	      	        // appropriate responses form the server
	      	    	print = Store.getInstance().makePurchase(tokens[1], tokens[2], Integer.valueOf(tokens[3]));
	      	    	MyClientT.println(print);
	      	      } else if (tokens[0].equals("cancel")) {
	      	        // TODO: send appropriate command to the server and display the
	      	        // appropriate responses form the server
	      	    	  print = Store.getInstance().cancelPurchase(Integer.valueOf(tokens[1]));
	      	    	  MyClientT.println(print);
	      	      } else if (tokens[0].equals("search")) {
	      	        // TODO: send appropriate command to the server and display the
	      	        // appropriate responses form the server
	      	    	  print = Store.getInstance().search(tokens[1]);
	      	    	  MyClientT.println(print);
	      	      } else if (tokens[0].equals("list")) {
	      	        // TODO: send appropriate command to the server and display the
	      	        // appropriate responses form the server
	      	    	  ArrayList<String> s = Store.getInstance().list();	 
	      	    	  for(String x : s){
	      	    		MyClientT.println(s);
	      	    	  }
	      	      }
	    	      MyClientT.flush();
	    	      socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			sc.close();
		}
	}

}
