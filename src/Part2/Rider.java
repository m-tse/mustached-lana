package Part2;

import Part2.Elevator.Direction;

/**
 * Thread class representing a Rider.  Riders play the "minstrels" role in the lab description.
 * @author tS3m
 *
 */
public class Rider extends Thread {
	Building myBuilding;
	int myFloor;
	int myDestination;
	
	public Rider(Building b, int floor, int destination){
		myBuilding=b;
		myFloor=floor;
		myDestination=destination;
		System.out.printf("new passenger on floor %d with destination %d\n", this.myFloor, this.myDestination);
	}
	
	
	private Elevator getElevatorGoingUp(int destinationFloor) throws InterruptedException {
			myBuilding.CallUp(myFloor);
			return myBuilding.AwaitUp(myFloor);
	}

	private Elevator getElevatorGoingDown(int destinationFloor) throws InterruptedException {
			myBuilding.CallDown(myFloor);
			return myBuilding.AwaitDown(myFloor);

	}

	private void rideElevator(Elevator.Direction direction, int destinationFloor) throws InterruptedException {
		Elevator arrived = null;
		while (arrived == null) {
			switch (direction) {
			case UP:
				arrived = this.getElevatorGoingUp(destinationFloor);
			case DOWN:
				arrived = this.getElevatorGoingDown(destinationFloor);
			}
		}
		arrived.Enter();
		arrived.RequestFloor(destinationFloor);
		myBuilding.exitBarriers.get(destinationFloor).hold();
		myBuilding.exitBarriers.get(destinationFloor).complete();
		arrived.Exit();

	}
	
	private void goToFloor(int destinationFloor) throws InterruptedException{
		System.out.println("Going to destination");
		Elevator arrived = null;
		if(destinationFloor==myFloor) return;
		else if(destinationFloor>myFloor){ //going up
			this.rideElevator(Direction.UP, destinationFloor);
		}
		else if(destinationFloor<myFloor){ //going down
			this.rideElevator(Direction.DOWN, destinationFloor);
		}
	}
	
	public void run() {
		try {
			goToFloor(myDestination);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
