package io.x666c.pictionary.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import io.x666c.pictionary.gui.CanvasPane;

public class GameFrame extends JFrame {
	
	public GameFrame() {
		setLayout(new BorderLayout());
		
		CanvasPane canvas = new CanvasPane(Color.WHITE, Color.BLACK);
		canvas.getPane().setDisplaySize(650, 650);
		getContentPane().add(canvas.getPane(), BorderLayout.CENTER);
		
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setVisible(true);
		
		canvas.getPane().start();
	}
	
}