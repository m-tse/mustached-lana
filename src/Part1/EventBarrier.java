package Part1;
import java.util.ArrayList;


/**
 * EventBarrier interface.
 * @author tS3m
 *
 */
public interface EventBarrier {
	public void hold() throws InterruptedException;
	public void signal() throws InterruptedException;
	public void complete() throws InterruptedException;
	int waiters();
}