package q3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer implements Runnable {
	DatagramSocket socket;
	DatagramPacket sendP;
	DatagramPacket recP;
	
	public UDPServer(int udpPort) throws SocketException {
		// TODO Auto-generated constructor stub
		socket = new DatagramSocket(udpPort);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		UDPRequests udpRequests;
		while(true){
			byte[] buf = new byte[1024];
			sendP = new DatagramPacket(buf,buf.length);
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
