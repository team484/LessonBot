package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static org.usfirst.frc.team484.robot.RobotMap.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	Joystick driveStick;
	Drive drive;
	
	@Override
	public void robotInit() {
		driveStick = new Joystick(DRIVE_STICK_PORT);
		
		Talon rightMotor = new Talon(RIGHT_MOTOR);
		Talon leftMotor = new Talon(LEFT_MOTOR);	
		
		AnalogGyro topGyro = new AnalogGyro(TOP_GYRO);
		AnalogGyro bottomGyro = new AnalogGyro(BOTTOM_GYRO);
		
		Encoder leftEncoder = new Encoder(LEFT_ENCODER_A, LEFT_ENCODER_B);
		Encoder rightEncoder = new Encoder(RIGHT_ENCODER_A, RIGHT_ENCODER_B);

		RobotDrive rawDrive = new RobotDrive(leftMotor, rightMotor);
		drive = new Drive(rawDrive, topGyro, bottomGyro, rightEncoder, leftEncoder);
		drive.setDistancePerPulse(ENCODER_DISTANCE_PER_PULSE);
	}
	
	@Override
	public void autonomousInit() {
		drive.enable();
	}

	@Override
	public void autonomousPeriodic() {
		drive.update();
	}
	
	@Override
	public void teleopInit() {
		drive.disable();
	}

	@Override
	public void teleopPeriodic() {
		double move = -driveStick.getY();
		double rotate = -driveStick.getTwist();
		drive.arcadeDrive(0.7 * move, 0.7 * rotate);
	}

	@Override
	public void disabledInit() {
		drive.disable();
	}
	
	@Override
	public void disabledPeriodic() {
	}
}

