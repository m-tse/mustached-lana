package Part1;



/**
 * Used for testing the EventBarrier implementation
 * @author tS3m
 *
 */
public class TestThread extends Thread{
	MyEventBarrier myEventBarrier;
	int myID;
	
	public TestThread(MyEventBarrier b,int id){
		myEventBarrier=b;
		myID=id;
		
	}
	
	public void run(){
		try {
			myEventBarrier.hold();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			myEventBarrier.complete();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}
}
