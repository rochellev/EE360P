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
		if(count == parties){     // or is it count == parties -1 idk
			count = 0; //reset
			this.notifyAll();  //want to release threads
		}else{
			this.wait(); //need to wait for more threads
		}
		
		return index;
	}

}
