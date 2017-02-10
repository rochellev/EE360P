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
	
	// conditions that rule the threads
	final ReentrantLock dLock = new ReentrantLock();
	final ReentrantLock sLock = new ReentrantLock();
	final ReentrantLock fLock = new ReentrantLock();
	final Condition noDigging = dLock.newCondition();  // digger wait
	final Condition noEmptyHoles = sLock.newCondition();   // seeder wait
	final Condition noSeededHoles = fLock.newCondition();  // filler wait
	int digCount, seedCount, fillCount;  //should make private?

	public Garden() {
		this.digCount = 0;
		this.seedCount = 0;
		this.fillCount = 0;
	};

	
	public synchronized void startDigging() throws InterruptedException{
		dLock.lock();
		try{
			while((digCount - seedCount >= 4) || (digCount - fillCount >= 8)){
				noDigging.await();
			}
			digCount++;  // is here the right spot to increment? idk
			noDigging.signal();
		}finally{
			dLock.unlock();
		}

	};

	public synchronized void doneDigging() {
	};

	public synchronized void startSeeding() {
	};

	public synchronized void doneSeeding() {
	};

	public synchronized void startFilling() {
	};

	public synchronized void doneFilling() {
	};

	/*
	 * The following methods return the total number of holes dug, seeded or
	 * filled by Newton, Benjamin or Mary at the time the methods' are invoked
	 * on the garden class.
	 */
	public synchronized int totalHolesDugByNewton() {
		return digCount;
	};

	public synchronized int totalHolesSeededByBenjamin() {
		return seedCount;
	};

	public synchronized int totalHolesFilledByMary() {
		return fillCount;
	};
}
