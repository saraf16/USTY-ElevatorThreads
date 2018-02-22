package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {


    this.scene;

    //þegar lyftan kemur niður segir hun relase * pláss  þá hleypir hun inn alltaf
	//jafn mörgum og hun á pláss fyrir
	//persóna fer inn í lyftu og ++ personInElveterCount
	//ein semehora per biðröð - vanda mál ef velja milli semphora og fólks?
	//lyftan þarf ekki að vita af neinu
	// ef lyftan tekur inn 4 og það er þegar 1 þá þarf að passa að hun læki samt sephorun niður i
	//0 svo það geti engin smegið sér i lyftu eftir að hun fer
	// ef við viljum hækka semphor köllum við á relase/signal
	//lækka semphoru með að kalla á aquire/wait
	//passa lyftan kalli ekki á aqure á semphore sem er komin niður i 0
	//og við þurfum þá að mutle exlute hana á meðan svo engin annar komist inn
	// 



	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading

    public static Semaphore semaphore1; // mætti mögulega vera private?

    public static Semaphore semaphore2;// mættu mögulega vara private?

    public static ElevatorScene scene; //geturm verið her sem stakið eða inn í
    //Person og þá er Person með private ElevatorScene scene en þa þarf smið

	public static boolean elevetorMayDie;


	//ekki láta þessa tölu niður fyrir 50 eða 30 millisecontur
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;
	private Thread elevatorTread = null;

	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you
	ArrayList<Integer> exitedCount = null;


	public static Semaphore exitedCountMutex;

	public static Semaphore elevatorWaitMUTEX; //nota inn i PERSON inn í try hja þar
	//þar sem semaphore er að waita og gera þá utan um hana mutex
	//ég mundi segja mutex fyrir hverja hæð

	public static Semaphore personCountMUTEX;

	//á lika vera elveter count




	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {

		elevetorMayDie = true;

		if(elevatorTread != null){
			if(elevatorTread.isAlive()){

				try{
					elevatorTread.join();

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}

		elevetorMayDie = false;

		scene = this;
        semaphore1 = new Semaphore(6);
        semaphore2 = new Semaphore(6);
        personCountMUTEX = new Semaphore(1);


		elevatorTread = new Thread(new Runnable()) { //þarf að breyta þessu?

            public void run () {

            	if(ElevatorScene.elevetorMayDie){
            		return;
				}

                EleveterScene.semaphore1.release(); //signal
            }

        });

		elevatorTread.start();





		/**
		 * Important to add code here to make new
		 * threads that run your elevator-runnables
		 * 
		 * Also add any other code that initializes
		 * your system for a new run
		 * 
		 * If you can, tell any currently running
		 * elevator threads to stop
		 */

		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}

		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}
		else {
			exitedCount.clear();
		}
		for(int i = 0; i < getNumberOfFloors(); i++) {
			this.exitedCount.add(0);
		}
		exitedCountMutex = new Semaphore(1);
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {

	    Thread thread = new Thread(new Person());
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */



		try {
			personCountMUTEX.acquire();
				personCount.set(sourceFloor, personCount.get(sourceFloor) + 1);
			personCountMUTEX.release();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//lika hægt að kalla með fallinu elevevatorScene.scene.incremenNumber OF peopleWAaitingAtFloor
		personCount.set(sourceFloor, personCount.get(sourceFloor) + 1);
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {


		//dumb code, replace it
		return 1;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		//lifta relesar þegar hun kemur á hæð 6 sinnum - kallar á fallið signal
		//eins oft og hun er með laus pláss
		//ef hun kemur ekki tóm þá bbara 6 minus hvað eru margir í
		//getum notað þetta fall
		//ef við gerum þetta fall rétt
		// ef liftan er ekki full þá kalla á wait?

		//dumb code, replace it!
		switch(elevator) {
		case 1: return 1;
		case 2: return 4;
		default: return 3;
		}
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}

    public void decrementNumberOfPeopleWaitingAtFloor(int floor) {

        try {

            personCountMUTEX.acquire();
            personCount.set(floor, (personCount.get(floor) - 1));
            personCountMUTEX.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

	//Person threads must call this function to
	//let the system know that they have exited.
	//Person calls it after being let off elevator
	//but before it finishes its run.
	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}


}