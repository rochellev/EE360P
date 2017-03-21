package q1;

import java.util.ArrayList;
import java.util.Scanner;

public class Client {

	//private static ArrayList<String> serverList = new ArrayList<String>();
	private static ArrayList<ServerInfo> serverList; 

	public void tcpComm(String cmd){
		
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int numServer = sc.nextInt(); //first line is number of servers
		serverList = new ArrayList<ServerInfo>();

		for (int i = 0; i < numServer; i++) {
			// TODO: parse inputs to get the ips and ports of servers
			String line = sc.nextLine();  //<ip-address>:<port-number>
			ServerInfo server = new ServerInfo(line);
			serverList.add(server);  // the ServerInfo class has getters for id and port#
		
			
			/*
			
			String serverIP ;
			*/
			//
		}

		while (sc.hasNextLine()) {
			String cmd = sc.nextLine();
			String[] tokens = cmd.split(" ");

			if (tokens[0].equals("purchase")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
				
				
			} else if (tokens[0].equals("cancel")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
			} else if (tokens[0].equals("search")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
			} else if (tokens[0].equals("list")) {
				// TODO: send appropriate command to the server and display the
				// appropriate responses form the server
			} else {
				System.out.println("ERROR: No such command");
			}
		}
	}
}
