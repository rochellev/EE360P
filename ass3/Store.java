package q3;

import java.util.HashMap;
import java.util.ArrayList;

public class Store {
	private int globalID;
	private HashMap<String, Integer> inventory;
	private ArrayList<Order> orderList;
	
	public Store(){
		globalID = 0;
		this.inventory = null;
		this.orderList = null;
	}
	
	public Store(int id, HashMap<String, Integer> inv, ArrayList<Order> list){
		globalID = id;
		this.inventory = inv;
		this.orderList = list;
	}
	
	public void incrementGlobalID(){
		globalID++;
	}
	public Integer getValue(String key){
		return inventory.get(key);
	}
	
	public void setValue(String key, Integer value){
		inventory.put(key,value);
	}
	public synchronized void makePurchase(String userName, String productName, Integer quantity){
		Integer invQuantity = getValue(productName);
		if(getValue(productName)==null){
			System.out.println("Not Available - We do not sell this product");
		}else if(invQuantity.compareTo(quantity) >= 0){
			setValue(productName, (invQuantity.intValue()-quantity.intValue()));
			Order purchase = new Order(userName,productName, quantity, this.globalID);
			incrementGlobalID();
			orderList.add(purchase);
		}else{
			System.out.println("Not Available - Not enough items");
		}		
	}
	
	public synchronized void cancelPurchase(Integer orderID){
		boolean found = false;
		String productName;
		Integer val;
		Order cancel = null;
		for(Order x: orderList){
			if(x.getOrderID().compareTo(orderID)==0){
				cancel = x;
				productName = x.getProductName();
				val = x.getQuantity();
				orderList.remove(x);
				found = true;
				System.out.println("Order <" + orderID + "> is canceled");
				break;			
			}
		}
		if(!found){
			System.out.println("<" + orderID + "> not found, no such order");
		}else{
			Integer invQuantity = getValue(cancel.getProductName()); //inv current value
			Integer prodtQuantity = cancel.getQuantity();
			
			inventory.put(cancel.getProductName(), invQuantity + prodtQuantity);
			
		}
	}
	
	public synchronized void search(String username){
		boolean found = false;
		for(Order x: orderList){
			if(x.getUserName().equals(username)){
				found = true;
				System.out.println("<" + x.getOrderID()+ "> <"+ x.getProductName()+ "> <" + x.getQuantity()+ ">");
			}
			
		}
		if(!found){
			System.out.println("No order found for <" + username + ">");
		}
		
	}
	
	public synchronized void list(){
		for (String productname: inventory.keySet()){
            Integer value = inventory.get(productname);  
            System.out.println(productname + " " + value);  
		} 
	}
	

}
