import java.util.ArrayList;


public class Building {
	private int floors;
	ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	
	public ArrayList<EventBarrier> exitBarriers = new ArrayList<EventBarrier>();
	public ArrayList<EventBarrier> entryUpBarriers = new ArrayList<EventBarrier>();
	public ArrayList<EventBarrier> entryDownBarriers = new ArrayList<EventBarrier>();
	Building(int numFloors, int numElevators){
		numFloors=floors;
		for(int i = 0;i<numElevators;i++)	elevators.add(new Elevator(this));
		for(Elevator e:elevators) e.start();	
		for(int i = 0;i<floors;i++){
			entryUpBarriers.add(new myEventBarrier());
			entryDownBarriers.add(new myEventBarrier());
			exitBarriers.add(new myEventBarrier());
		}
	}
	
	
	public void CallUp(int floor){
		
	}
	public void CallDown(int floor){
		
	}
	public Elevator AwaitUp(int floor){
		
		return null;
		
	}
	public Elevator AwaitDown(int floor){
		return null;
		
	}
}
