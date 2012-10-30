package Part2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Part1.EventBarrier;
import Part1.MyEventBarrier;

/**
 * Building controller class.
 * 
 * @author tS3m
 * 
 */

public class Building {
	
	private int floors;
	
	ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	public ArrayList<EventBarrier> enterBarriers = new ArrayList<EventBarrier>();
	
	private FileWriter logFile;
	private PrintWriter logger; 

	public Building(int numFloors, int numElevators, int capacity) throws IOException {
		floors = numFloors;
		for (int i = 0; i < floors; i++) {
			enterBarriers.add(new MyEventBarrier());
		}
		for (int i = 0; i < numElevators; i++)
			elevators.add(new Elevator(this, i, capacity));
		for (Elevator e : elevators)
			e.start();
		this.logFile = new FileWriter("Elevator.log"); 
		this.logger = new PrintWriter(logFile);

	}
	
	public synchronized void log(String format, Object... args) {
		this.logger.printf(format, args);
	}
	
	public void closeLog() throws IOException {
		this.logger.close();
		this.logFile.close();
	}

	public synchronized Elevator CallUp(int id, int rider, int current) throws InterruptedException {
		this.log("T%d: R%d pushes U%d\n", id, rider, current);
		Elevator closest = this.findClosestElevator(Elevator.Direction.UP, current);
		closest.ridingBarriers.get(current-1).incrementIntended();
		closest.RequestFloor(id, rider, current, true);
		return closest;
	}

	public synchronized Elevator CallDown(int id, int rider, int current) throws InterruptedException {
		this.log("T%d: R%d pushes D%d\n", id, rider, current);
		Elevator closest = this.findClosestElevator(Elevator.Direction.DOWN, current);
		closest.ridingBarriers.get(current-1).incrementIntended();
		closest.RequestFloor(id, rider, current, true);
		return closest;
	}

	public Elevator AwaitUp(int threadId, int riderId, int floor) throws InterruptedException {
		Elevator arrived = this.CallUp(threadId, riderId, floor);
		this.log("T%d: R%d waits U%d\n", threadId, riderId, floor);
		arrived.ridingBarriers.get(floor-1).hold();
		return arrived;
	}
	
	public Elevator AwaitDown(int threadId, int riderId, int floor) throws InterruptedException {
		Elevator arrived = this.CallDown(threadId, riderId, floor);
		this.log("T%d: R%d waits D%d\n", threadId, riderId, floor);
		arrived.ridingBarriers.get(floor-1).hold();
		return arrived;
	}

	/*
	 *	Finds the closest elevator, closest floor same direction 
	 *	If there are no elevators with correct direction, poll the one with lowest requests
	 */
	public Elevator findClosestElevator(Elevator.Direction direction, int floor) {
		Elevator closest = null;
		int difference = Integer.MAX_VALUE;
		int dir = 0;
		for (Elevator e: this.elevators) {
			dir = e.getCurrentFloor()-floor;
			if (dir < 0 && e.getDirection() == Elevator.Direction.UP) {
				int newDifference = Math.abs(dir);
				if (newDifference < difference) {
					difference = newDifference;
					closest = e;
				}
			} else if (dir > 0 && e.getDirection() == Elevator.Direction.DOWN) {
				int newDifference = Math.abs(dir);
				if (newDifference < difference) {
					difference = newDifference;
					closest = e;
				}
			}
		}
		if (closest == null) {
			closest = this.getLowestRequestsElevator();
		}
		return closest;
	}
	
	public Elevator getLowestRequestsElevator() {
		int min = Integer.MAX_VALUE;
		Elevator e;
		int index = 0;
		for (int i = 0; i < this.elevators.size(); ++i) {
			e = this.elevators.get(i);
			if (e.getNumberRequests() < min) {
				min = e.getNumberRequests(); 
				index = i; 
			}
		}
		return this.elevators.get(index);
	}
	
	public int getNumberFloors() {
		return this.floors;
	}
	
	public void stopElevators() {
		for (Elevator e: this.elevators) {
			e.stopElevator();
		}
	}
}
