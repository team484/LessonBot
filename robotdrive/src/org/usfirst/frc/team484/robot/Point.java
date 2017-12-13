package org.usfirst.frc.team484.robot;

public final class Point {
	public double x = 0, y = 0;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distance(Point p) {
		return Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2);
	}
}
