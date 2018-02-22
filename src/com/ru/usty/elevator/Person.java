package com.ru.usty.elevator;

public class Person implements Runnable {

	/*int src, dst;
	public Person(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}*/
	
	@Override
	public void run() {
		
		try {
			ElevatorScene.semaphore1.acquire(); //wait
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}