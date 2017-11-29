package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	public static final int DRIVE_STICK_PORT = 1;
	public static final int RIGHT_MOTOR = 1;
	public static final int LEFT_MOTOR = 0;
	public static final int LEFT_ENCODER_A = 1;
	public static final int LEFT_ENCODER_B = 0;
	
	// The value that determines how many "pulses", the unit the encoder
	// returns, constitute some other unit (in this case, feet)
	public static final double ENCODER_DISTANCE_PER_PULSE = 12.0*(1.0/150.0);
	
	Joystick driveStick;
	RobotDrive drive;
	
	Talon rightMotor, leftMotor;
	Encoder encoder;
	
	PIDController pid;
	
	@Override
	public void robotInit() {
		rightMotor = new Talon(RIGHT_MOTOR);
		leftMotor = new Talon(LEFT_MOTOR);	
		driveStick = new Joystick(DRIVE_STICK_PORT);
		
		encoder = new Encoder(LEFT_ENCODER_A, LEFT_ENCODER_B);
		encoder.setDistancePerPulse(ENCODER_DISTANCE_PER_PULSE);
		
		drive = new RobotDrive(rightMotor, leftMotor);
		
		pid = new PIDController(0.05, 0.0, 0.5, encoder, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
				System.out.println("Output: " + output);
				drive.arcadeDrive(-output, 0.141);
			}
		});
		
		pid.setOutputRange(-0.6, 0.6);
	}
	
	private void pidEnable() {
		// Zero the encoder...
		encoder.reset();
		
		// Start the PID loop
		pid.setSetpoint(120.0);
		pid.enable();
	}

	@Override
	public void autonomousInit() {
		pidEnable();
	}

	@Override
	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Encoder Distance", encoder.getDistance());
		System.out.println("Encoder Distance: " + encoder.getDistance());
		System.out.println("Encoder Raw: " + encoder.getRaw());
	}
	
	@Override
	public void teleopInit() {
		pid.disable();
	}

	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("Encoder Distance", encoder.getDistance());
		double move = driveStick.getY();
		double rotate = -driveStick.getTwist();
		drive.arcadeDrive(0.7 * move, 0.7 * rotate);
	}

	@Override
	public void disabledInit() {
		pid.disable();
	}
}

