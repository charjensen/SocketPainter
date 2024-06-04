package edu.du.cs.cjensensocketpainter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ClientThread implements Runnable {

	private Painter panel;
	private ObjectInputStream ois;

	public ClientThread(Painter p, ObjectInputStream o) {

		panel = p;
		ois = o;

	}

	@Override
	public void run() {

		Object obj;
		try {
			obj = ois.readObject();
			if (obj instanceof ArrayList) {
				ArrayList<PaintingPrimitive> p = (ArrayList<PaintingPrimitive>) obj;
				for (PaintingPrimitive pp : p) {
					panel.getP().addPrimitive(pp);
				}
				panel.repaint();
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {

			try {
				Object o = ois.readObject();

				if (o instanceof PaintingPrimitive) {
					panel.getP().addPrimitive((PaintingPrimitive) o);
					panel.repaint();
				} else if (o instanceof String) {
					panel.addMsg((String) o);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
