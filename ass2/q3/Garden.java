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
	/*
	// not sure how many locks 
	final ReentrantLock dLock = new ReentrantLock();  
	final ReentrantLock sLock = new ReentrantLock();
	final ReentrantLock fLock = new ReentrantLock();
	final Condition notDigging = dLock.newCondition();  // digger wait
	final Condition emptyHole = dLock.newCondition();
	final Condition seededHole = sLock.newCondition();   
	final Condition filledHole = fLock.newCondition();  // filler wait
	int digCount, seedCount, fillCount;  //should make private?
	
	*/
	final ReentrantLock Lock = new ReentrantLock();  
	final Condition notDigging = Lock.newCondition();  // digger wait
	final Condition emptyHole = Lock.newCondition();
	final Condition seededHole = Lock.newCondition();   
	final Condition filledHole = Lock.newCondition();  // filler wait
	int digCount, seedCount, fillCount;  //should make private?

	public Garden() {
		this.digCount = 0;
		this.seedCount = 0;
		this.fillCount = 0;
	};

	public void startDigging() throws InterruptedException {
		Lock.lock();
		try{
		while(digCount - seedCount >= 4){  // wait for digger conditions
				seededHole.await();
		}
		while(digCount - fillCount >=8){
				filledHole.await();
		}
		digCount++;            // once satisfied, can dig new hole
		emptyHole.signalAll(); // notify others
		}finally{
			Lock.unlock();
		}
		
	};

	//not sure what supposed to do
	public void doneDigging() throws InterruptedException{
		Lock.lock();
		try{
			
			
		}finally{
			Lock.unlock();
		}
		
	};

	public void startSeeding() throws InterruptedException{
		Lock.lock();
		try{
			while(digCount <= seedCount){ // no empty holes to put seeds in
				emptyHole.await();       // waiting for at least one empty hole from digger
			}
			seedCount++;
			seededHole.signalAll();
		}finally{
		Lock.unlock();	
		}
		
	};

	public void doneSeeding() {
		
		
	};

	public void startFilling() throws InterruptedException {
		Lock.lock();
		try{
			while( seedCount <= fillCount ){ // no seeded holes to fill
				seededHole.await();
			}
			seedCount++;
			filledHole.signalAll();
		}finally{
			Lock.unlock();
		}
	};

	public void doneFilling() {
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
