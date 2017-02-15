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
	final Condition emptyHole = Lock.newCondition();  // notify when a new hole dug, seeder awaits
	final Condition seededHole = Lock.newCondition(); // notify when a hole seeded, filler and digger awaits   
	final Condition filledHole = Lock.newCondition();  // notify when a filled hole, digger awaits
	int digCount, seedCount, fillCount;  //should make private?

	public Garden() {
		this.digCount = 0;
		this.seedCount = 0;
		this.fillCount = 0;
	};

	public void startDigging(){
		Lock.lock();  // waits until free	
		while(digCount - seedCount >= 4){  // wait for digger conditions
			try {
				seededHole.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	while(digCount - fillCount >=8){
			try {
				filledHole.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
		
	};

	public void doneDigging(){
		// assume a dig was made
		digCount++;
		emptyHole.signalAll();  // indicate new hole available
		Lock.unlock(); // release resource
	};

	// start seeding not need lock to work, just the conditions to be there
	public void startSeeding(){
		while(digCount <= seedCount){ // no empty holes to put seeds in
			try {
				emptyHole.await();  // waiting for at least one empty hole from digger
			} catch (InterruptedException e) {
				e.printStackTrace();
			}      
		}
		
	};

	public void doneSeeding() {
		// assume some seed function called
		seedCount++;
		seededHole.signalAll();
		
	};

	public void startFilling() {
		Lock.lock();
		while( seedCount <= fillCount ){ // no seeded holes to fill
			try {
				seededHole.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	};

	public void doneFilling() {
		fillCount++;
		filledHole.signalAll();
		Lock.unlock();
	};

	/*
	 * The following methods return the total number of holes dug, seeded or
	 * filled by Newton, Benjamin or Mary at the time the methods' are invoked
	 * on the garden class.
	 */
	
	//TODO: do these need locks?
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
