package q3;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 * lv5743
 * rvr324
 */

/*
 * Newton = digger
 * Benjamin = seeder
 * Mary = filler
 * */

public class Garden {
	
	final ReentrantLock Lock = new ReentrantLock();   // the shovel is the resource
	final ReentrantLock seedLock = new ReentrantLock();
	final Condition digReady = Lock.newCondition();
	final Condition seedReady = seedLock.newCondition();
	final Condition fillReady = Lock.newCondition();
	
	int digCount, seedCount, fillCount;

	public Garden() {
		this.digCount = 0;
		this.seedCount = 0;
		this.fillCount = 0;
	};

	
	// the seeding should be locked until have dug one, then signal  
	public void startDigging(){
		Lock.lock();  // waits until free	
		while((digCount - seedCount >= 4) || (digCount - fillCount >=8) ){  // wait for digger conditions
			try {
				digReady.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
		
	};

	public void doneDigging(){
		// assume a dig was made
		digCount++;
		Lock.unlock(); // release resource
		try{		
		if(digCount > seedCount){
			seedLock.lock(); // no more seeds need to be sew
			seedReady.signal();
		}
		}finally{ seedLock.unlock();}

		
	};

	
	public void startSeeding(){
		seedLock.lock();
		while(digCount <= seedCount){ // no empty holes to put seeds in
			try {
				seedReady.await();  // waiting for at least one empty hole from digger
			} catch (InterruptedException e) {
				e.printStackTrace();
			}      
		}
		
	};

	public void doneSeeding() {
		// assume some seed function called
		seedCount++;
		seedLock.unlock();
		Lock.lock();
		
		try{
			if(digCount - seedCount < 4){
				digReady.signal();
			}
			if(seedCount > fillCount){
				fillReady.signal();
			}
		}finally{
			Lock.unlock();
		}
	
		
	};

	public void startFilling() {
		Lock.lock();
		while( seedCount <= fillCount ){ // no seeded holes to fill
			try {
				fillReady.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	};

	public void doneFilling() {
		fillCount++;
		digReady.signal();
		Lock.unlock();
	
	};

	/*
	 * The following methods return the total number of holes dug, seeded or
	 * filled by Newton, Benjamin or Mary at the time the methods' are invoked
	 * on the garden class.
	 */
	
	
	public int totalHolesDugByNewton() {
		return digCount;
	};

	public int totalHolesSeededByBenjamin() {
		return seedCount;
	};

	public int totalHolesFilledByMary() {
		return fillCount;
	};
}
