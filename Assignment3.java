/**Java Assignment 3
 * @authors Joshua Chew, Luke Fox, Daniel Porter, Mengxin Zhu
 */

import ShefRobot.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Assignment3 extends JFrame implements Runnable, KeyListener, WindowListener, ActionListener {
	
	//defining the behavior of the program
	enum Command {STOP, LEFT, RIGHT, FORWARD, REVERSE, CATCH, RELEASE};
	private static final int DELAY_MS = 50;
	
	//make the window, text label and menu
	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 200;
	
	private JLabel label = new JLabel("Stop",JLabel.CENTER);
			
	public Assignment3() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Quit");
		JMenuItem menuItem = new JMenuItem("Really Quit?");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		this.add(label, BorderLayout.CENTER);
		label.setFont(new Font("SansSerif", Font.PLAIN, 48));
		this.setBounds(0,0,FRAME_WIDTH,FRAME_HEIGHT);
		this.setTitle("Robot A7 Controller");
		this.addKeyListener(this);
		this.addWindowListener(this);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//start the program	
	private Command command = Command.STOP;	
	private Robot myRobot = new Robot();	 
	public static void main(String[] args) {
		Thread t = new Thread(new Assi());
		t.start();
	}

	//select the command corresponding to the key pressed	
	public void keyPressed(KeyEvent e) {
		switch ( e.getKeyCode()) {
			case java.awt.event.KeyEvent.VK_UP:
				command = Command.FORWARD;
				break;
			case java.awt.event.KeyEvent.VK_DOWN:
				command = Command.REVERSE;
				break;
			case java.awt.event.KeyEvent.VK_LEFT:
				command = Command.LEFT;
				break;
			case java.awt.event.KeyEvent.VK_RIGHT:
				command = Command.RIGHT;
				break;
			case java.awt.event.KeyEvent.VK_SPACE:
				command = Command.CATCH;//engage the nick cage
				break;
			case java.awt.event.KeyEvent.VK_R:
				command = Command.RELEASE;//release the cage
				break;
			default:
				command = Command.STOP;
				break;
		}
	}
	//and released
	public void keyReleased(KeyEvent e) {
		command = Command.STOP;
	}
	//ignore everything else
	public void keyTyped(KeyEvent e) {}	
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}	
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	
	//handle the quit menu item	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Really Quit?")) {
			System.out.println("Closing Bluetooth");
			myRobot.close();
			System.exit(0);
		}
	}
	//and the window closing
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing Bluetooth");
		myRobot.close();
	}

	public void run() {
		//declare the connected motors, sensors, and speaker
		Motor leftMotor = myRobot.getLargeMotor(Motor.Port.A);
		Motor rightMotor = myRobot.getLargeMotor(Motor.Port.B);
		Motor cage = myRobot.getMediumMotor(Motor.Port.C);
		ColorSensor sensor = myRobot.getColorSensor(Sensor.Port.S1);
		TouchSensor touch = myRobot.getTouchSensor(Sensor.Port.S2);
		Speaker horn = myRobot.getSpeaker();
		
        //delay for setting up
        myRobot.sleep(2000);
	
	//getting the robot to follow the black line
        do {
        	if (sensor.getColor() == ColorSensor.Color.BLACK) {
        		//Go Forwards
        		leftMotor.setSpeed(100);
        		rightMotor.setSpeed(100);
        		leftMotor.forward();
        		rightMotor.forward();
        		
        		System.out.println("black");
        	}
        	else if (sensor.getColor() != ColorSensor.Color.BLACK) {
				
        		//stop and go right
        		leftMotor.stop();
				rightMotor.stop();
				leftMotor.setSpeed(100);
				leftMotor.forward();
				rightMotor.setSpeed(0);

				myRobot.sleep(500);
				
				if (sensor.getColor() == ColorSensor.Color.BLACK) {
					do {
						leftMotor.stop();
						rightMotor.stop();
						leftMotor.setSpeed(100);
						rightMotor.setSpeed(100);
						leftMotor.forward();
						rightMotor.forward();
					
						System.out.println("black");
					} while (sensor.getColor() == ColorSensor.Color.BLACK);
				}
				
				else if (sensor.getColor() != ColorSensor.Color.BLACK) {
					leftMotor.stop();
					rightMotor.stop();
					rightMotor.setSpeed(100);
					rightMotor.forward();
					leftMotor.setSpeed(0);
					
					myRobot.sleep(1000);
					
					System.out.println("white");
				}			
        	}	
	} while (sensor.getColor() !=ColorSensor.Color.BLUE); 
	
		
		while (true) {
			switch (command) {
				case STOP:
					label.setText("Stop");
					leftMotor.stop();
					rightMotor.stop();
					cage.stop();
					//put your code for stopping here
					
					break;					
				case FORWARD:
					label.setText("Forward");
					leftMotor.setSpeed(250);
					rightMotor.setSpeed(250); 
					leftMotor.forward();
					rightMotor.forward();
					//put your code for going forwards here
					
					break;					
				case REVERSE:
					label.setText("Reverse");
					leftMotor.setSpeed(250);
					rightMotor.setSpeed(250);
					leftMotor.backward();                                            
					rightMotor.backward();
					//put your code for going backwards here
					
					break;					
				case LEFT:
					label.setText("Left");
					rightMotor.setSpeed(200);
					rightMotor.forward();
					leftMotor.setSpeed(0);

					break;
				case RIGHT:
					label.setText("Right");
					leftMotor.setSpeed(200);
					leftMotor.forward();
					rightMotor.setSpeed(0);

					break;
				case CATCH:
					label.setText("Catch");
					cage.setSpeed(200); 
					while(touch.isTouched()==false){
						cage.forward();
					}
					horn.playTone(1500,200);
					cage.stop();
					label.setText("Caught");
 					break;
 					
				case RELEASE:
					label.setText("Release");
					cage.setSpeed(200); 
					cage.backward();
 					break;
			}
			try {
				Thread.sleep(DELAY_MS);
			} catch (InterruptedException e) {};
		}	
	}
}
