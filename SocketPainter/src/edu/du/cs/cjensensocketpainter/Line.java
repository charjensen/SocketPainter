package edu.du.cs.cjensensocketpainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

public class Line extends PaintingPrimitive implements Serializable {

	private Point start;
	private Point end;
	protected Color c;

	public Line(Color c, Point s, Point e) {
		super(c);
		start = s;
		end = e;
	}

	public void drawGeometry(Graphics g) {
		g.drawLine(start.x - 41, start.y - 56, end.x - 41, end.y - 56);
		// 41, 56
	}

}
