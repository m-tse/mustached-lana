package Part2;

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
	
	public enum Direction {
		UP, DOWN, STATIONARY 
	}
	
	private Object lock1;
	private Building myBuilding;
	private Queue<Integer> floorRequests;
	private Direction direction;
	
	private int myId;
	private int riders;
	private int currentFloor;
	private int currentRequest;
	private int capacity;
	
	private ArrayList<Integer> visitedFloors = new ArrayList<Integer>();
	/**
	 * Floor requests are implemented in a queue
	 */
	
	public Elevator(Building b, int id, int capacity){
		this.lock1 = new Object();
		this.myBuilding = b;
		this.floorRequests = new LinkedList<Integer>();
		this.direction = Direction.STATIONARY;
		this.myId = id;
		this.riders = 0;
		this.currentFloor = -1;
		this.currentRequest = -1;
		this.capacity = capacity;
	}
	

	/*
	 *	Utility methods 
	 */
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public int getNumberRequests() {
		return this.floorRequests.size();
	}
	
	public ArrayList<Integer> getVisitedFloors() {
		return this.visitedFloors;
	}
	
	public void removeRequest(int floor) {
		System.out.printf("Elevator %s: REMOVING floor %d\n", this.getName(), floor);
		while (this.floorRequests.contains(floor)) {
			this.floorRequests.remove(floor);
		}
	}
	
	public boolean hasRequest(int floor) {
		return this.floorRequests.contains(floor);
	}


	
	

	public boolean atCapacity() {
		if (this.riders == this.capacity) {
			return true;
		}
		return false;
	}
	
	
	private void getNextRequest() {
	//	System.out.printf("IN THIS Visiting floor %d\n", currentRequest);
		if(!floorRequests.isEmpty()){
			currentRequest=floorRequests.poll();
			System.out.printf("VREQUESTED %s floor %d\n", this.getName(), currentRequest);
			int difference = currentRequest - this.currentFloor;
			if (difference < 0) {
				this.direction = Direction.DOWN;
			} else {
				this.direction = Direction.UP;
			}
		} else {
			this.direction = Direction.STATIONARY;
		}
	}

	
	
	/*
	 * 	Elevator methods described in hand out
	 */
	
	public void Enter(int threadId, int riderId, int floor){
		System.out.printf("ENTERING Elevator %s\n", this.getName());
		this.myBuilding.log("T%d: R%d enters E%d on F%d\n", threadId, riderId, this.myId, floor);
		synchronized(lock1){
			riders++;
		}
	}
	
	public void Exit(int threadId, int riderId, int floor){
		System.out.printf("EXITING Elevator %s\n", this.getName());
		this.myBuilding.log("T%d: R%d exits E%d on F%d\n", threadId, riderId, this.myId, floor);
		synchronized(lock1){
			riders--;	
		}
	}
	
	public void RequestFloor(int threadId, int riderId, int floor, boolean isBuilding){
		System.out.printf("Elevator %s: Got request %d | Current: %d Dir: %s Riders: %d\n",
				this.getName(),floor, this.currentFloor, this.direction.toString(), this.riders);
		if (!isBuilding) this.myBuilding.log("T%d: R%d pushes E%dB%d\n", threadId, riderId, this.myId, floor);
		this.floorRequests.add(floor);
	}
	
	private void OpenDoors(){
		try {
			// Tell barriers that elevator has arrived at floor
			System.out.printf("OPENING Elevator %s at Floor %d Riders %d\n", this.getName(), this.currentFloor, this.riders);
			this.myBuilding.log("E%d on F%d opens\n", this.myId, this.currentFloor);
			myBuilding.exitBarriers.get(currentFloor-1).signal();
			//myBuilding.entryUpBarriers.get(this.currentFloor).signal();
			this.removeRequest(this.currentFloor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		CloseDoors();
	}
	
	//doesn't do anything for now.
	private void CloseDoors(){
		this.myBuilding.log("E%d on F%d closes\n", this.myId, this.currentFloor);
		return;
	}
	
	//for now we will not pick up passengers along the way in the same direction, simply go to the next requested floor
	private void VisitFloor(int floorRequest) throws InterruptedException{
		if (currentFloor == floorRequest) {
			OpenDoors();
			this.currentRequest = -1;
			return;
		}
		while(currentFloor!=floorRequest){
			Random rand = new Random();
			int elevatorTime = rand.nextInt(1000);
			this.sleep(elevatorTime);
			if(this.direction == Direction.UP) {
				currentFloor++;
				this.myBuilding.log("E%d moves up to F%d\n", this.myId, this.currentFloor);
			}
			else if(this.direction == Direction.DOWN) {
				currentFloor--;
				this.myBuilding.log("E%d moves down to F%d\n", this.myId, this.currentFloor);
			}
			System.out.printf("Elevator %s is now on floor %d\n", this.getName(), currentFloor);
			if (currentFloor == floorRequest) {
				myBuilding.visitFloor(currentFloor);
				this.visitedFloors.add(currentFloor);
				OpenDoors();
			} else if (!this.floorRequests.isEmpty()) {
				if (this.floorRequests.contains(currentFloor)) {
					this.visitedFloors.add(currentFloor);
					OpenDoors();
				}
			}
			this.currentRequest = -1;
		}
		return;
	}
	


	public void run() {
		while(true){
			if(currentRequest!=-1){
				if(currentRequest==currentFloor){
					System.out.printf("Current request: %d | Current floor: %d\n", currentRequest, currentFloor);
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



}
