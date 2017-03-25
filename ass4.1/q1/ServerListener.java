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
    private PriorityQueue<Timestamp> q;  // queue for the requests
    private ArrayList<Timestamp> responses; // time of responses 
    private int id; 

    ServerListener(int serverPort, int myID) throws IOException {        
            this.serverSocket = new ServerSocket(serverPort);
            this.q = new PriorityQueue<>();
            this.responses = new ArrayList<>();
            this.id = myID;        
    }

    @Override
    public void run() {
        while(true) {            
                Socket s;
                try {
                	// for some reason, only works when have it in line
               // 	s = serverSocket.accept(); // not work
					while ( (s = serverSocket.accept()) != null) {
					    ObjectInputStream reader = new ObjectInputStream(s.getInputStream());
					    String line = reader.readUTF();					    
					       	if(line.equals("request")){
					            Timestamp request = (Timestamp)reader.readObject();
					            int id = request.getId();
					            q.add(request);
					            Timestamp reply = new Timestamp(this.id);
					            @SuppressWarnings("resource")
								Socket replySocket = new Socket(Server.ipMap.get(id), Server.serverPortMap.get(id));
					            ObjectOutputStream writer = new ObjectOutputStream(replySocket.getOutputStream());
					            writer.writeUTF("ack");
					            writer.writeObject(reply);
					            writer.writeObject(request);                            
					       	}else if(line.equals("ack")){
					            responses.add((Timestamp)reader.readObject());
					            Timestamp own = (Timestamp)reader.readObject();
					            if (!q.contains(own)) {
					                q.add(own);
					            }                            
					       	}else if(line.equals("release")){
					            Timestamp remove = (Timestamp)reader.readObject();
					            q.remove(remove);
					            for (Timestamp t : responses) {
					                @SuppressWarnings("resource")
									Socket removeSocket = new Socket(Server.ipMap.get(t.getId()), Server.serverPortMap.get(t.getId()));
					                ObjectOutputStream out = new ObjectOutputStream(removeSocket.getOutputStream());
					                out.writeUTF("release");
					                out.writeObject(remove);
					            }
					            responses.clear();
					       	}else if(line.equals("poll")){
					            DataOutputStream out = new DataOutputStream(s.getOutputStream());
					            Timestamp top = q.poll();                            
					            if (top == null) {
					                out.writeBoolean(true);
					            } else if (responses.size() == Server.ipMap.size() - 1 && top.getId() == this.id) {
					                out.writeBoolean(true);
					            } else {
					                out.writeBoolean(false);
					            }
					            
					       	}else if(line.equals("update")){
					            Store.setInstance((Store)reader.readObject());
							}
					       
					    }
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
                
         
        }
    }
}