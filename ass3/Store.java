package q3;

import java.util.HashMap;
import java.util.ArrayList;

public class Store {
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
		return inventory.get(key);  // returns null if no mapping
	}
	
	public void setValue(String key, Integer value){
		inventory.put(key,value);
	}
	public synchronized String makePurchase(String userName, String productName, Integer quantity){
		Integer invQuantity = getValue(productName); //checks the inventory hash map, returns the # of items in inventory
		String ret = null;
		if(invQuantity.equals(null)){  // no items 
			ret = "Not Available - We do not sell this product";
		}else if(invQuantity.compareTo(quantity) >= 0){ // (x, y):: the value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
			setValue(productName, (invQuantity.intValue()-quantity.intValue()));
			Order purchase = new Order(userName,productName, quantity, this.globalID);
			incrementGlobalID();
			orderList.add(purchase); // null pointer here, purchase  
			int id = getID();
			ret = ("You order has been placed, <" + id + "> <" + userName+ "> <" + productName + "> <" + quantity + ">\n" );
		}else{
			ret = "Not Available - Not enough items";
		}
		return ret;		
	}
	
	public synchronized String cancelPurchase(Integer orderID){
		boolean found = false;
		String productName;
		Integer val;
		Order cancel = null;
		String ret = null;
		for(Order x: orderList){
			if(x.getOrderID().compareTo(orderID)==0){
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
	
	public synchronized String search(String username){
		boolean found = false;
		String ret = null;
		for(Order x: orderList){
			if(x.getUserName().equals(username)){
				found = true;
				ret = "<" + x.getOrderID()+ "> <"+ x.getProductName()+ "> <" + x.getQuantity()+ ">";
			}
			
		}
		if(!found){
			ret = "No order found for <" + username + ">";
		}
		return ret;
		
	}
	
	public synchronized ArrayList<String> list(){
		ArrayList<String> ret = new ArrayList<String>();
		for (String productname: inventory.keySet()){
            Integer value = inventory.get(productname);  
            ret.add(productname + " " + value);  
		}
		return ret; 
	}
	

}
