package q1;

import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;


public class SocketTimeoutTask implements Callable<Boolean>{
	Socket socket;
	Scanner din;
	
	public SocketTimeoutTask(Socket sock, Scanner in){
		socket = sock;
		din = in;
	}
	
	@Override
	public Boolean call() throws Exception {
		while (din.hasNextLine()) {
			System.out.println(din.nextLine());
		}
		return true;
	}
	
}