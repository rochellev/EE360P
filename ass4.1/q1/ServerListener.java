package q1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PriorityQueue;

class ServerListener implements Runnable {
    private ServerSocket serverSocket;
    private PriorityQueue<Timestamp> q;
    private ArrayList<Timestamp> responses;
    private int id;

    ServerListener(int serverPort, int myID) {
        try {
            this.serverSocket = new ServerSocket(serverPort);
            this.q = new PriorityQueue<>();
            this.responses = new ArrayList<>();
            this.id = myID;
            // System.out.println("Starting ServerListener: " + serverSocket); // TODO: Remove
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket s;
                while ((s = serverSocket.accept()) != null) {
                    ObjectInputStream reader = new ObjectInputStream(s.getInputStream());
                    String line = reader.readUTF();
                    // System.out.println("Handling inter-server " + line); // TODO: Remove
                    switch (line) {
                        case "request":
                            Timestamp request = (Timestamp)reader.readObject();
                            int id = request.getId();
                            q.add(request);
                            Timestamp reply = new Timestamp(this.id);
                            Socket replySocket = new Socket(Server.ipMap.get(id), Server.serverPortMap.get(id));
                            ObjectOutputStream writer = new ObjectOutputStream(replySocket.getOutputStream());
                            writer.writeUTF("ack");
                            writer.writeObject(reply);
                            writer.writeObject(request);
                            break;
                        case "ack":
                            responses.add((Timestamp)reader.readObject());
                            Timestamp own = (Timestamp)reader.readObject();
                            if (!q.contains(own)) {
                                q.add(own);
                            }
                            break;
                        case "release":
                            Timestamp remove = (Timestamp)reader.readObject();
                            q.remove(remove);
                            // System.out.println("Removing: " + remove);
                            // System.out.println("Q: " + q);
                            for (Timestamp t : responses) {
                                Socket removeSocket = new Socket(Server.ipMap.get(t.getId()), Server.serverPortMap.get(t.getId()));
                                ObjectOutputStream out = new ObjectOutputStream(removeSocket.getOutputStream());
                                out.writeUTF("release");
                                out.writeObject(remove);
                            }
                            responses.clear();
                            break;
                        case "poll":
                            DataOutputStream out = new DataOutputStream(s.getOutputStream());
                            Timestamp top = q.poll();
                            // System.out.println("Top of Q: " + top);
                            // System.out.println("Responses: " + responses);

                            if (top == null) {
                                out.writeBoolean(true);
                            } else if (responses.size() == Server.ipMap.size() - 1 && top.getId() == this.id) {
                                out.writeBoolean(true);
                            } else {
                                out.writeBoolean(false);
                            }
                            break;
                        case "update":
                            Store.setInstance((Store)reader.readObject());
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

