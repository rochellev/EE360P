package q3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer implements Runnable {
	DatagramSocket socket;
	DatagramPacket sendP; // packet for sending data
	DatagramPacket recP;  // packet for receiving data
	
	public UDPServer(int udpPort) throws SocketException {
		socket = new DatagramSocket(udpPort);
		//System.out.println("hey in udp Server");
	}

	@Override
	public void run() {
		UDPRequests udpRequests;	// handler
		while(true){
			byte[] buf = new byte[1024];
			sendP = new DatagramPacket(buf,buf.length);
			//System.out.println("New UDP Connection");
			try {
				socket.receive(sendP);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			udpRequests = new UDPRequests(socket,sendP);
			Thread udp = new Thread(udpRequests);
			udp.start();
		}
	}

}
