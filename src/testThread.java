


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myEventBarrier.complete();
		
	}
}
