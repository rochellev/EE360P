package q3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable{
	ServerSocket socket;
	
	public TCPServer(int port) throws IOException{
		socket = new ServerSocket(port);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			Socket s;
			try {
				s = socket.accept();
				TCPRequests tcpRequests = null;
				while(s != null){
					tcpRequests = new TCPRequests(s);
					Thread tcp = new Thread(tcpRequests);
					tcp.start();
					s = socket.accept();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
