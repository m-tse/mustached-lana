import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Building controller class.
 * 
 * @author tS3m
 * 
 */

public class Building {
	private int floors;
	/**
	 * Array list of elevators, currently we only have 1 elevator
	 */
	ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	
	/**
	 * Every floor has 3 event barriers in this implementation, one for exit,
	 * one for entry going up, one for entry going down.
	 */
	public ArrayList<EventBarrier> exitBarriers = new ArrayList<EventBarrier>();
	public ArrayList<EventBarrier> entryUpBarriers = new ArrayList<EventBarrier>();
	public ArrayList<EventBarrier> entryDownBarriers = new ArrayList<EventBarrier>();
	private ArrayList<Integer> visitedFloors = new ArrayList<Integer>();

	Building(int numFloors, int numElevators) {
		floors = numFloors;
		for (int i = 0; i < floors; i++) {
			entryUpBarriers.add(new myEventBarrier());
			entryDownBarriers.add(new myEventBarrier());
			exitBarriers.add(new myEventBarrier());
		}
		for (int i = 0; i < numElevators; i++)
			elevators.add(new Elevator(this));
		for (Elevator e : elevators)
			e.start();

	}

	// for now, simply send a request to the elevator
	public void CallUp(int floor) {
		System.out.printf("Passenger calls up on floor %d\n", floor);
		Elevator closest = this.findClosestElevator(Elevator.Direction.UP, floor);
		closest.RequestFloor(floor);
	}

	public void CallDown(int floor) {
		System.out.printf("Passenger calls down on floor %d\n", floor);
		Elevator closest = this.findClosestElevator(Elevator.Direction.DOWN, floor);
		closest.RequestFloor(floor);
	}

	public Elevator AwaitUp(int floor) throws InterruptedException {
		EventBarrier upBarrier = exitBarriers.get(floor);
		upBarrier.hold();
		upBarrier.complete();
		Elevator arrived = this.getElevatorAtFloor(floor);
		return arrived;
	}


	
	public Elevator AwaitDown(int floor) throws InterruptedException {
		EventBarrier downBarrier = exitBarriers.get(floor);
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
