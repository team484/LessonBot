package org.usfirst.frc.team484.robot;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
	private PIDController forwardPid, rotationPid;
	private RobotDrive drive;
	private LinkedList<Point> botPath;
	private double prevLength = 0;
	
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
		
		this.botPath = new LinkedList<>();
		
		forwardPid = new PIDController(0.05, 0.0, 0.4, leftEncoder, output -> {
			forwardPidOut = output;
			putDiagnostics();
		});
		
		rotationPid = new PIDController(0.1, 0.0, 0.0, topGyro, output -> {
			rotationPidOut = output;
			putDiagnostics();
		});
		
		forwardPid.setOutputRange(-0.8, 0.8);
		rotationPid.setOutputRange(-0.8, 0.8);
		
		forwardPid.setAbsoluteTolerance(1.0);
		rotationPid.setAbsoluteTolerance(5.0);
	}
	
	private static final double ENCODER_EPSILON = 1;
	
	private boolean converged(PIDController controller) {
		return leftEncoder.getRate() < ENCODER_EPSILON
				&& rightEncoder.getRate() < ENCODER_EPSILON
				&& controller.onTarget();
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
		forwardPid.setSetpoint(120.0);
		forwardPid.enable();
		rotationPid.setSetpoint(0.0);
		rotationPid.enable();
	}
	
	private boolean isTurning = false;
	
	private Point getNext() {
		return botPath.get(1);
	}
	
	private double getNextLength() {
		return prevLength + botPath.getFirst().distance(getNext());
	}
	
	public void update() {
		drive.arcadeDrive(forwardPidOut, rotationPidOut);
		
		// This kinda looks like a state machine...
		if (isTurning) {
			// if we were turning and have finished, move on.
			if (converged(rotationPid)) {
				isTurning = false;
				Point removed = botPath.removeFirst();
				System.out.println("Reached " + removed + ", removed from list.");
			}
			
			// Figure out the angle we need to rotate
			Point next = getNext();
			Point current = botPath.getFirst();
			double theta = Math.atan2(next.y - current.y, next.x - current.x);
			
			rotationPid.setSetpoint(Math.toDegrees(theta));
		} else {
			// if we have reached where we are trying to go,
			// try to align the bot towards the next point
			if (converged(forwardPid)) {
				isTurning = true;
				// Append the length we just traveled to the
				// previous length we've driven
				prevLength = getNextLength();
			}
			
			forwardPid.setSetpoint(getNextLength());
		}
	}
	
	public void disable() {
		forwardPid.disable();
	}
	
	public void arcadeDrive(double forwards, double rotation) {
		drive.arcadeDrive(forwards, rotation);
	}
	
}
