package q1;

/*
 * lv5743
 * rvr324
 */

// uses monitors

public class MonitorCyclicBarrier {
	
	int parties;
	int count;
	
	public MonitorCyclicBarrier(int parties){
		this.parties = parties;
		this.count = 0;
		
	}
	
	
	public synchronized int await() throws InterruptedException {
		
		count++;
		int index = parties - count;
		if(count != parties){
			
		}else{
			count = 0; //time to reset
		}
		
		return index;
	}

}
