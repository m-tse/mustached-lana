import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * Thread class representing an elevator.  Elevators play a "gatekeeper" role, in the lab description.
 * @author tS3m
 *
 */
public class Elevator extends Thread {
	public static final int up = 1;
	public static final int down = 2;
	public static final int stationary = 0;
	
	public enum Direction {
		UP, DOWN, STATIONARY 
	}
	
	private PriorityQueue<Integer> floorRequests;
	private Queue<Integer> actualRequests;
	private Direction direction = Direction.UP;
	
	private Object lock1 = new Object();
	private int riders=0;
	private int myFloor=0;
	private int currentRequest=-1;
//	private int direction;
	private Building myBuilding;
	private int capacity = 2;
	
	private ArrayList<Integer> visitedFloors = new ArrayList<Integer>();
	/**
	 * Floor requests are implemented in a queue
	 */
	
	public Elevator(Building b){
		actualRequests = new LinkedList<Integer>();
		floorRequests = new PriorityQueue<Integer>();
		myBuilding = b;
//		direction = stationary;
		direction = Direction.STATIONARY;
	}
	
	public void run() {
		//System.out.println("Elevator Thread begins");
		while(true){
	//		System.out.printf("Visiting floor %d\n", currentRequest);
			if(currentRequest!=-1){
				if(currentRequest==myFloor){
					System.out.printf("Current request: %d | Current floor: %d\n", currentRequest, myFloor);
					OpenDoors();
				}
				try {
					System.out.printf("Visiting floor %d\n", currentRequest);
					VisitFloor(currentRequest);
					System.out.printf("DONE: Visiting floor %d\n", currentRequest);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				getNextRequest();
			}
			
		}
	}


	public boolean atCapacity() {
		if (this.riders == this.capacity) {
			return true;
		}
		return false;
	}
	
	public void Enter(){
		System.out.printf("ENTERING Elevator %s\n", this.getName());
		synchronized(lock1){
			riders++;
		}
	}
	
	public void Exit(){
		System.out.printf("EXITING Elevator %s\n", this.getName());
		synchronized(lock1){
			riders--;	
		}
	}
	
	public void RequestFloor(int floor){
		System.out.printf("Elevator %s: Got request %d | Current: %d Dir: %s Riders: %d\n",
				this.getName(),floor, this.myFloor, this.direction.toString(), this.riders);
		floorRequests.add(floor);
		this.actualRequests.add(floor);
	}

	private void getNextRequest() {
	//	System.out.printf("IN THIS Visiting floor %d\n", currentRequest);
		if(!actualRequests.isEmpty()){
			currentRequest=actualRequests.poll();
			System.out.printf("VREQUESTED %s floor %d\n", this.getName(), currentRequest);
			int difference = currentRequest - this.myFloor;
			if (difference < 0) {
				this.direction = Direction.DOWN;
			} else {
				this.direction = Direction.UP;
			}
		} else {
			this.direction = Direction.STATIONARY;
		}
	}
	
	private void OpenDoors(){
		try {
			// Tell barriers that elevator has arrived at floor
		//	myBuilding.entryDownBarriers.get(myFloor).signal();
		//	myBuilding.entryUpBarriers.get(myFloor).signal();
			System.out.printf("OPENING Elevator %s at Floor %d Riders %d\n", this.getName(), this.myFloor, this.riders);
			myBuilding.exitBarriers.get(myFloor).signal();
			myBuilding.entryUpBarriers.get(this.myFloor).signal();
			this.removeRequest(this.myFloor);
//			myBuilding.updateRequests(myFloor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		CloseDoors();
	}
	
	//doesn't do anything for now.
	private void CloseDoors(){
		return;
	}
	
	//for now we will not pick up passengers along the way in the same direction, simply go to the next requested floor
	private void VisitFloor(int floorRequest) throws InterruptedException{
		if (myFloor == floorRequest) {
			OpenDoors();
			this.currentRequest = -1;
			return;
		}
		while(myFloor!=floorRequest){
			Random rand = new Random();
			int elevatorTime = rand.nextInt(1000);
			this.sleep(elevatorTime);
			if(this.direction == Direction.UP) myFloor++;
			else if(this.direction == Direction.DOWN) myFloor--;
			System.out.printf("Elevator %s is now on floor %d\n", this.getName(), myFloor);
			if (myFloor == floorRequest) {
				myBuilding.visitFloor(myFloor);
				this.visitedFloors.add(myFloor);
				OpenDoors();
			} else if (!this.actualRequests.isEmpty()) {
				if (this.actualRequests.contains(myFloor)) {
					this.visitedFloors.add(myFloor);
					OpenDoors();
				}
			}
			this.currentRequest = -1;
		}
		
//		else {
//			this.direction = Direction.UP;
//		}
		return;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public int getCurrentFloor() {
		return this.myFloor;
	}
	
	public int getNumberRequests() {
		return this.actualRequests.size();
	}
	
	public ArrayList<Integer> getVisitedFloors() {
		return this.visitedFloors;
	}
	
	public void removeRequest(int floor) {
		System.out.printf("Elevator %s: REMOVING floor %d\n", this.getName(), floor);
		while (this.floorRequests.contains(floor)) {
			this.floorRequests.remove(floor);
		}
		while (this.actualRequests.contains(floor)) {
			this.actualRequests.remove(floor);
		}
	}
	
	public boolean hasRequest(int floor) {
		return this.actualRequests.contains(floor);
	}
}
