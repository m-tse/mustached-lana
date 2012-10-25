

/**
 * Used for testing the EventBarrier implementation
 * @author tS3m
 *
 */
public class testThread extends Thread{
	EventBarrier myEventBarrier;
	int myID;
	
	public testThread(EventBarrier b,int id){
		myEventBarrier=b;
		myID=id;
		
	}
	
	public void run(){
		System.out.println("Thread "+myID+" has started");
		try {
			myEventBarrier.hold();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myEventBarrier.complete();
	}
}
