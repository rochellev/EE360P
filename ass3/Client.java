package q3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{
  public static void main (String[] args) throws IOException {
    String hostAddress;
    int tcpPort;
    int udpPort;

    if (args.length != 3) {
      System.out.println("ERROR: Provide 3 arguments");
      System.out.println("\t(1) <hostAddress>: the address of the server");
      System.out.println("\t(2) <tcpPort>: the port number for TCP connection");
      System.out.println("\t(3) <udpPort>: the port number for UDP connection");
      System.exit(-1);
    }

    hostAddress = args[0];
    tcpPort = Integer.parseInt(args[1]);
    udpPort = Integer.parseInt(args[2]);
    
    byte[] buf = new byte[1024]; 
    Socket echoSocket = null;
    InputStream input = null;
	OutputStream output = null;
	DatagramSocket MyClientU = null;	
	PrintStream MyClientT = null;
	InetAddress addr = null;
	
	try {
		addr = InetAddress.getByName(hostAddress);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	echoSocket = new Socket(addr,tcpPort);
	input = echoSocket.getInputStream();
	output = echoSocket.getOutputStream();
	
    Scanner sc = new Scanner(System.in);
    Scanner tcpConnection = new Scanner(input);
    MyClientT = new PrintStream(output);
    boolean modeFlag = false; //flag default TCP = false and UDP = true
    try{
    	while(sc.hasNextLine()) {
    	      String cmd = sc.nextLine();
    	      String[] tokens = cmd.split(" ");
    	      for(String x: tokens){
    	    	  System.out.println(x);
    	      }
    	      if(tokens[0].equals("setmode")) {
      	        // TODO: set the mode of communication for sending commands to the server 
      	        // and display the name of the protocol that will be used in future
      	    	  if(tokens.length == 2 && tokens[1].equals("U")){
      	    		  System.out.println("Protocol set to UDP");
      	    		  modeFlag = true;    			     	
      	    	  }else{
      	    		  System.out.println("Protocol set to TCP");
      	    		  modeFlag = false;  // TCP default
      	    	  }
    	      }else if(tokens[0].equals("purchase")||tokens[0].equals("cancel")||
    	    		  tokens[0].equals("search")||tokens[0].equals("list")){
    	    	  if(modeFlag){ // true if UDP
      	    		byte[] sendInfo = cmd.getBytes();
      	    		MyClientU = new DatagramSocket();
      	    		DatagramPacket sendP = new DatagramPacket(sendInfo, sendInfo.length,addr,udpPort);
      	    		DatagramPacket recP = new DatagramPacket(buf, buf.length);
      	    		MyClientU.send(sendP);
      	    		MyClientU.receive(recP);
      	    		System.out.println(new String(recP.getData(),0,recP.getLength()));
      	    		MyClientU.close();		    		
      	    	}else{
      	    		MyClientT.println(cmd);
      	    		MyClientT.flush();
  					while (tcpConnection.hasNextLine()) {
  						System.out.println(tcpConnection.nextLine());					
  					}
      	    	} 
    	      }else{
    	    	  System.out.println("ERROR: No such command");
    	      }
    	     
    	}
    
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	sc.close();
    	echoSocket.close();
    	tcpConnection.close();
    	input.close();
    	output.close();
    }
  }
}
