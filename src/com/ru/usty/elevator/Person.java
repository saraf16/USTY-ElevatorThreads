package com.ru.usty.elevator;
import java.util.concurrent.Semaphore;

public class Person implements Runnable {

	/*int src, dst;
	public Person(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}*/
	
	@Override
	public void run() {
		ElevatorScene.elevatorWaitMUTEX = new Semaphore(1);
		try {
			ElevatorScene.elevatorWaitMUTEX.acquire();
			ElevatorScene.semaphore1.acquire(); // elevatorWaitMutex
			ElevatorScene.elevatorWaitMUTEX.release();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}