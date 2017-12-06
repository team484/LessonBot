package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;

public class PIDConvergenceDetector {
	private double window;
	private double lastTime = 0.0;
	private boolean finished = false;
	public PIDController controller;
	
	public PIDConvergenceDetector(PIDController controller, double window) {
		this.window = window;
		this.controller = controller;
	}
	
	public boolean hasConverged() {
		return finished;
	}
	
	private void resetConverged() {
		finished = false;
		lastTime = 0.0;
	}
	
	public void calculate() {
		double currentTime = Timer.getFPGATimestamp();
		if (controller.onTarget()) {
			// If the value has stayed within the margin of error the
			// entire time, we're done.
			if (currentTime - lastTime >= window) {
				finished = true;
			}
		} else {
			// We don't update `lastTime` if our input is out of bounds,
			// so `lastTime` is really the time since last out of bounds
			lastTime = currentTime;
		}
	}
}
