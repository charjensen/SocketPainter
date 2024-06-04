package edu.du.cs.cjensensocketpainter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaintingPanel extends JPanel {

	ArrayList<PaintingPrimitive> primitives = new ArrayList<PaintingPrimitive>();
	PaintingPrimitive temp = null;

	public PaintingPanel() {

		this.setBackground(Color.WHITE);

	}

	@Override
	public synchronized void paintComponent(Graphics g) {

		// System.out.println("Painting Component!");

		super.paintComponent(g);

		for (PaintingPrimitive p : primitives) {
			p.draw(g);
		}

		if (temp != null)
			temp.draw(g);

	}

	public synchronized void addPrimitive(PaintingPrimitive p) {
		this.primitives.add(p);
		// this.paintComponent(getGraphics());
	}

	public void updateTemp(PaintingPrimitive p) {

		this.temp = p;
		// this.paintComponent(getGraphics());

	}

	public void removeTemp() {

		this.temp = null;
		// this.paintComponent(getGraphics());

	}

}
