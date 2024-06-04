package edu.du.cs.cjensensocketpainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

public class Circle extends PaintingPrimitive implements Serializable {

	private Point center;
	private Point radiusPoint;
	protected Color c;

	public Circle(Color c, Point cr, Point rp) {
		super(c);
		center = cr;
		radiusPoint = rp;
	}

	public void drawGeometry(Graphics g) {
		int radius = (int) Math.abs(center.distance(radiusPoint));
		g.drawOval(center.x - 41 - radius, center.y - 56 - radius, radius * 2, radius * 2);
		// 41, 56
	}

}
