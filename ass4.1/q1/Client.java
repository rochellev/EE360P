package q1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

	private static ArrayList<String> serverList;
	
	
	// repeated for each command
	public static void tcpComm(String cmd){
		Socket socket = null;
		InputStream inStream = null;
		OutputStream outStream = null;
		PrintStream printOut = null;
		Scanner inData = null;
		
		for(String s : serverList){
			String serverDetails = s;
			String ip = serverDetails.split(":")[0];
			int port = Integer.parseInt(serverDetails.split(":")[1]);
			try {
				InetAddress addr = InetAddress.getByName(ip);
				socket = new Socket();
				socket.connect(new InetSocketAddress(addr, port), 100);
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
				printOut = new PrintStream(outStream);
				inData = new Scanner(inStream);
				printOut.println(cmd);
				printOut.flush(); //remember to flush!
			
				while (inData.hasNextLine()) {
					System.out.println(inData.nextLine());
				}
				
				socket.close();
				return;
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main (String[] args) {
		Scanner sc = new Scanner(System.in);
		int numServer = Integer.parseInt(sc.nextLine());
		serverList = new ArrayList<String>();
		
	    while (serverList.size() < numServer) {
	      // TODO: parse inputs to get the ips and ports of servers
	    	String server = sc.nextLine();
	    	serverList.add(server);
	    }

	    while(sc.hasNextLine()) {
	      String cmd = sc.nextLine();
	      String[] tokens = cmd.split(" ");
	      if (tokens[0].equals("purchase")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  if (tokens.length != 4) {
					System.out.println("Incorrect purchase command");
				}
	    	  tcpComm(cmd);
	    	  
	      } else if (tokens[0].equals("cancel")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  if (tokens.length != 2) {
	    		  System.out.println("Incorrect cancel command");
				}
	    	  tcpComm(cmd);
	    	  
	      } else if (tokens[0].equals("search")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  if (tokens.length != 2) {
					System.out.println("Incorrect search command");
				}
	    	  tcpComm(cmd);
	    	  
	      } else if (tokens[0].equals("list")) {  //only has one argument
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  tcpComm(cmd);
	      } else {
	        System.out.println("ERROR: No such command");
	      }
	    }
	}
}
