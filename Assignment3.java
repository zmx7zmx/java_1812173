/**Java Assignment 3
 * @authors Joshua Chew, Luke Fox, Daniel Porter, Mengxin Zhu
 */
 
import ShefRobot.*;
 
 public class Assignment3  {
 
 	// static constants, variables and methods
 	private static final int MOVE_SPEED = 300;
	private static final int TURN_SPEED = 200;
	private static final int CATCH_SPEED = 100;
	private static final int BLUE_LIMIT = 10;
 	private static Robot myRobot = new Robot();	 
	private static Motor leftMotor = myRobot.getLargeMotor(Motor.Port.A);
	private static Motor rightMotor = myRobot.getLargeMotor(Motor.Port.B);
	private static Motor cage = myRobot.getMediumMotor(Motor.Port.C);
	private static ColorSensor sensor = myRobot.getColorSensor(Sensor.Port.S1);
	private static TouchSensor touch = myRobot.getTouchSensor(Sensor.Port.S2);
	private static Speaker horn = myRobot.getSpeaker(); 
	
	// instance variables
	//private EV3football ev3football;
 	 
 	// public method declarations
 	public static void followLine() {
 		int blueChecker = 0;
		//edge following
		while (blueChecker < BLUE_LIMIT){
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
 	 	
 	public static void main(String[] args) {	
 		followLine();
 		myRobot.close();
 		
 		EV3football test = new EV3football();
 		test.run();
 		
 				
 		
 			
	}
 	 
 	 
 	 
 }