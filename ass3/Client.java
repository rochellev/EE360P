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

public class Client {
	public static void main(String[] args) throws IOException {
		String hostAddress;
		int tcpPort;
		int udpPort;

		if (args.length != 3) {
			System.out.println("ERROR: Provide 3 arguments");
			System.out.println("\t(1) <hostAddress>: the address of the server"); // 1
			System.out.println("\t(2) <tcpPort>: the port number for TCP connection"); // 2
			System.out.println("\t(3) <udpPort>: the port number for UDP connection"); // 3
			System.exit(-1);
		}
	//	System.out.println("Running client");
		hostAddress = args[0];
		tcpPort = Integer.parseInt(args[1]);
		udpPort = Integer.parseInt(args[2]);

		byte[] buf = new byte[1024];
		Socket echoSocket = null;
		InputStream input = null;
		OutputStream output = null;
		DatagramSocket MyClientU = null; // data socket
		PrintStream MyClientT = null;
		InetAddress addr = null;

		try {
			addr = InetAddress.getLocalHost(); // server is on local host

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		echoSocket = new Socket(addr, tcpPort); // socket to server, local host
		input = echoSocket.getInputStream();
		output = echoSocket.getOutputStream();

		Scanner sc = new Scanner(System.in);
		Scanner tcpConnection = new Scanner(input); // input data
		MyClientT = new PrintStream(output);
		boolean modeFlag = false; // flag default TCP = false and UDP = true
		try {
			while (sc.hasNextLine()) {
				String cmd = sc.nextLine();
				String[] tokens = cmd.split(" ");
				/*
				for (String x : tokens) { // just printing the token
					System.out.println(x);
				}
				*/
				if (tokens[0].equals("setmode")) {
					// TODO: set the mode of communication for sending commands
					// to the server
					// and display the name of the protocol that will be used in
					// future
					if (tokens.length == 2 && tokens[1].equals("U")) {
						System.out.println("Protocol set to UDP");
						modeFlag = true;
					} else {
						System.out.println("Protocol set to TCP");
						modeFlag = false; // TCP default
					}
				} else if (tokens[0].equals("purchase")) {
					if (tokens.length == 4) {
						if (modeFlag) { // true if UDP
							byte[] sendInfo = cmd.getBytes();
							MyClientU = new DatagramSocket();
							DatagramPacket sendP = new DatagramPacket(sendInfo, sendInfo.length, addr, udpPort);
							DatagramPacket recP = new DatagramPacket(buf, buf.length);
							MyClientU.send(sendP);
							MyClientU.receive(recP);
							System.out.println(new String(recP.getData(), 0, recP.getLength()));
							MyClientU.close();
						} else { // means it's TCP
							MyClientT.println(cmd);
							MyClientT.flush();
							while (tcpConnection.hasNextLine()) {
								System.out.println(tcpConnection.nextLine());
							}
						}
					} else {
						System.out.println("ERROR: Purchase missing arguments. No such command");
					}
				} else if (tokens[0].equals("cancel")) {
					if (tokens.length == 2) {
						if (modeFlag) { // true if UDP
							byte[] sendInfo = cmd.getBytes();
							MyClientU = new DatagramSocket();
							DatagramPacket sendP = new DatagramPacket(sendInfo, sendInfo.length, addr, udpPort);
							DatagramPacket recP = new DatagramPacket(buf, buf.length);
							MyClientU.send(sendP);
							MyClientU.receive(recP);
							System.out.println(new String(recP.getData(), 0, recP.getLength()));
							MyClientU.close();
						} else { // means it's TCP
							MyClientT.println(cmd);
							MyClientT.flush();
							while (tcpConnection.hasNextLine()) {
								System.out.println(tcpConnection.nextLine());
							}
						}
					} else {
						System.out.println("ERROR: Cancel missing arguments. No such command");
					}
				} else if (tokens[0].equals("search")) {
					if (tokens.length == 2) {
						if (modeFlag) { // true if UDP
							byte[] sendInfo = cmd.getBytes();
							MyClientU = new DatagramSocket();
							DatagramPacket sendP = new DatagramPacket(sendInfo, sendInfo.length, addr, udpPort);
							DatagramPacket recP = new DatagramPacket(buf, buf.length);
							MyClientU.send(sendP);
							MyClientU.receive(recP);
							System.out.println(new String(recP.getData(), 0, recP.getLength()));
							MyClientU.close();
						} else { // means it's TCP
							MyClientT.println(cmd);
							MyClientT.flush();
							while (tcpConnection.hasNextLine()) {
								System.out.println(tcpConnection.nextLine());
							}
						}
					} else {
						System.out.println("ERROR: Search missing arguments. No such command");
					}
				} else if (tokens[0].equals("list")) {
					if (modeFlag) { // true if UDP
						byte[] sendInfo = cmd.getBytes();
						MyClientU = new DatagramSocket();
						DatagramPacket sendP = new DatagramPacket(sendInfo, sendInfo.length, addr, udpPort);
						DatagramPacket recP = new DatagramPacket(buf, buf.length);
						MyClientU.send(sendP);
						MyClientU.receive(recP);
						System.out.println(new String(recP.getData(), 0, recP.getLength()));
						MyClientU.close();
					} else { // means it's TCP
						MyClientT.println(cmd);
						MyClientT.flush();
						while (tcpConnection.hasNextLine()) {
							System.out.println(tcpConnection.nextLine());
						}
					}
				} else {
					System.out.println("ERROR: No such command");

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			echoSocket.close();
			tcpConnection.close();
			input.close();
			output.close();
		}
	}
}
