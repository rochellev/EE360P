package q1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
	
	private static ArrayList<ServerInfo> serverList; 

	//method does the TCP connection things, t
	public static void tcpComm(String cmd){
		Socket socket = null;
		InputStream inStream = null;
		OutputStream outStream = null;
		PrintStream printOut = null;
		Scanner inData = null;
		
		//iterate through all the servers, trying to connect
		for(ServerInfo s : serverList){
			try {
				InetAddress addr = InetAddress.getByName(s.getIP());
				InetSocketAddress sockAddr = new InetSocketAddress(addr, s.getPortNum());
				socket = new Socket();
				socket.connect(sockAddr, 100);
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
				printOut = new PrintStream(outStream);
				inData = new Scanner(inStream);
				printOut.println(cmd);
				printOut.flush();	//remember to flush!
				while(inData.hasNextLine()){
					System.out.println(inData.nextLine());
				}
				//when done close socket and return
				socket.close();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int numServer = sc.nextInt(); //first line is number of servers
		serverList = new ArrayList<ServerInfo>();

		// the parsing part
		for (int i = 0; i < numServer; i++) {
			String line = sc.nextLine();  //<ip-address>:<port-number>
			ServerInfo server = new ServerInfo(line);
			serverList.add(server);  // the ServerInfo class has getters for id and port#
		}

		while (sc.hasNextLine()) {
			String cmd = sc.nextLine();
			String[] tokens = cmd.split(" ");

			if (tokens[0].equals("purchase")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
				if(tokens.length != 4){
					System.out.println("Incorrect purchase command");
				}
				tcpComm(cmd);  //needs to be static to reference here idk
				
			} else if (tokens[0].equals("cancel")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
				if(tokens.length != 2){
					System.out.println("Incorrect cancel command");
				}
				tcpComm(cmd);
				
			} else if (tokens[0].equals("search")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
				if(tokens.length != 2){
					System.out.println("Incorrect search command");
				}
				tcpComm(cmd);
				
			} else if (tokens[0].equals("list")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
				if(tokens.length != 1){
					System.out.println("Incorrect list command");
				}
				tcpComm(cmd);
				
			} else {
				System.out.println("ERROR: No such command");
			}
		}
	}
}
