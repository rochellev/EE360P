package q1;

//class will be used for the <ip-address>:<port-number>
// idea is to have an array list of this type, sting seperated 

public class ServerInfo {

	private String ip;
	private String port;  
	private int portNum; // i think we probably want an int
	
	public ServerInfo(){
		ip = new String();
		port = new String();
		portNum = 0;
	}

	public ServerInfo(String input){
		String[] s = input.split(":");
		ip = s[0];
		portNum = Integer.parseInt(s[1]);
		//System.out.println(s[0]);
	} 
	
	public String getIP(){
		return ip;
		}
	
	public int getPortNum(){
		return portNum;
	}
	
}
