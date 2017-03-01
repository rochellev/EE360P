package q3;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class Server {
	  public static void main (String[] args) throws IOException {
	    int tcpPort;
	    int udpPort;	    
	    
	    if (args.length != 3) {
	      System.out.println("ERROR: Provide 3 arguments");
	      System.out.println("\t(1) <tcpPort>: the port number for TCP connection");
	      System.out.println("\t(2) <udpPort>: the port number for UDP connection");
	      System.out.println("\t(3) <file>: the file of inventory");

	      System.exit(-1);
	    }
	    InetAddress tcpPortAddr = InetAddress.getLocalHost();
	    InetAddress udpPortAddr = InetAddress.getLocalHost();
	    
	    tcpPort = Integer.parseInt(args[0]);
	    udpPort = Integer.parseInt(args[1]);
	    String fileName = args[2];  // use to set up inventory
	    System.out.println("tcp" + tcpPort);
	    // parse the inventory file, done once
	    HashMap<String, Integer> inventory = new HashMap<String, Integer>();
	    String line = null;
	    String[] arr;
	    Integer value =0;
	    BufferedReader buffReader = null;
	    try { 
	    	FileReader fileReader = new FileReader(fileName); // need to wrap with buffer
			buffReader = new BufferedReader(fileReader); 
			line = buffReader.readLine();  // gets a line terminated by enter
			while(line != null){
				// the line has a string and integer
				//System.out.println("the line looks like:" + line + " ");
				arr = line.split(" ");
				
				//value = Integer.getInteger(arr[1]);   //this resulted in null for some reason idk
				value = Integer.valueOf(arr[1]);
			    // System.out.println(arr[1]);
			    //System.out.println(value);
			    inventory.put(arr[0], value);
			    line = buffReader.readLine();
			    System.out.println("Server getting inputs");
			}
			//buffReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("hey girl, can't find the file " + fileName + " :( ");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			buffReader.close();
		}
	    
	    Store EE360PStore = Store.getInstance();
	    EE360PStore.setID(1);
	    EE360PStore.setInv(inventory);
	    
	    // TODO: handle request from clients
	    TCPServer tcpServer = new TCPServer(2);
	    Thread TCP = new Thread(tcpServer);
	    UDPServer udpServer = new UDPServer(2);
	    Thread UDP =  new Thread(udpServer);
	    //TCP.start();
	    //UDP.start();
	    while(true){
	    try {
			TCP.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    try {
			UDP.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	    
	  }
	}
