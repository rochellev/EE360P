package q1;

import java.util.*;
import java.io.*;

//import q3.Store;

public class Server {

    static Map<Integer, String> ipMap = new HashMap<>();
    private static Map<Integer, Integer> clientPortMap = new HashMap<>();
    static Map<Integer, Integer> serverPortMap = new HashMap<>();
    private static HashMap<String, Integer> inventory = new HashMap<>();
    private static final int INTER_SERVER_BASE_PORT = 9000;

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        int myID = sc.nextInt() - 1;
        int numServer = sc.nextInt();
        String inventoryPath = sc.next();
        
        System.out.println("[DEBUG] my id: " + myID);
        System.out.println("[DEBUG] numServer: " + numServer);
        System.out.println("[DEBUG] inventory path: " + inventoryPath);

        // Parse inputs to get the ips and ports of servers
        for (int i = 0; i < numServer; i++) {
            String line = sc.next();
            String[] parts = line.split(":");
            ipMap.put(i, parts[0]);
            clientPortMap.put(i, Integer.parseInt(parts[1]));
            serverPortMap.put(i, INTER_SERVER_BASE_PORT + i);
        }
        sc.close();

        try (Scanner file = new Scanner(new FileReader("src/q1/inventory.txt"))) {
        	String line;
            while ((file.hasNextLine())) {
            	line = file.nextLine();
                inventory.put(line.split(" ")[0], Integer.parseInt(line.split(" ")[1]));
            }
        }
        Store TheStore = Store.getInstance();
	    TheStore.setID(1);
	    TheStore.setInv(inventory);

        Thread clientListener = new Thread(new ClientListener(clientPortMap.get(myID), myID));
        Thread serverListener = new Thread(new ServerListener(serverPortMap.get(myID), myID));

        clientListener.start();
        serverListener.start();
    }
}
