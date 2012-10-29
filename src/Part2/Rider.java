package Part2;

import java.io.BufferedReader;
import java.io.IOException;

import Part2.Elevator.Direction;

/**
 * Thread class representing a Rider.  Riders play the "minstrels" role in the lab description.
 * @author tS3m
 *
 */
public class Rider extends Thread {
	
	Object lock;
	Building myBuilding;
	BufferedReader myInput;
	int myId;
	
	public Rider(Building b, BufferedReader input, int id){
		lock = new Object(); 
		myBuilding = b;
		myInput = input;
		myId = id;
	}
	
	private Elevator getElevatorGoingUp(int riderId, int current, int destination) throws InterruptedException {
			myBuilding.CallUp(myId, riderId, current);
			return myBuilding.AwaitUp(myId, riderId, current);
	}

	private Elevator getElevatorGoingDown(int riderId, int current, int destination) throws InterruptedException {
			myBuilding.CallDown(myId, riderId, current);
			return myBuilding.AwaitDown(myId, riderId, current);
	}

	private void rideElevator(Elevator.Direction direction, int riderId, int current, int destination) throws InterruptedException {
		Elevator arrived = null;
		System.out.printf("Riding %s from %d to floor %d\n", direction.toString(), current, destination);
		while (arrived == null) {
			switch (direction) {
			case UP:
				arrived = this.getElevatorGoingUp(riderId, current, destination);
				break;
			case DOWN:
				arrived = this.getElevatorGoingDown(riderId, current, destination);
				break;
			case STATIONARY:
				break;
			}
		}
		System.out.printf("ENTERING %d %d\n", this.myId, riderId);
		arrived.Enter(myId, riderId, current);
//		myBuilding.enterBarriers.get(current-1).complete();
		System.out.printf("EREQUESTIN\n");
		arrived.RequestFloor(myId, riderId, destination, false);
		System.out.printf("EXITING\n");
		arrived.Exit(myId, riderId, destination);

	}
	
	private void goToFloor(int riderId, int current, int destination) throws InterruptedException{
		Elevator arrived = null;
		if(destination==current) {
			this.myBuilding.log("T%d: NO-OP R%d S%d D%d\n", this.myId, riderId, current, destination);
			return;
		}
		else if(destination>current){ //going up
			this.rideElevator(Direction.UP, riderId, current, destination);
		}
		else if(destination<current){ //going down
			this.rideElevator(Direction.DOWN, riderId, current, destination);
		}
	}
	
	
	public void run() {
		String line;
		try {
			while(true) {
				System.out.printf("RIDER THREAD IN %d\n", this.myId);
				synchronized(lock) {
					line = this.myInput.readLine();
					if (line == null) {
						System.out.println("RIDER THREAD OUT");
						return;
					}
				}		
				int[] info = ElevatorSimulator.parseInformation(line.split(" "));
				int riderId = info[0];
				int start = info[1];
				int destination = info[2];
				System.out.printf("Going to floor %d\n", destination);
				goToFloor(riderId, start, destination);
				System.out.printf("Went to floor on T%d\n", this.getId());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
