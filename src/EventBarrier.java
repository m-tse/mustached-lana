import java.util.ArrayList;



public interface EventBarrier {

	public void hold() throws InterruptedException;
	public void signal() throws InterruptedException;
	public void complete();
	int waiters();
	

}
