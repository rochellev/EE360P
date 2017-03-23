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
	
	private final static String ARG_ERROR = "ERROR: Incorrect number of arguments";
	
	private final static int PURCHASE_ARGS = 4;
	private final static int CANCEL_ARGS = 2;
	private final static int SEARCH_ARGS = 2;
	private final static int TIMEOUT = 100;
	private static ArrayList<String> servers;
	
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	
	public static void attemptCommunication(String command){
		Iterator<String> i = servers.iterator();
		Socket sock = null;
		InputStream in = null;
		OutputStream out = null;
		PrintStream pout = null;
		Scanner din = null;
		while(i.hasNext()){
			String serverDetails = i.next();
			String ip = serverDetails.split(":")[0];
			int port = Integer.parseInt(serverDetails.split(":")[1]);
			try {
				InetAddress addr = InetAddress.getByName(ip);
				sock = new Socket();
				sock.connect(new InetSocketAddress(addr, port), TIMEOUT);
				in = sock.getInputStream();
				out = sock.getOutputStream();
				pout = new PrintStream(out);
				din = new Scanner(in);
				pout.println(command);
				pout.flush();
				
				while (din.hasNextLine()) {
					System.out.println(din.nextLine());
				}
				sock.close();
				return;
				/*
				Future<Boolean> future = executor.submit(new SocketTimeoutTask(sock, din));
				try {
					if(future.get(TIMEOUT, TimeUnit.MILLISECONDS)){
						
						return;
					}
				} catch (InterruptedException | ExecutionException
						| TimeoutException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					continue;
				}
				*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				continue;
			}
		}
	}
	
	
	public static void main (String[] args) {
		Scanner sc = new Scanner(System.in);
		int numServer = Integer.parseInt(sc.nextLine());
		servers = new ArrayList<String>();
		
	    while (servers.size() < numServer) {
	      // TODO: parse inputs to get the ips and ports of servers
	    	String server = sc.nextLine();
	    	servers.add(server);
	    }

	    while(sc.hasNextLine()) {
	      String cmd = sc.nextLine();
	      String[] tokens = cmd.split(" ");
	      if (tokens[0].equals("purchase")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  if (tokens.length != PURCHASE_ARGS) {
					System.out.println(ARG_ERROR);
					System.out.println(
							"purchase <user-name> <product-name> <quantity>");
					continue;
				}
	    	  attemptCommunication(cmd);
	    	  
	      } else if (tokens[0].equals("cancel")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  if (tokens.length != CANCEL_ARGS) {
					System.out.println(ARG_ERROR);
					System.out.println("cancel <order-id>");
					continue;
				}
	    	  attemptCommunication(cmd);
	    	  
	      } else if (tokens[0].equals("search")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  if (tokens.length != SEARCH_ARGS) {
					System.out.println(ARG_ERROR);
					System.out.println("search <user-name>");
					continue;
				}
	    	  attemptCommunication(cmd);
	    	  
	      } else if (tokens[0].equals("list")) {
	        // TODO: send appropriate command to the server and display the
	        // appropriate responses form the server
	    	  attemptCommunication(cmd);
	      } else {
	        System.out.println("ERROR: No such command");
	      }
	    }
	}
}
