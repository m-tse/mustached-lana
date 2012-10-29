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
	private Elevator lastSignaled;

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

	public void CallUp(int id, int rider, int current) throws InterruptedException {
		this.log("T%d: R%d pushes U%d\n", id, rider, current);
		Elevator closest = this.findClosestElevator(Elevator.Direction.UP, current);
		closest.RequestFloor(id, rider, current, true);
	}

	public void CallDown(int id, int rider, int current) throws InterruptedException {
		this.log("T%d: R%d pushes D%d\n", id, rider, current);
		Elevator closest = this.findClosestElevator(Elevator.Direction.DOWN, current);
		closest.RequestFloor(id, rider, current, true);
	}

	public Elevator AwaitUp(int threadId, int riderId, int floor) throws InterruptedException {
		this.log("T%d: R%d waits U%d\n", threadId, riderId, floor);
		EventBarrier upBarrier = enterBarriers.get(floor-1);
//		System.out.printf("AWAITING UP T%d R%d  F%d\n", threadId, riderId, floor);
//		upBarrier.hold();
		Elevator arrived = this.lastSignaled;
		System.out.printf("Done AWAITING UP E%d T%d R%d  F%d\n", arrived.getMyId(),threadId, riderId, floor);
		return arrived;
	}
	
	public Elevator AwaitDown(int threadId, int riderId, int floor) throws InterruptedException {
		this.log("T%d: R%d waits D%d\n", threadId, riderId, floor);
		EventBarrier downBarrier = enterBarriers.get(floor-1);
//		System.out.printf("AWAITING DOWN T%d R%d  F%d\n", threadId, riderId, floor);
//		downBarrier.hold();
		Elevator arrived = this.lastSignaled;
		System.out.printf("Done AWAITING DOWN E%d T%d R%d  F%d\n", arrived.getMyId(),threadId, riderId, floor);
		return arrived;
	}

	
	public void stopElevators() {
		for (Elevator e: this.elevators) {
			e.stopElevator();
		}
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
	
	public synchronized void setLastSignaled(Elevator e) {
		this.log("Setting last signaled E%d\n", e.getMyId());
		this.lastSignaled = e;
	}
	
}
