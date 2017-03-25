package q1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class Store implements Serializable {

	private int globalID;
	private HashMap<String, Integer> inventory;
	private ArrayList<Order> orderList;
	private static Store singleton = null;   // doing the singleton thing

	public Store(){
		globalID = 0;
		inventory = new HashMap<String, Integer>();
		orderList = new ArrayList<Order>();
	}

    public Store(int id, HashMap<String, Integer> inv, ArrayList<Order> list){
		globalID = id;
		this.inventory = inv;
		this.orderList = list;
	}

    public static Store getInstance() {
		if (singleton == null) {
			synchronized (Store.class) {
				if (singleton == null) {
					singleton = new Store();
				}
			}
		}
		return singleton;
	}

    public void setID(int id){
		globalID = id;
	}
	
	public int getID(){
		return globalID;
	}
	public void setInv(HashMap<String, Integer> inv){
		inventory = inv;
	}
	public void incrementGlobalID(){
		globalID++;
	}
	public Integer getValue(String key){
		boolean flag= inventory.containsKey(key);
		if(!flag)
			return -1;
		else
			return inventory.get(key);  // returns null if no mapping
	}
	
	public void setValue(String key, Integer value){
		inventory.put(key,value);
	}

    public class Order implements Serializable{
	    private String userName, productName;
		private int quantity, orderID;
	
		public Order(String userName, String productName, int quantity, int orderID){
			this.userName = userName;
			this.productName = productName;
			this.quantity = quantity;
			this.orderID = orderID;
		}
		
		public Order(){
			this.userName = null;
			this.productName = null;
			this.quantity = 0;
			this.orderID = 0;
		}
		
		public String getUserName(){
			return userName;
			
		}
		
		public String getProductName(){
			return productName;
			
		}
		
		public int getQuantity(){
			return quantity;
			
		}
		
		public int getOrderID(){
			return orderID;
		}
    }

    /**
     *
     * @param user The user to search the store for
     * @param product The product the user bought
     * @param quantity The amount the user bought, if successful
     * @return 0 if quantity too low, -1 if item does not exist or order ID if
     *         purchase is successful
     */

    public synchronized String makePurchase(String userName, String productName, int quantity){
		Integer invQuantity = getValue(productName); //checks the inventory hash map, returns the # of items in inventory
		
		String ret = "Not Available - We do not sell this product";
		if(invQuantity == -1){
			
		}else if(invQuantity >=quantity){ // (x, y):: the value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
			setValue(productName, invQuantity-quantity);
			Order purchase = new Order(userName,productName, quantity, this.globalID);
			incrementGlobalID();
			orderList.add(purchase); // null pointer here, purchase  
			int id = getID();
			ret = ("You order has been placed, <" + id + "> <" + userName+ "> <" + productName + "> <" + quantity + ">\n" );
		}else if (invQuantity<quantity){
			ret = "Not Available - Not enough items";
		}
		return ret;		
	}

    public synchronized String cancelPurchase(int orderID){
		boolean found = false;
		String productName;
		Integer val;
		Order cancel = null;
		String ret = null;
		for(Order x: orderList){
			if(x.getOrderID()==orderID){
				cancel = x;
				productName = x.getProductName();
				val = x.getQuantity();
				orderList.remove(x);
				found = true;
				ret = "Order <" + orderID + "> is canceled";
				break;			
			}
		}
		if(!found){
			ret = "<" + orderID + "> not found, no such order";
		}else{
			Integer invQuantity = getValue(cancel.getProductName()); //inv current value
			Integer prodtQuantity = cancel.getQuantity();
			
			inventory.put(cancel.getProductName(), invQuantity + prodtQuantity);
			
		}
		return ret;
	}

    public synchronized  ArrayList<String> search(String username){
    	ArrayList<String> orders = new ArrayList<>();
		boolean found = false;
		String ret = null;
		for(Order x: orderList){
			if(x.getUserName().equals(username)){
				found = true;
				ret = "<" + x.getOrderID()+ "> <"+ x.getProductName()+ "> <" + x.getQuantity()+ ">";
				orders.add(ret);
			}
			
		}
		if(!found){
			ret = "No order found for <" + username + ">";
		}
		return orders;
		
	}

    public synchronized ArrayList<String> list(){
		ArrayList<String> ret = new ArrayList<String>();
		for (String productname: inventory.keySet()){
            Integer value = inventory.get(productname);  
            ret.add(productname + " " + value);  
		}
		return ret; 
	}

	public static void setInstance(Store readObject) {
		// TODO Auto-generated method stub
		singleton = readObject;
	}
}