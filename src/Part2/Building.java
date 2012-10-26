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
	public ArrayList<EventBarrier> exitBarriers = new ArrayList<EventBarrier>();
	
	private Object lock;
	private ArrayList<Integer> visitedFloors = new ArrayList<Integer>();
	private FileWriter logFile;
	private PrintWriter logger; 

	public Building(int numFloors, int numElevators, int capacity) throws IOException {
		lock = new Object();
		floors = numFloors;
		for (int i = 0; i < floors; i++) {
			exitBarriers.add(new MyEventBarrier());
		}
		for (int i = 0; i < numElevators; i++)
			elevators.add(new Elevator(this, i, capacity));
		for (Elevator e : elevators)
			e.start();
		this.logFile = new FileWriter("Elevator.log"); // true = append to existing file
		this.logger = new PrintWriter(logFile);

	}
	
	public synchronized void log(String format, Object... args) {
		this.logger.printf(format, args);
	}
	
	public void closeLog() throws IOException {
		this.logger.close();
		this.logFile.close();
	}

	// for now, simply send a request to the elevator
	public void CallUp(int id, int rider, int current) {
		System.out.printf("Passenger calls up on floor %d\n", current);
		this.log("T%d: R%d pushes U%d\n", id, rider, current);
		Elevator closest = this.findClosestElevator(Elevator.Direction.UP, current);
		closest.RequestFloor(id, rider, current, true);
	}

	public void CallDown(int id, int rider, int current) {
		System.out.printf("Passenger calls down on floor %d\n", current);
		this.log("T%d: R%d pushes D%d\n", id, rider, current);
		Elevator closest = this.findClosestElevator(Elevator.Direction.DOWN, current);
		closest.RequestFloor(id, rider, current, true);
	}

	public Elevator AwaitUp(int floor) throws InterruptedException {
		EventBarrier upBarrier = exitBarriers.get(floor-1);
		upBarrier.hold();
		upBarrier.complete();
		Elevator arrived = this.getElevatorAtFloor(floor);
		return arrived;
	}
	
	public Elevator AwaitDown(int floor) throws InterruptedException {
		EventBarrier downBarrier = exitBarriers.get(floor-1);
		downBarrier.hold();
		downBarrier.complete();
		Elevator arrived = this.getElevatorAtFloor(floor);
		return arrived;
	}

	public Elevator getElevatorAtFloor(int floor) {
		// Get first elevator found
		for (Elevator e: this.elevators) {
			System.out.printf("Current %d Floor %d Capacity %s HasRequest %s\n",
					e.getCurrentFloor(), floor, e.atCapacity(), e.hasRequest(floor));
			if (e.getCurrentFloor() == floor && !e.atCapacity()) {
				System.out.println("GETTING ELEVATOR");
				return e;
			} else if (e.hasRequest(floor)) {
				e.removeRequest(floor);
			}
		}
		return null;
	}

	
	public void stopElevators() {
		for (Elevator e: this.elevators) {
			e.stop();
		}
		System.out.println("All elevators stopped.");
	}
	
	
	// For testing purposes
	public void visitFloor(int floor) {
		this.visitedFloors.add(floor);
	}

	public ArrayList<Integer> getVisitedFloors() {
		return this.visitedFloors;
	}
	
	public void printVisitedFloors() {
		for (int i = 0; i < this.elevators.size(); ++i) {
			System.out.printf("Elevator %d: %s\n", i, this.elevators.get(i).getVisitedFloors());
		}
	}
	

	/*
	 *	Finds the closest elevator, closest floor same direction 
	 */
	public Elevator findClosestElevator(Elevator.Direction direction, int floor) {
		Elevator closest = null;
		int difference = Integer.MAX_VALUE;
		int dir = 0;
		for (Elevator e: this.elevators) {
			dir = e.getCurrentFloor()-floor;
			if (dir < 0 && e.getDirection() == Elevator.Direction.UP) {
				int newDifference = dir;
				if (newDifference < difference) {
					difference = Math.abs(newDifference);
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
		if (closest == null) { // Not most efficient but whatever
			closest = this.getLowestRequestsElevator();
		}
		return closest;
	}

	public Elevator findClosestElevator(Elevator.Direction direction, int floor, Elevator excluded) {
		Elevator closest = null;
		int difference = Integer.MAX_VALUE;
		int dir = 0;
		for (Elevator e: this.elevators) {
			dir = e.getCurrentFloor()-floor;
			if (!e.equals(excluded)) {
				if (dir < 0 && e.getDirection() == Elevator.Direction.UP) {
					int newDifference = dir;
					if (newDifference < difference) {
						difference = Math.abs(newDifference);
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
		}
		if (closest == null) { // Not most efficient but whatever
			closest = this.getLowestRequestsElevator();
		}
		return closest;
	}

	
	public Elevator getLowestRequestsElevator() {
		int min = Integer.MAX_VALUE;
		Elevator best, e;
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
	
	public void updateRequests(int floor) {
		for (Elevator e: this.elevators) {
			e.removeRequest(floor);
		}
	}

}
