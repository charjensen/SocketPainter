package edu.du.cs.cjensensocketpainter;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public abstract class PaintingPrimitive implements Serializable {

	protected Color color;

	public PaintingPrimitive(Color c) {
		color = c;
	}

	public final void draw(Graphics g) {
		g.setColor(this.color);
		drawGeometry(g);
	}

	protected abstract void drawGeometry(Graphics g);

}
