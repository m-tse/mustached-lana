package Part2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Part1.EventBarrier;
import Part1.MyEventBarrier;

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
	private boolean stopped;
	
	private int myId;
	private int riders;
	private int currentFloor;
	private int currentRequest;
	private int capacity;
	public ArrayList<EventBarrier> ridingBarriers;
	
	/**
	 * Floor requests are implemented in a queue
	 * @throws IOException 
	 */
	
	public Elevator(Building b, int id, int capacity) throws IOException{
		this.lock1 = new Object();
		this.myBuilding = b;
		this.floorRequests = new LinkedList<Integer>();
		this.direction = Direction.STATIONARY;
		this.myId = id;
		this.riders = 0;
		this.currentFloor = -1;
		this.currentRequest = -1;
		this.capacity = capacity;
		this.ridingBarriers = new ArrayList<EventBarrier>();
		for (int n = 0; n < this.myBuilding.getNumberFloors(); ++n) {
			this.ridingBarriers.add(new MyEventBarrier());
		}
		this.stopped = false;
	}
	

	/*
	 *	Utility methods 
	 */
	
	public int getMyId() {
		return this.myId;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public int getNumberRequests() {
		return this.floorRequests.size();
	}
	
	public synchronized void removeRequest(int floor) {
		if (this.floorRequests == null) {
			// For some reason it sometimes goes null?
			this.floorRequests = new LinkedList<Integer>();
			return;
		}
		while (this.floorRequests.contains(floor)) {
			this.floorRequests.remove(floor);
		}
	}
	
	public boolean hasRequest(int floor) {
		return this.floorRequests.contains(floor);
	}

	public boolean atCapacity() {
		if (this.capacity == -1) {
			return false;
		}
		if (this.riders == this.capacity) {
			return true;
		}
		return false;
	}
	
	private synchronized void getNextRequest() {
		if(!floorRequests.isEmpty() && floorRequests != null){
			currentRequest=floorRequests.poll();
			int difference = currentRequest - this.currentFloor;
			if (difference < 0) {
				this.direction = Direction.DOWN;
			} else {
				this.direction = Direction.UP;
			}
		} else {
			this.moveTowardsCenter();
		}
	}

	private void moveTowardsCenter() {
		int centerFloor = this.myBuilding.getNumberFloors()/2;
		this.direction = Direction.STATIONARY;
		if (this.currentFloor > centerFloor) {
			this.currentFloor--;
		} else if (this.currentFloor < centerFloor) {
			this.currentFloor++;
		} 
	}
	
	public void stopElevator() {
		this.interrupt();
	}

	
	
	/*
	 * 	Elevator methods described in hand out
	 */
	
	public void Enter(int threadId, int riderId, int current) throws InterruptedException {
		this.myBuilding.log("T%d: R%d enters E%d on F%d\n", threadId, riderId, this.myId, current);
		synchronized(lock1) {
			riders++;
		}
	}
	
	public void Exit(int threadId, int riderId, int floor) throws InterruptedException {
		this.myBuilding.log("T%d: R%d exits E%d on F%d\n", threadId, riderId, this.myId, floor);
		synchronized (lock1) {
			riders--;	
		}
	}
	
	public void RequestFloor(int threadId, int riderId, int floor, boolean isBuilding) throws InterruptedException{
		if (!isBuilding) this.myBuilding.log("T%d: R%d pushes E%dB%d\n", threadId, riderId, this.myId, floor);
		synchronized(lock1) {
			this.floorRequests.add(floor);
		}
	}
	
	private void OpenDoors() throws InterruptedException { // Needs to synchronize to set lastSignaled Elevator in Building
		this.myBuilding.log("E%d on F%d opens\n", this.myId, this.currentFloor);
		myBuilding.enterBarriers.get(currentFloor-1).signal();
		this.ridingBarriers.get(currentFloor-1).signal();
		this.removeRequest(this.currentFloor);
		CloseDoors();
	}

	private void CloseDoors() {
		this.myBuilding.log("E%d on F%d closes\n", this.myId, this.currentFloor);
		return;
	}
	
	private void VisitFloor(int floorRequest) throws InterruptedException{
		if (currentFloor == floorRequest) {
			OpenDoors();
			this.currentRequest = -1;
			return;
		}
		while(currentFloor!=floorRequest){
			Random rand = new Random();
			// To simulate actual elevator travel time
			int elevatorTime = rand.nextInt(100);
			sleep(elevatorTime);
			if(this.direction == Direction.UP) {
				currentFloor++;
				this.myBuilding.log("E%d moves up to F%d\n", this.myId, this.currentFloor);
			}
			else if(this.direction == Direction.DOWN) {
				currentFloor--;
				this.myBuilding.log("E%d moves down to F%d\n", this.myId, this.currentFloor);
			}
			
		
		}
		if (currentFloor == floorRequest) {
			OpenDoors();
		} else if (!this.floorRequests.isEmpty()) {
			if (this.floorRequests.contains(currentFloor)) {
				OpenDoors();
			}
		}
		this.currentRequest = -1;
		return;
	}

	
	// Thread "main"
	public void run() {
		while(!this.isInterrupted()){
			if(currentRequest!=-1){
					try {
						VisitFloor(currentRequest);
					} catch (InterruptedException e) {
						return;
					}
			}
			else {
				getNextRequest();
			}

		}
	}
	
	
	

	

}
