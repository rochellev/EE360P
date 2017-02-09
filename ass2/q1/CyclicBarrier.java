package q1;

/*
 * lv5743
 * rvr324
 */
import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class CyclicBarrier {

	Semaphore semaphore; // used for holding permits
	//static Semaphore mutex = new Semaphore(1); // needed?
	int parties;
	int count;

	/*
	 * Creates a new CyclicBarrier that will trip when the given number of
	 * parties (threads) are waiting upon it
	 */
	public CyclicBarrier(int parties) {
		this.parties = parties;
		this.semaphore = new Semaphore(parties); // semaphore with given threads
		this.count = 0; // counts the number of threads waiting
	}

	/*
	 * Waits until all parties have invoked await on this barrier. If the
	 * current thread is not the last to arrive then it is disabled for thread
	 * scheduling purposes and lies dormant until the last thread arrives.
	 * Returns: the arrival index of the current thread, where index (parties -
	 * 1) indicates the first to arrive and zero indicates the last to arrive.
	 */
	public synchronized int await() throws InterruptedException {  //ok to be synchronized??
		count++; // increment, await was called, so another thread
		int index = parties - count;
		if (count == parties) {
			semaphore.release(parties);  // want to release all the threads
			count = 0; //reset
		} else {
			semaphore.acquire();  //should there be a wait??
			
		}
		return index;
	}
}
