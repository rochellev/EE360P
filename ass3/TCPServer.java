package q3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable{
	ServerSocket socket;
	
	public TCPServer(int port) throws IOException{
		socket = new ServerSocket(port);
		//System.out.println("In TCPServer, made socket");
	}
	
	@Override
	public void run() {
		while(true){
			int i = 0;
			//System.out.println("New TCP Connection");
			try {
				Socket s;
				TCPRequests tcpRequests;
				
				while((s = socket.accept()) != null){
					tcpRequests = new TCPRequests(s);
					Thread tcp = new Thread(tcpRequests);
					tcp.start();
					i++;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("in while number " + i + "");
		}
	}

}
