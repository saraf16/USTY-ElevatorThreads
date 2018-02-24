package com.ru.usty.elevator;
import java.util.concurrent.Semaphore;

public class Person implements Runnable {

	int sourceFloor, destinationFloor;
	private ElevatorScene scene;
	
	public Person(int sourceFloor, int destinationFloor) {
		this.sourceFloor = sourceFloor;
		this.destinationFloor = destinationFloor;
	}

	
	@Override
	public void run() {
		// 1. increment waiting on floor
		// 2. wait for elevator
		// 3. increment people elevator 
        // 4. decrement waiting on floor
		// 5. wait to get out
		// 6. decrement people in elevator

		//ElevatorScene.elevatorWaitMUTEX = new Semaphore(1);
		try {
			ElevatorScene.elevatorWaitMUTEX.acquire(); 
				ElevatorScene.semaphore1.acquire(); // wait
			ElevatorScene.elevatorWaitMUTEX.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Person is in elevator(through barrier)
		ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(sourceFloor);
		System.out.println("Person thread released");
	}

}