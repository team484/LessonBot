package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	private PIDController forwardPid, rotationPid;
	
	private RobotDrive drive;
	
//	private SpeedController rightMotor, leftMotor;
	private Encoder rightEncoder, leftEncoder;
	private AnalogGyro topGyro, bottomGyro;
	
	public Drive(RobotDrive drive, AnalogGyro topGyro, AnalogGyro bottomGyro, Encoder rightEncoder, Encoder leftEncoder) {
		this.drive = drive;
		
		this.rightEncoder = rightEncoder;
		this.leftEncoder = leftEncoder;
		this.topGyro = topGyro;
		this.bottomGyro = bottomGyro;
		
		forwardPid = new PIDController(0.05, 0.0, 0.5, leftEncoder, output -> {
			drive.arcadeDrive(output, -0.25);
			putDiagnostics();
		});
		
		forwardPid.setOutputRange(-0.8, 0.8);
	}
	
	// Put some useful diagnostic values on the smart dashboard
	public void putDiagnostics() {
		SmartDashboard.putNumber("Right Encoder", rightEncoder.getDistance());
		SmartDashboard.putNumber("Left Encoder", leftEncoder.getDistance());
		SmartDashboard.putNumber("Top Gyro", topGyro.getAngle());
		SmartDashboard.putNumber("Bottom Gyro", bottomGyro.getAngle());
	}
	
	public void enable() {
		// Zero the encoder...
		leftEncoder.reset();
		rightEncoder.reset();
		
		// Start the PID loop
		forwardPid.setSetpoint(120.0);
		forwardPid.enable();
	}
	
	public void disable() {
		forwardPid.disable();
	}
	
	public void arcadeDrive(double forwards, double rotation) {
		drive.arcadeDrive(forwards, rotation);
	}
	
}
