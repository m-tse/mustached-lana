################
# Team Info
################

Name1: Andrew Shim  
NetId1: aks35

Name2: Matthew Tse
NetId2: mst13

Name3: Afanasiy Yermakov
NetId3: asy9

###############
# Time spent
###############

40 hours [combined]

################
# Files to submit
################

*.java  #Including the test case files; Please do not submit tar or zip files
README	#This file filled with the lab implementation details
Elevator.log #The name of the log file you generate should be Elevator.log.
	 # You can submit the log file along with your source code. However, 
	 # it should be generated automatically when the Elevator is executed.

####################################
# Implementation details
####################################
______________________________________________________________________
PROMPT

This section should contain the implementation details and a overview of the
results. 

You are required to provide a good README document along with the
implementation details. In particular, you can pseudocode to describe your
implementation details where necessary. However that does not mean to
copy/paste your Java code. Rather, provide clear and concise text/pseudocode
describing the primary algorithms (for e.g., scheduling choices you used) and
any special data structures in your implementation. We expect the design and
implementation details to be at most 2-3 pages. A plain text file is encouraged.
However, a pdf is acceptable. No other forms are permitted.

In case of lab is limited in some functionality, you should provide
the details to maximize your partial credit.
______________________________________________________________________

Part 1. Event Barrier

Our implementation of EventBarrier uses the following instance variables:
    - boolean signaled
    - int counter

We will discuss the implementation of the EventBarrier methods using these
variables.

void hold()
    The hold() method makes use of the signaled boolean and counter.
    Anytime a thread (i.e. minstrel) calls the hold() method, the 
    counter is incremented, and this operation is made atomic by using
    the "synchronized" key word; the counter allows the EventBarrier to know how many
    "waiters" it has. While the signaled boolean is false (i.e. the EventBarrier
    has not been signaled), the thread is forced to wait through the 
    wait() method. Once the EventBarrier becomes signaled and the thread
    is notified, the thread will be allowed to move on.

void signal()
    The signal() method makes use of the signaled boolean and the counter.
    Once a "gatekeeper" thread calls signal, the EventBarrier's signaled
    boolean gets set to true and the EventBarrier calls notifyAll(), thereby
    waking up all threads that were waiting on the EventBarrier. Then the 
    thread is forced to wait() while the number of waiters is greater then 0. 
    This simulates the notion of the "gatekeeper" waiting for all of the 
    "minstrels" to cross the bridge. Once the last thread is finished 
    "crossing the bridge", the "gatekeeper" thread breaks out of the wait 
    because it is notified by the waiters as they call complete() (discussed below).
    The EventBarrier is then set to unsignaled.

void complete()
    The complete() method uses the counter. When a thread calls
    complete(), which means the thread has finished "crossing the bridge",
    the EventBarrier decrements its counter (done atomically through "synchronized")
    because there is one less waiter waiting. Then, the calling thread 
    notifies all of the waiters. This serves two functions: one, it wakes
    up the signaler, and two it wakes up any other threads that were waiting
    on the calling thread. Basically, when a thread completes, if the number of
    waiters is greater than 0 that thread will wait() until there are no more 
    waiters. Thus, it is up to every completing thread to notifyAll() because
    there may be threads waiting on the calling thread's completion. This 
    simulates the "minstrels" waiting for all of the other "minstrels" to 
    finish crossing the bridge before moving on.

int waiters()
    This method simply returns the current count of the waiters.

Our EventBarrier also comes with logging and debug toggle functionality 
but this functionality was more for our benefit and was outside the 
scope of the lab requirements.

Testing EventBarrier
    To test our EventBarrier we created TestThread.java, TestGateKeeper.java,
    and EventSimulator.java. EventSimulator takes a text file as input
    where each line of the file contains a single integer. That integer
    represents how many TestThreads are to be run. Upon initialization,
    the EventSimulator creates a MyEventBarrier and TestGateKeeper.
    Reading the file one line at a time, the EventSimulator creates the requested
    number of TestThreads, where all of the created threads and the TestGateKeeper
    share the same MyEventBarrier. It then launches the TestThreads, 
    sleeps for a moment, and then has the TestGateKeeper signal the 
    EventBarrier. Note, the TestThreads simply call hold() and 
    complete() on the MyEventBarrier.

    The sample output for running the EventSimulator on "Part1_test.txt" was
    logged to "Event.log".

______________________________________________________________________

Part 2. Elevator

Overview of Elevator System
    We assume that there is exactly one building that can contain multiple
    elevators and support multiple riders. We have the following three classes:
        - Building
        - Elevator extends Thread
        - Rider extends Thread

    The Building instance variables:
        - int floors
        - ArrayList<Elevator> elevators
        - ArrayList<EventBarrier> entryBarriers
    
    Elevator instance variables:
        - Building myBuilding
        - Queue<Integer> floorRequests
        - Direction direction /* this is an enum defined in Elevator
                                can be UP, DOWN, or STATIONARY */
        - int myId
        - int riders
        - int currentFloor
        - int currentRequest
        - int capacity

    Rider instance variables:
        - Building myBuilding
        - int myId

    Note: Just the pertinent instance variables are listed. By pertinent we
        mean those that have to do with functionality listed in the handout.

Description of Functionality
    The main method must start by initializing the Building.
    While the Building initializes, it creates and launches its Elevator threads,
    and creates an EventBarrier for each of its floors.
    Then, the main creates as many Rider threads as is specified
    by an input. Each of those Rider threads can make requests to the Building 
    to travel to a different floor. Based on where a Rider is, the Building will
    send an Elevator to that Rider by requesting that the Elevator move to the
    current floor of the Rider. Elevators handle floor requests by adding the 
    requests to their queues. The Rider must then wait for however long it takes
    the assigned Elevator to reach the Rider's current floor. Once the Elevator 
    arrives at the Rider's floor, it opens its doors. If there are other Riders
    currently in the Elevator, those Riders will exit while the Riders that have
    been waiting on that floor for an Elevator enter. Entry will only be permitted
    if the Elevator has not reached max capacity, otherwise the Rider will have
    to wait for another Elevator. Once a Rider enters the Elevator, the Elevator
    will proceed in the Direction it was traveling, unless the only floor 
    request in the Elevator's queue is that of the current Rider, then the Elevator
    will take the Rider to the requested destination. As a Rider travels to 
    the destination, the Elevator will pick up and drop off
    other Riders along the way. The Elevator will not, however, override the 
    "current" Rider's request (meaning that it will not go in the opposite direction
    of where the "current" Rider wants to go), so it will always, although not 
    completely fairly, service the Rider's request. In summary, the Elevator scheduling
    operates according to a SCAN methodology.

Implementation Specifics
    We created an ElevatorSimulator.java that reads an input text file (as specified 
    in post 191 on Piazza) and runs the Elevators and Riders accordingly. The Riders
    know which floors they are currently on and which floors they want to go to, thus
    they also know in which direction they need to travel. They
    pass this knowledge onto the building through their goToFloor() method, which eventually
    calls the Building's CallUp/CallDown and AwaitUp/AwaitDown methods. The 
    Building's AwaitUp/Down methods are essentially wrappers that include logging
    functionality with the CallUp/Down methods.

    CallUp/Down methods find the closest Elevator (algorithm for that discussed
    below) and then calls the RequestFor() method on that Elevator. The RequestFor() method
    adds the requested floor to the Elevator's queue of requests. 
    
    Once the Elevator has been called, the rider will wait for the Elevator to get to its 
    floor by calling hold() on the Elevator's ridingBarrier (i.e. MyEventBarrier) for that floor.
    We guarantee that the Elevators will service the Riders because the Elevators operate
    in a loop where they call their VisitFloor() methods for the currentRequest; then
    once they get to the floor of the current request, they take another request off of 
    their queues and proceed until there are no more requests. 
    
    Once an Elevator reaches the Rider's floor, the Elevator will call its OpenDoors() method
    and signal() the EventBarrier of that floor. This will cause the requesting Riders
    of that floor to wake up, after which they will call the Enter() method of the 
    Elevator. The Elevator will increment its count of riders for every call to Enter(),
    and once its number of riders equals its capacity, the Elevator will stop allowing 
    Rides to enter. 
    
    In addition to signaling the riders to board, the OpenDoors() method removes 
    the current floor from the Elevator's queue of requests; this is because 
    everyone who was in the Elevator that wanted to go to the current floor will 
    have exited while those who want to get on the Elevator either have boarded 
    or cannot due to capacity constraints. At the end of OpenDoors(),
    the Elevator calls CloseDoors() and resumes its travels. 

    Once an Elevator is finished visiting a floor (all Riders that wanted 
    to have entered (or capacity has been reached) and exited, it will set its current floor
    request equal to -1. In an Elevator's main while loop, the loop conditional
    checks whether the current floor request is equal to -1 or not. If it is,
    the Elevator will fetch the next request from its queue unless the queue 
    is empty, in which case the current floor request will remain -1. 
    In the first situation, the Elevator has fetched the next request 
    and will make its way to the requested floor, otherwise the Elevator will
    make its way to the center floor while maintaining that it is STATIONARY.
    We make it move to the center floor because that minimizes the expected 
    amount of work the Elevator will need to carry out for the next request
    (how many floors the Elevator will have to move), and we say that it is
    STATIONARY because we only want to assign the Elevator's Direction when
    it has received a request, otherwise we would indicate that 
    the Elevator is busier than it actually is (i.e. our algorithm to find
    the "closest" Elevator would be affected).

    Now that Riders have entered the Elevator, they can RequestFloor() and
    travel to their destination. After making the request, the Riders will
    call hold() on the Elevator's ridingBarrier of the destination floor.
    When the Elevator reaches the destination, it will OpenDoors() as 
    before and the Riders will be allowing to exit while new ones
    can enter. Note that Riders call complete() on the Elevator's 
    ridingBarriers at the end of every Enter() and Exit().

Algorithm for Finding the Closest Elevator
    The method findClosestElevator() is in Building. 

    We base our definition of "closeness" on the proximity of the Elevator
    to the requested floor and the Elevator's direction. We do not give 
    specific scores for these characteristics, rather we simply define
    that going in the proper direction grants a significantly higher score
    than being in close proximity (i.e. an Elevator A on floor 1 that is going
    UP is closer to floor 4 than is an Elevator B on floor 5 that is going UP).
    If two Elevators are going in the proper directions, then proximity is
    taken into account (i.e. if Elevator B was on floor 5 and going DOWN then
    it is closer to floor 4 than Elevator A is).

    The algorithm prioritizes direction and proximity simply by checking
    that the Elevator is going in the proper direction first. If the Elevator
    is going the right way, the algorithm calculates the difference of 
    that Elevator's current floor with the desired floor and maintains 
    that difference. If the difference is the smallest the algorithm has
    seen at that point, the algorithm will set that difference as the 
    smallest difference seen and set the Elevator as the return value.
    The algorithm loops through all of the buildings Elevators, and by the
    end of the loop, if there is a "closest" Elevator, that Elevator will
    be returned.

    However, if none of the Elevators are going in the proper direction, 
    the algorithm will not have found an Elevator to return. In this case,
    the algorithm finds the Elevator with the lowest number of requests,
    (the logic being that more requests means that an Elevator is more 
    likely to reach capacity and will thus be more likely to not
    service the Rider by the time the Elevator gets to the Rider).

    We realize that there are a number of metrics we are not considering 
    and that are important to a real-world model such as the time it
    takes for an Elevator to service a request and how many Riders
    the Elevator can satisfy in a given amount of time. In order 
    to model these, we could have timers on each of the Riders
    and associate costs with not being fair or taking too much time.
    It would then be the system's job to attempt to minimize these
    costs.


Testing the Elevator System
    As mentioned earlier, we came up with the ElevatorSimulator.java as a means 
    to test our implementation. The simulator takes a text file as input. The 
    text file MUST have the following information or the program will break:
        F E R T C
        r s d
        .
        .
        .

    The first line must contain 5 integers:
        F = Number of floors in the Building
        E = Number of Elevators in the Building
        R = Number of Riders
        T = Number of Rider threads
        C = Capacity of the Elevators

    After the first line there can be any number of following lines that 
    must contain three integers:
        r = The Rider number 
        s = The starting floor
        d = The destination floor
    
    Upon completion of the simulation, the program will have produced 
    "Elevator.log", which captures all of the pertinent thread actions.
    Two sample input text files have been provided:
        "Part2_Sub1_test.txt"
        "Part2_Sub2_test.txt"
        "Part2_Sub3_test.txt"
    
    A sample "Elevator.log" has also been provided; the log is a result of
    running the simulator on "Part2_Sub2.txt".

    In order to make sure that our Elevators and Riders are behaving as 
    expected, we also created SimulationGrader.java. The grader takes 
    the output file ("Elevator.log") as input and determines whether 
    or not the sequence of actions is in a proper order (e.g. the Rider has
    to have called on an Elevator before it can start waiting on the Elevator).
    If it finds out of order actions, it increments a score counter. When it 
    has finished reading through the input file, if the score is anything
    greater than 0 then the grader determines that the simulation has 
    failed.

    Note: The grader still has a few bugs in it, but it is a decent
    is a good test for correct functionality.

####################################
# Feedback on the lab
####################################

How did you find the lab?

    The lab was good in that it allowed us to explore multi-threaded programming
    but the timeline for the release of guidelines lead to a bit of struggle, although
    the extension did help to mitigate that. Basically,
    when the lab was first assigned, we only had the handout to go off of, and the
    handout mentioned nothing of the I/O testing guidelines (mentioned later on Piazza).
    Because of this, we attacked this lab in a particular way and ended up having 
    to change a good amount of the code in order to follow the I/O guidelines. 
    For one thing, in order to log the particular information (e.g. "E1 on F3 closes") 
    certain methods had to get passed certain parameters. While this wasn't a very
    significant mental challenge, it was rather tedious. Also, we found that we 
    spent a good amount of time on creating the test cases, whereas we feel that 
    it would have been better had we spent all our time dealing with threads.


##################################
# Additional comments
##################################

Anything else you would like to convey.


