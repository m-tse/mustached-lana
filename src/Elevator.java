import java.util.LinkedList;
import java.util.Queue;


public class Elevator extends Thread{
	public static final int up = 1;
	public static final int down = 2;
	public static final int stationary = 0;
	private int myFloor=0;
	private int currentRequest=-1;
	private int direction;
	private Building myBuilding;
	private Queue<Integer> floorRequests;
	
	public Elevator(Building b){
		myBuilding = b;
		floorRequests = new LinkedList<Integer>();
		direction = stationary;
	}
	public void run() {
		while(true){
			if(currentRequest!=-1){
				if(currentRequest==myFloor){
					OpenDoors();

				}
				VisitFloor(currentRequest);
				
			}
			else if(!floorRequests.isEmpty()){
				currentRequest=floorRequests.poll();
				
			}
			
		}
		// TODO Auto-generated method stub
		
	}
	public void Enter(){
		
	}
	public void Exit(){
	
	}
	public void RequestFloor(int floor){
		floorRequests.add(floor);
	}
	private void OpenDoors(){
		try {
			myBuilding.exitBarriers.get(myFloor).signal();
			//myBuilding.entryBarriers.get(myFloor).signal();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentRequest=-1;
		CloseDoors();
	}
	private void CloseDoors(){
		
	}
	//for now we will not pick up passengers along the way in the same direction, simply go to the next requested floor
	private void VisitFloor(int floorRequest){
		myFloor=floorRequest;
	}
}
