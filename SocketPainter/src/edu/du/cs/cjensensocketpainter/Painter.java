package edu.du.cs.cjensensocketpainter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class Painter extends JFrame
		implements ActionListener, MouseListener, MouseMotionListener, InputMethodListener, KeyListener {

	enum Shape {
		CIRCLE, LINE
	}

	private Shape s = Shape.LINE;
	private Color color = Color.RED;
	private Point start;
	private Point stop;
	private PaintingPanel p;
	private Socket so;
	private ObjectOutputStream oos;
	private String name = JOptionPane.showInputDialog("Enter your name");
	private JPanel textConatiner;
	private JPanel messageBox;
	private JTextField textBox;
	private JButton submit;
	private JTextArea messages;
	private JScrollPane scrollPane;

	public Painter() {

		setSize(500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		JPanel shapes = new JPanel();
		// CHANGE LATER
		shapes.setLayout(new GridLayout(1, 2)); // 3 by 1
		JButton line = new JButton("Line");
		line.addActionListener(this);
		line.setActionCommand("line");
		JButton circle = new JButton("Circle");
		circle.addActionListener(this);
		circle.setActionCommand("circle");

		shapes.add(line, BorderLayout.WEST);
		shapes.add(circle, BorderLayout.EAST);

		content.add(shapes, BorderLayout.NORTH);

		JPanel colors = new JPanel();
		colors.setLayout(new GridLayout(3, 1)); // 3 by 1

		JButton red = new JButton("");
		red.setBackground(Color.RED);
		red.setOpaque(true);
		red.setBorderPainted(false);
		red.addActionListener(this);
		red.setActionCommand("red");
		colors.add(red); // Added in next open cell in the grid

		JButton blue = new JButton("");
		blue.setBackground(Color.BLUE);
		blue.setOpaque(true);
		blue.setBorderPainted(false);
		blue.addActionListener(this);
		blue.setActionCommand("blue");
		colors.add(blue); // Added in next open cell in the grid

		JButton green = new JButton("");
		green.setBackground(Color.GREEN);
		green.setOpaque(true);
		green.setBorderPainted(false);
		green.addActionListener(this);
		green.setActionCommand("green");
		colors.add(green); // Added in next open cell in the grid

		content.add(colors, BorderLayout.WEST);

		textConatiner = new JPanel();
		messageBox = new JPanel();
		messageBox.setLayout(new BorderLayout());

		textBox = new JTextField();
		textBox.setToolTipText("Type your message here...");

		submit = new JButton("Send");
		submit.addActionListener(this);
		submit.setActionCommand("send");

		messageBox.add(textBox);
		messageBox.add(submit, BorderLayout.EAST);

		messages = new JTextArea(7, 7);
		messages.setEditable(false);
		messages.setLineWrap(true);
		messages.setBackground(Color.LIGHT_GRAY);
		scrollPane = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setAutoscrolls(true);

		textConatiner.setLayout(new BorderLayout());
		textConatiner.add(messageBox, BorderLayout.NORTH);
		textConatiner.add(scrollPane);
		textBox.addKeyListener(this);

		content.add(textConatiner, BorderLayout.SOUTH);

		p = new PaintingPanel();

		content.add(p, BorderLayout.CENTER);

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		setContentPane(content);
		setVisible(true);

		try {
			System.out.println("About to call");
			so = new Socket("localhost", 7000);
			System.out.println("Connected");
			Thread th = new Thread(new ClientThread(this, new ObjectInputStream(this.so.getInputStream())));
			oos = new ObjectOutputStream(so.getOutputStream());
			th.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public PaintingPanel getP() {

		return p;

	}

	public void setP(PaintingPanel p) {

		this.p = p;

	}

	public void addMsg(String s) {

		messages.append(s + "\n");

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		stop = e.getPoint();
		if (s.equals(Shape.LINE))
			p.updateTemp(new Line(color, start, stop));
		if (s.equals(Shape.CIRCLE))
			p.updateTemp(new Circle(color, start, stop));

		p.repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		start = e.getPoint();
		System.out.println(start.x + ", " + start.y);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		stop = e.getPoint();
		if (s.equals(Shape.LINE)) {
			Line l = new Line(color, start, stop);
			p.addPrimitive(l);
			try {
				oos.writeObject(l);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (s.equals(Shape.CIRCLE)) {
			Circle c = new Circle(color, start, stop);
			p.addPrimitive(c);
			try {
				oos.writeObject(c);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		p.removeTemp();
		p.repaint();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("red")) {
			color = Color.RED;
			System.out.println("red");
		} else if (e.getActionCommand().equals("blue")) {
			color = Color.BLUE;
			System.out.println("blue");
		} else if (e.getActionCommand().equals("green")) {
			color = Color.GREEN;
			System.out.println("green");
		} else if (e.getActionCommand().equals("circle")) {
			s = Shape.CIRCLE;
			System.out.println("circle");
		} else if (e.getActionCommand().equals("line")) {
			s = Shape.LINE;
			System.out.println("line");
		} else if (e.getActionCommand().equals("send")) {
			String msg = textBox.getText();
			textBox.setText("");
			try {
				oos.writeObject(msg);
				messages.append(this.name + ": " + msg + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String msg = textBox.getText();
			textBox.setText("");
			try {
				oos.writeObject(this.name + ": " + msg);
				messages.append(this.name + ": " + msg + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputMethodTextChanged(InputMethodEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caretPositionChanged(InputMethodEvent event) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		new Painter();

	}

}
