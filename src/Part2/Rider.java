package Part2;

import java.io.BufferedReader;
import java.io.IOException;


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
	
	private void goToFloor(int riderId, int current, int destination) throws InterruptedException{
		Elevator arrived = null;
		if(destination==current) {
			this.myBuilding.log("T%d: NO-OP R%d S%d D%d\n", this.myId, riderId, current, destination);
			return;
		}
		else if(destination>current){ //going up
			arrived = this.myBuilding.AwaitUp(myId, riderId, current);
		}
		else if(destination<current){ //going down
			arrived = this.myBuilding.AwaitDown(myId, riderId, current);
		}
		arrived.Enter(myId, riderId, current);
		arrived.ridingBarriers.get(current-1).complete();
		arrived.RequestFloor(myId, riderId, destination, false);
		arrived.ridingBarriers.get(destination-1).hold();
		arrived.Exit(myId, riderId, destination);
		arrived.ridingBarriers.get(destination-1).complete();
	}
	
	
	public void run() {
		String line;
		try {
			while(true) {
				synchronized(lock) {
					line = this.myInput.readLine();
					if (line == null) {
						return;
					}
				}		
				int[] info = ElevatorSimulator.parseInformation(line.split(" "));
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
