import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Thread class representing an elevator.  Elevators play a "gatekeeper" role, in the lab description.
 * @author tS3m
 *
 */
public class Elevator extends Thread{
	public static final int up = 1;
	public static final int down = 2;
	public static final int stationary = 0;
	
	
	private Object lock1 = new Object();
	private int riders=0;
	private int myFloor=0;
	private int currentRequest=-1;
	private int direction;
	private Building myBuilding;
	/**
	 * Floor requests are implemented in a queue
	 */
	private Queue<Integer> floorRequests;
	
	public Elevator(Building b){
		myBuilding = b;
		floorRequests = new LinkedList<Integer>();
		direction = stationary;
	}
	public void run() {
		//System.out.println("Elevator Thread begins");
		while(true){
			if(currentRequest!=-1){
				if(currentRequest==myFloor){
					OpenDoors();

				}
				try {
					VisitFloor(currentRequest);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else if(!floorRequests.isEmpty()){
				currentRequest=floorRequests.poll();
				
			}
			
		}

		
	}
	public void Enter(){
		synchronized(lock1){
			riders++;
		}
	}
	public void Exit(){
		synchronized(lock1){
			riders--;	
		}
	}
	public void RequestFloor(int floor){
		floorRequests.add(floor);
	}
	private void OpenDoors(){
		try {
			myBuilding.exitBarriers.get(myFloor).signal();
			//myBuilding.entryBarriers.get(myFloor).signal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentRequest=-1;
		CloseDoors();
	}
	//doesn't do anything for now.
	private void CloseDoors(){
		return;
	}
	//for now we will not pick up passengers along the way in the same direction, simply go to the next requested floor
	private void VisitFloor(int floorRequest) throws InterruptedException{
		
		while(myFloor!=floorRequest){
			Random rand = new Random();
			int elevatorTime = rand.nextInt(5000);
			this.sleep(elevatorTime);
			if(myFloor<floorRequest) myFloor++;
			else if(myFloor>floorRequest) myFloor--;
			System.out.printf("Elevator is now on floor %d\n", myFloor);
		}
		return;
	}
}
