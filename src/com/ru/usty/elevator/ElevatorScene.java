package com.ru.usty.elevator;

import org.lwjgl.Sys;

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


    //this.scene;

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
	// segðir að við getum gert array af semphorum



	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading

    public static Semaphore inSemaphore; // mætti mögulega vera private?

    public static Semaphore outSemaphores[];// mættu mögulega vara private?

    public static ElevatorScene scene; //geturm verið her sem stakið eða inn í
    //Person og þá er Person með private ElevatorScene scene en þa þarf smið

	public static boolean elevetorMayDie;

	public static int currentFloor;

	public static int numberOfPeopleInEleveter;


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

	public static Semaphore elevatorWaitFistMUTEX; //nota inn i PERSON inn í try hja þar
	//þar sem semaphore er að waita og gera þá utan um hana mutex
	//ég mundi segja mutex fyrir hverja hæð

	public static Semaphore personCountMUTEX;

    public static Semaphore elevatornCountMUTEX;

    public static Semaphore elevatorWaitSecentMUTEX[];

    public static Semaphore elevatorReleseMUTEX[];



    //á lika vera elveter count




	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(final int numberOfFloors, int numberOfElevators) {

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
        inSemaphore = new Semaphore(0);
        personCountMUTEX = new Semaphore(1);
        elevatorWaitFistMUTEX = new Semaphore(1);
        elevatornCountMUTEX = new Semaphore(1);
        numberOfPeopleInEleveter = 0;

        outSemaphores = new Semaphore[numberOfFloors];
        elevatorWaitSecentMUTEX = new Semaphore[numberOfFloors];
        elevatorReleseMUTEX = new Semaphore[numberOfFloors];



        for (int i = 0; i < numberOfFloors; i++) {
            outSemaphores[i] = new Semaphore(0);
            elevatorWaitSecentMUTEX[i] = new Semaphore(1);

        }


		elevatorTread = new Thread(new Runnable() { //þarf að breyta þessu?

            public void run () {

             do{

                 currentFloor = 0;

                 if (ElevatorScene.elevetorMayDie) {
                     return;
                 }

                 try {
                     Thread.sleep(1500);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }


                 //System.out.println("getNumberOfPeopleInElevator(1) "+ getNumberOfPeopleInElevator(currentFloor));
                 for(int i = 0; i < 6 ; i++){
                     try {
                         elevatorWaitFistMUTEX.acquire();
                            ElevatorScene.inSemaphore.release(); //signal ++
                         elevatorWaitFistMUTEX.release();
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }


                 }

                 //up
                 while(currentFloor != (numberOfFloors - 1)){


                     try {
                         Thread.sleep(1500);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }


                     currentFloor++;


                     System.out.println("getNumberOfPeopleInElevato(" + currentFloor + ")"+ getNumberOfPeopleInElevator(currentFloor));
                     for(int i = 0; i < 6 ; i++){

                         try {
                             elevatorWaitSecentMUTEX[currentFloor].acquire();
                                ElevatorScene.outSemaphores[currentFloor].release();
                             elevatorWaitSecentMUTEX[currentFloor].release();
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         System.out.println("i = " + i);

                     }

                 }


             }while(true);

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

        Thread thread = new Thread(new Person(sourceFloor, destinationFloor));
        thread.start();
        //System.out.println("Person thread starts");

        /**
         * Important to add code here to make a
         * new thread that runs your person-runnable
         *
         * Also return the Thread object for your person
         * so that it can be reaped in the testSuite
         * (you don't have to join() yourself)
         */

	    //incrementNumberOfPeopleWaitingAtFloor(sourceFloor);
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {
		//dumb code, replace it
		return currentFloor;
	}

    public int freeSlotsInElevator() {
        return 6 - getNumberOfPeopleInElevator(0);
    }
	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {

	    return numberOfPeopleInEleveter;
	}


    public void decrementPeopleInEleveter() {

        try {
            ElevatorScene.elevatornCountMUTEX.acquire();
                ElevatorScene.numberOfPeopleInEleveter--;
            ElevatorScene.elevatornCountMUTEX.release();
            //System.out.println("PEOPLE CLASSI -- = " + numberOfPeopleInEleveter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void incrementPeopleInEleveter() {

        try {
            ElevatorScene.elevatornCountMUTEX.acquire();
                ElevatorScene.numberOfPeopleInEleveter++;
            ElevatorScene.elevatornCountMUTEX.release();
            //System.out.println("PEOPLE CLASSI ++ = " + numberOfPeopleInEleveter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}

    public void decrementNumberOfPeopleWaitingAtFloor(int floor) {

        try {
            ElevatorScene.personCountMUTEX.acquire();
                personCount.set(floor, (personCount.get(floor) - 1));
                ElevatorScene.personCountMUTEX.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void incrementNumberOfPeopleWaitingAtFloor(int floor) {

        try {
            personCountMUTEX.acquire();
                personCount.set(floor, (personCount.get(floor) + 1));
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
