package org.usfirst.frc.team484.robot;

import static org.usfirst.frc.team484.robot.RobotMap.ENCODER_DISTANCE_PER_PULSE;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	private PIDConvergenceDetector forwardPid, rotationPid;
	
	private RobotDrive drive;
	
//	private SpeedController rightMotor, leftMotor;
	private Encoder rightEncoder, leftEncoder;
	private AnalogGyro topGyro, bottomGyro;
	
	private double forwardPidOut = 0.0, rotationPidOut = 0.0;
	
	public Drive(RobotDrive drive, AnalogGyro topGyro, AnalogGyro bottomGyro, Encoder rightEncoder, Encoder leftEncoder) {
		this.drive = drive;
		
		this.rightEncoder = rightEncoder;
		this.leftEncoder = leftEncoder;
		this.topGyro = topGyro;
		this.bottomGyro = bottomGyro;
		
		forwardPid = new PIDConvergenceDetector(new PIDController(0.05, 0.0, 0.4, leftEncoder, output -> {
			forwardPidOut = output;
			putDiagnostics();
		}), 1.0);
		
		rotationPid = new PIDConvergenceDetector(new PIDController(0.1, 0.0, 0.0, topGyro, output -> {
			rotationPidOut = output;
			putDiagnostics();
		}), 1.0);
		
		forwardPid.controller.setOutputRange(-0.8, 0.8);
		rotationPid.controller.setOutputRange(-0.8, 0.8);
		
		forwardPid.controller.setAbsoluteTolerance(1.0);
		rotationPid.controller.setAbsoluteTolerance(5.0);
	}
	
	public void setDistancePerPulse(double distancePerPulse) {
		leftEncoder.setDistancePerPulse(distancePerPulse);
		rightEncoder.setDistancePerPulse(distancePerPulse);
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
		topGyro.reset();
		
		// Start the PID loop
		forwardPid.controller.setSetpoint(120.0);
		forwardPid.controller.enable();
		rotationPid.controller.setSetpoint(0.0);
		rotationPid.controller.enable();
	}
	
	public void update() {
		drive.arcadeDrive(forwardPidOut, -rotationPidOut);
		System.out.println(forwardPid.hasConverged() + " " + rotationPid.hasConverged());
	}
	
	public void disable() {
		forwardPid.controller.disable();
	}
	
	public void arcadeDrive(double forwards, double rotation) {
		drive.arcadeDrive(forwards, rotation);
	}
	
}
