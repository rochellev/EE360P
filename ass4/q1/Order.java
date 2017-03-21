package q1;
//copied from our assignment 3

public class Order {

	private String userName, productName;
	private Integer quantity, orderID;

	public Order(String userName, String productName, Integer quantity, Integer orderID){
		this.userName = userName;
		this.productName = productName;
		this.quantity = quantity;
		this.orderID = orderID;
	}
	
	public Order(){
		this.userName = null;
		this.productName = null;
		this.quantity = null;
		this.orderID = null;
	}
	
	public String getUserName(){
		return userName;
		
	}
	
	public String getProductName(){
		return productName;
		
	}
	
	public Integer getQuantity(){
		return quantity;
		
	}
	
	public Integer getOrderID(){
		return orderID;
	}
	}