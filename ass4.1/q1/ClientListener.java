package q1;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class ClientListener implements Runnable {
	private ServerSocket socket;
	private int id;

	ClientListener(int port, int id) throws IOException {
		this.socket = new ServerSocket(port);
		this.id = id;
	}

	@Override
	public void run() {
		while (true) {
			Socket s;
			try {
				while ((s = socket.accept()) != null) {
					Scanner reader = new Scanner(s.getInputStream()); // need to get stream in/out
					PrintWriter writer = new PrintWriter(s.getOutputStream()); 
					String line = reader.nextLine();
					Scanner inputCommand = new Scanner(line);
					String tag = inputCommand.next();

					Timestamp request = new Timestamp(id); // preserve ordering
					Iterator<Integer> iterator = Server.ipMap.keySet().iterator(); 
					while (iterator.hasNext()) { // going through list to 
						Integer host = iterator.next();
						if (host != id) { // broadcast to all
							try {
								@SuppressWarnings("resource")
								Socket sock = new Socket(Server.ipMap.get(host), Server.serverPortMap.get(host));
								ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
								out.writeUTF("request");
								out.writeObject(request);
							} catch (ConnectException e) { // failed to connect
								iterator.remove();
								Server.serverPortMap.remove(host);
							}
						}
					}

					@SuppressWarnings("resource")
					Socket self = new Socket(Server.ipMap.get(id), Server.serverPortMap.get(id));
					self.setSoTimeout(1);
					ObjectOutputStream selfOut = new ObjectOutputStream(self.getOutputStream());
					DataInputStream selfIn = new DataInputStream(self.getInputStream());
					boolean b = false;
					while (!b) {
						selfOut.writeUTF("poll");
						try {
							b = selfIn.readBoolean();
						} catch (SocketTimeoutException e) {
							//e.printStackTrace();  //not printing, expected to not always meet time
						}
					}

					// Critical Section: interacting with store
					if (tag.equals("purchase")) {
						String user = inputCommand.next();
						String product = inputCommand.next();
						int quantity = inputCommand.nextInt();
						String ret = Store.getInstance().makePurchase(user, product, quantity);
						writer.println(ret);
					} else if (tag.equals("cancel")) {
						int orderID = inputCommand.nextInt();
						writer.println(Store.getInstance().cancelPurchase(orderID));
					} else if (tag.equals("search")) {
						String user = inputCommand.next();
						ArrayList<String> userOrders = Store.getInstance().search(user);
						for (String val : userOrders) {
							writer.println(val);
						}
					} else { // list command
						ArrayList<String> inv = Store.getInstance().list();
						for (String str : inv) {
							writer.println(str);
						}
					}
					writer.flush();
					writer.close();
					// after interact with store, now update
					iterator = Server.ipMap.keySet().iterator();
					while (iterator.hasNext()) {
						Integer host = iterator.next();
						if (host != id) {
							try {
								@SuppressWarnings("resource")
								Socket sock = new Socket(Server.ipMap.get(host), Server.serverPortMap.get(host));
								ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
								out.writeUTF("update");
								out.writeObject(Store.getInstance());
							} catch (ConnectException e) { 
								iterator.remove();
								Server.serverPortMap.remove(host); //failed so remove
							}
						}
					}

					@SuppressWarnings("resource")
					Socket release = new Socket(Server.ipMap.get(id), Server.serverPortMap.get(id)); //time to release
					ObjectOutputStream releaseOut = new ObjectOutputStream(release.getOutputStream());
					releaseOut.writeUTF("release");
					releaseOut.writeObject(request);

				}
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}
}