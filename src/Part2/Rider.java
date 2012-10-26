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
			return myBuilding.AwaitUp(current);
	}

	private Elevator getElevatorGoingDown(int riderId, int current, int destination) throws InterruptedException {
			myBuilding.CallDown(myId, riderId, current);
			return myBuilding.AwaitDown(current);

	}

	private void rideElevator(Elevator.Direction direction, int riderId, int current, int destination) throws InterruptedException {
		Elevator arrived = null;
		while (arrived == null) {
			switch (direction) {
			case UP:
				arrived = this.getElevatorGoingUp(riderId, current, destination);
				break;
			case DOWN:
				arrived = this.getElevatorGoingDown(riderId, current, destination);
				break;
			case STATIONARY:
				System.out.println("ASDKJASLKDASKLJHDAKSJHDALKSJDHALKSJDHLKJASDHLASJHALSKJDHAS");
				break;
			}
		}
		arrived.Enter(myId, riderId, current);
		arrived.RequestFloor(myId, riderId, destination, false);
		myBuilding.exitBarriers.get(destination-1).hold();
		myBuilding.exitBarriers.get(destination-1).complete();
		arrived.Exit(myId, riderId, destination);

	}
	
	private void goToFloor(int riderId, int current, int destination) throws InterruptedException{
		System.out.println("Going to destination");
		Elevator arrived = null;
		if(destination==current) return;
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
				synchronized(lock) {
					line = this.myInput.readLine();
					if (line == null) {
						System.out.println("RIDER OUT!!");
						return;
					}
					System.out.println(line);
				}		
				int[] info = Simulator.parseInformation(line.split(" "));
				int riderId = info[0];
				int start = info[1];
				int destination = info[2];
				goToFloor(riderId, start, destination);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
