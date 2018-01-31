/**Java Assignment 3
 * @authors Robot A7 Team - Joshua Chew, Luke Fox, Daniel Porter, Mengxin Zhu
 */
 
import ShefRobot.*;
 
public class Assignment3  {
 	 
 	 // static constants, variables and methods
 	private static final int MOVE_SPEED = 300;
	private static final int TURN_SPEED = 200;
	private static final int CATCH_SPEED = 100;	 
  	private static final int BLUE_MAX = 10;
	 
	private static int blueCount = 0;
	private static boolean reachedCentre = false;

 	private static Robot myRobot = new Robot();	 
	private static Motor leftMotor = myRobot.getLargeMotor(Motor.Port.A);
	private static Motor rightMotor = myRobot.getLargeMotor(Motor.Port.B);
	private static Motor cage = myRobot.getMediumMotor(Motor.Port.C);
	private static ColorSensor sensor = myRobot.getColorSensor(Sensor.Port.S1);
	private static TouchSensor touch = myRobot.getTouchSensor(Sensor.Port.S2);
	private static Speaker horn = myRobot.getSpeaker(); 
 	 
 	// public method declarations
	/** Checks to see if the robot has reached the blue centre by counting the
	* number of blue's detected by the sensor. Once the counter reaches the max 
	* limit for blue it will set the "reachedCentre" variable as true and
	* break out of the line following code.
	* @authors A7 Team
	*/
	public static void checkColor() {
		for (int count: blueCount){
			if (count == 1) {
				System.out.println(count+"st Blue!");
			}
			if (count == 2){
				System.out.println(count+"nd Blue!");
			}
			else if (count == 3){
				System.out.println(count+"rd Blue!");
			}
			else if (count < BLUE_MAX)
				System.out.println(count+"th Blue!");
			}
			else {
				reachedCentre = true;
			}
		}
		System.out.println(reachedCentre);
	} 
	
	/** Line following code for following the black line to the blue centre. 
	* This will run as long as the "reachedCentre" variable is false (see above
	checkColor method). The robot is following the edge of the black line. When
	* it detects black it will turn left to the white edge and when it detects
	* white it will turn right to the black edge. It will continue this zig-zag
	* motion until it reaches the blue centre. There are commented println parts
	* used for troubleshooting the sensor.
	* @authors A7 Team
	*/
 	public static void followLine() {
 		int blueChecker = 0;
		//edge following
		while (blueChecker < BLUE_MAX){
				if (sensor.getColor() == ColorSensor.Color.BLUE) {
					blueChecker ++;
        			//System.out.println("blue " + blueChecker); for troubleshooting to check if sensor picks up blue on the line
        		}
				while (sensor.getColor() == ColorSensor.Color.BLACK) {
					rightMotor.setSpeed(MOVE_SPEED);
					rightMotor.forward();
					blueChecker = 0;
				}
				//System.out.println(sensor.getColor()); for troubleshooting the colour sensor
				rightMotor.stop();
				while (sensor.getColor() == ColorSensor.Color.WHITE) {
					leftMotor.setSpeed(MOVE_SPEED);
					leftMotor.forward();
					blueChecker = 0;
				}
				//System.out.println(sensor.getColor()); for troubleshooting the colour sensor
				leftMotor.stop();
		}
		//System.out.println(sensor.getColor());for troubleshooting the colour sensor	
 	}
 	
 	
/*  	public static void dance() {
		for (int i=30; i<=140; i+=10) {
			//stop and go left  
			leftMotor.stop();
			rightMotor.stop();
			rightMotor.setSpeed(3*MOVE_SPEED);
			leftMotor.setSpeed(3*MOVE_SPEED);
			rightMotor.rotate(i);
			leftMotor.rotate(-i);
																			
			//stop and go right
			leftMotor.stop();
			rightMotor.stop();
			leftMotor.setSpeed(3*MOVE_SPEED);
			rightMotor.setSpeed(3*MOVE_SPEED);
			leftMotor.rotate(i);
			rightMotor.rotate(-i);
		}
 	} */
 	 	
 	public static void main(String[] args) {	
 		followLine();
 		myRobot.close();
 		
 		Ev3football test = new Ev3football();
 		test.run();			
	}
}
