package org.usfirst.frc.team484.robot;

public final class RobotMap {
	public static final int DRIVE_STICK_PORT = 1;
	public static final int RIGHT_MOTOR = 1;
	public static final int LEFT_MOTOR = 0;
	
	public static final int LEFT_ENCODER_A = 1;
	public static final int LEFT_ENCODER_B = 0;
	public static final int RIGHT_ENCODER_B = 3;
	public static final int RIGHT_ENCODER_A = 2;
	
	public static final int TOP_GYRO = 0;
	public static final int BOTTOM_GYRO = 1;
	
	// The value that determines how many "pulses", the unit the encoder
	// returns, constitute some other unit (in this case, feet)
	public static final double ENCODER_DISTANCE_PER_PULSE = 12.0*(1.0/120.0);
}
