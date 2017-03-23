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

    ClientListener(int port, int id) {
        try {
            this.socket = new ServerSocket(port);
            this.id = id;
            //   System.out.println("Starting ClientListener: " + socket); // TODO: Remove
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            Socket s;
            try {
                while ((s = socket.accept()) != null) {
                    Scanner reader = new Scanner(s.getInputStream()); // Get input stream from connection
                    PrintWriter writer = new PrintWriter(s.getOutputStream()); // Get output stream from connection
                    String line = reader.nextLine();
                    Scanner inputCommand = new Scanner(line);
                    String tag = inputCommand.next();

                    // REQUEST CS
                    Timestamp request = new Timestamp(id);
                    Iterator<Integer> iterator = Server.ipMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        Integer host = iterator.next();
                        if (host != id) { // Send to all other hosts
                            try {
                                Socket sock = new Socket(Server.ipMap.get(host), Server.serverPortMap.get(host));
                                System.out.println("Sending request to: " + sock);
                                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                                out.writeUTF("request");
                                out.writeObject(request);
                            } catch (ConnectException e) { // Connection to another Server was denied
                                iterator.remove();
                                Server.serverPortMap.remove(host);
                            }
                        }
                    }

                    Thread.sleep(750); // TODO: Experiment

                    Socket self = new Socket(Server.ipMap.get(id), Server.serverPortMap.get(id));
                    self.setSoTimeout(1); // TODO: Experiment
                    // System.out.println("Sending polling request to: " + self);
                    ObjectOutputStream selfOut = new ObjectOutputStream(self.getOutputStream());
                    DataInputStream selfIn = new DataInputStream(self.getInputStream());
                    boolean b = false;
                    while (!b) {
                        selfOut.writeUTF("poll");
                         System.out.println("Trying. . .");
                        try {
                            b = selfIn.readBoolean();
                        } catch (SocketTimeoutException e) {
                            // . . .
                        }
                    }

                    // ENTER CS
                     System.out.println("Handling client request: " + tag); // TODO: Remove

                    if (tag.equals("purchase")) {
                        String user = inputCommand.next();
                        String product = inputCommand.next();
                        int quantity = inputCommand.nextInt();
                        int orderID = Store.getInstance().purchase(user, product, quantity);
                        switch (orderID) {
                            case -1:
                                writer.println("Not Available - We do not sell this product");
                                break;
                            case 0:
                                writer.println("Not Available - Not enough items");
                                break;
                            default:
                                writer.println("Your order has been placed, " + orderID + " " + user + " " + product + " "
                                        + quantity);
                                break;
                        }
                    } else if (tag.equals("cancel")) {
                        int orderID = inputCommand.nextInt();
                        writer.println(Store.getInstance().cancel(orderID));
                    } else if (tag.equals("search")) {
                        String user = inputCommand.next();
                        ArrayList<String> userOrders = Store.getInstance().search(user);
                       // userOrders.forEach(str -> writer.println(str));
                        System.out.println("search");
                    } else { // if (tag.equals("list")) {
                        String[] invList = Store.getInstance().list();
                        for (String str : invList) {
                            writer.println(str);
                        }
                    }
                    writer.flush();
                    writer.close();
                    // EXIT CS

                    // UPDATE STORES
                    iterator = Server.ipMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        Integer host = iterator.next();
                        if (host != id) { // Send to all other hosts
                            try {
                                Socket sock = new Socket(Server.ipMap.get(host), Server.serverPortMap.get(host));
                                // System.out.println("Sending update to: " + sock);
                                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                                out.writeUTF("update");
                                out.writeObject(Store.getInstance());
                            } catch (ConnectException e) { // Connection to another Server was denied
                                iterator.remove();
                                Server.serverPortMap.remove(host);
                            }
                        }
                    }

                    // RELEASE
                    Socket release = new Socket(Server.ipMap.get(id), Server.serverPortMap.get(id));
                    System.out.println("Sending release request to: " + release);
                    ObjectOutputStream releaseOut = new ObjectOutputStream(release.getOutputStream());
                    releaseOut.writeUTF("release");
                    releaseOut.writeObject(request);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
