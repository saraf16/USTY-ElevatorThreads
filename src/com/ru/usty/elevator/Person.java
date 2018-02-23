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