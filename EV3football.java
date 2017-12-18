/**
* Sheffield University Robot Football Controller
* Guy Brown September 2008, amended Siobhan North 2014
* Rewritten for EV3 robots SDN 2016
* 
* SCROLL DOWN TO THE BOTTOM OF THE PROGRAM
* YOU DO NOT NEED TO UNDERSTAND (OR EDIT) ANYTHING APART FROM THE
* MARKED SECTIONS OF THE PROGRAM
*/

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

public class EV3football extends JFrame implements Runnable, KeyListener, WindowListener, ActionListener {
	
	//Defining the behaviour of the prgram
	enum Command {STOP, LEFT, RIGHT, FORWARD, REVERSE, CATCHER, RCATCHER };
	private static final int DELAY_MS = 50;
	
	// Make the window, text label and menu
	private static final int FRAME_WIDTH = 400;
	private static final int FRAME_HEIGHT = 200;
	
	private JLabel label = new JLabel("Stop",JLabel.CENTER);
			
	public EV3football() {
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
		this.setTitle("Sheffield Robot Football Controller");
		this.addKeyListener(this);
		this.addWindowListener(this);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	// Start the program	
	private Command command = Command.STOP;	
	private Robot myRobot = new Robot();	 
	public static void main(String[] args) {
		Thread t = new Thread(new EV3football());
		t.start();
	}

	// Select the command corresponding to the key pressed	
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
				command = Command.CATCHER;
				break;
			case java.awt.event.KeyEvent.VK_R:
				command = Command.RCATCHER; // Reversing the Catcher
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
	
	// handle the quit menu item	
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

	/*
	 * THIS IS THE ONLY PART OF THE PROGRAM THAT YOU NEED TO EDIT
	 */

	public void run() {
		//This defines and names the two large Motors that turn the wheels
		Motor leftMotor = myRobot.getLargeMotor(Motor.Port.A);
		Motor rightMotor = myRobot.getLargeMotor(Motor.Port.B);
		ColorSensor sensor = myRobot.getColorSensor(Sensor.Port.S1);
		Motor foot = myRobot.getLargeMotor(Motor.Port.C);
		Speaker horn = myRobot.getSpeaker();
		
		
       //put your code to define other things here
        // Delay for setting up
        myRobot.sleep(2000);

       //Getting the robot to follow the black line
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
				
        		// Stop and Go Right
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
/*        
        while (sensor.getColor() != ColorSensor.Color.BLACK) {
        	    //Go Forwards
        		leftMotor.setSpeed(200);
        		rightMotor.setSpeed(200);
        		leftMotor.forward();
        		rightMotor.forward();
 		
        		System.out.println("Test");
        } */
       
		while (true) {
			switch (command) {
				case STOP:
					label.setText("Stop");
					leftMotor.stop();
					rightMotor.stop();
					foot.stop();
					// put your code for stopping here
					
					break;					
				case FORWARD:
					label.setText("Forward");
					//Going forward
					leftMotor.setSpeed(250);
					rightMotor.setSpeed(250); 
					leftMotor.forward();
					rightMotor.forward();
					//horn.playTone(1000, 200);
					// put your code for going forwards here
					
					break;					
				case REVERSE:
					label.setText("Reverse");
					//Reverse
					leftMotor.setSpeed(250);
					rightMotor.setSpeed(250);
					leftMotor.backward();                                            
					rightMotor.backward();
					// put your code for going backwards here
					
					break;					
				case LEFT:
					label.setText("Left");
					rightMotor.setSpeed(200);
					rightMotor.forward();
					leftMotor.setSpeed(0);

					break;
				case RIGHT:
					label.setText("Right");
					//Going right      
					leftMotor.setSpeed(200);
					leftMotor.forward();
					rightMotor.setSpeed(0);

					break;
				case CATCHER:
					label.setText("CATCHER");
					foot.setSpeed(200); 
					foot.forward();
 					break;
 					
				case RCATCHER:
					label.setText("R_CATCHER");
					foot.setSpeed(200); 
					foot.backward();
 					break;
 					

 					

			}
			try {
				Thread.sleep(DELAY_MS);
			} catch (InterruptedException e) {};
		}	
	}

}
