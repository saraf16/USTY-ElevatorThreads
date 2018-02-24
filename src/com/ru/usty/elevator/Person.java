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

		ElevatorScene.scene.incrementNumberOfPeopleWaitingAtFloor(sourceFloor);

		try {
			ElevatorScene.elevatorWaitFistMUTEX.acquire();
				ElevatorScene.inSemaphore.acquire();
			ElevatorScene.elevatorWaitFistMUTEX.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ElevatorScene.scene.incrementPeopleInEleveter();

		ElevatorScene.scene.decrementNumberOfPeopleWaitingAtFloor(sourceFloor);

		try {
			ElevatorScene.elevatorWaitSecentMUTEX.acquire();
				ElevatorScene.outSemaphore.acquire();
			ElevatorScene.elevatorWaitSecentMUTEX.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ElevatorScene.scene.decrementPeopleInEleveter();
		ElevatorScene.scene.personExitsAtFloor(destinationFloor);
		ElevatorScene.scene.getExitedCountAtFloor(destinationFloor);





		// Person is in elevator(through barrier)
		System.out.println("Person thread released");
	}

}