package io.x666c.pictionary.gui;

import java.awt.Color;
import java.util.ArrayList;

import io.x666c.glib4j.GDisplay;
import io.x666c.glib4j.graphics.Renderer;
import io.x666c.glib4j.input.Input;
import io.x666c.glib4j.math.vector.Point;
import io.x666c.glib4j.math.vector.Vector;

public class CanvasPane {
	
	private GDisplay g;
	private String infoString = "";
	
	private ArrayList<CanvasPoint> points = new ArrayList<>();
	
	private Color activeColor = Color.WHITE;
	private Color bgColor = Color.BLACK;
	private boolean selectingFgOrBg = true;
	
	private float brushSize = 1;
	
	private boolean clearFlag = false;
	private boolean showBrush = false;
	
	public CanvasPane(Color bg, Color fg) {
		points.add(new CanvasPoint(Vector.zero()));
		points.get(0).setConnected(false);
		
		g = new GDisplay(60, 500, this::render, this::update);
		g.synchronize(false);
		
		bgColor = bg;
		activeColor = fg;
		
		g.background(bgColor);
		
		new javax.swing.Timer(500, ev -> System.out.println(points.size())).start();
	}
	
	public GDisplay getPane() {
		return g;
	}
	
	private void render(Renderer r) {
		r.fill();
		
		r.line(0, 25, 500, 25);
		
		r.color(activeColor);
		r.rectangle(0, 0, 20, 20);
		r.color(bgColor);
		r.rectangle(21, 0, 20, 20);
		
		r.g2d().setXORMode(Color.WHITE);
		
		r.draw();
		r.color(Color.BLACK);
		r.rectangle(0, 0, 19, 19);
		r.rectangle(21, 0, 19, 19);
		
		r.g2d().setPaintMode();
		
		r.color(bgColor.getRGB() ^ ~0);
		r.text(infoString, 50, 14);
		
		if(clearFlag) {
			clearFlag = false;
			points = new ArrayList<>();
			return;
		}
		
		for (int i = 0; i < points.size() - 1; i++) {
			CanvasPoint p1 = points.get(i);
			CanvasPoint p2 = points.get(i + 1);
			
			
			if (p1.equals(p2))
				points.remove(i + 1);
			
			
			try {
				if (p1.isConnected() && p2.isConnected()) {
					r.color(p1.getNextColor());
					r.stroke(p1.getNextBrushSize());

					r.line(p1, p2);
				}
			} catch (Exception e) {
				System.out.println(p1 + "=1");
				System.out.println(p2 + "=2");
			}
		}
		
		if(showBrush) {
			r.color(bgColor.getRGB()/2);
			r.nostroke();
			Point mPos = r.mouse().xy();
			r.square(mPos.x-brushSize/2, mPos.y-brushSize/2, brushSize);
		}
	}
	
	private volatile boolean pressedBefore = false;
	
	private void update() {
		if(Input.mouse.lmb()) {
			Vector vec = Input.mouse.vec_xy(0, 0);
			if(vec != null) {
				CanvasPoint p = new CanvasPoint(vec);
				if(!pressedBefore)
					p.setConnected(false);
				
				p.setNextColor(activeColor);
				p.setNextBrushSize(brushSize);
				points.add(p);
				
				pressedBefore = true;
			} else {
				pressedBefore = false;
			}
		} else {
			pressedBefore = false;
		}
		
		
		Color activeColor = null;
		
		if(Input.keyboard.key('_')) {
			System.out.println("bg");
		}
		if(Input.keyboard.key('1')) {
			activeColor = Color.BLACK;
		}
		if(Input.keyboard.key('2')) {
			activeColor = Color.GRAY;
		}
		if(Input.keyboard.key('3')) {
			activeColor = Color.WHITE;
		}
		if(Input.keyboard.key('4')) {
			activeColor = Color.RED;
		}
		if(Input.keyboard.key('5')) {
			activeColor = Color.GREEN;
		}
		if(Input.keyboard.key('6')) {
			activeColor = Color.YELLOW;
		}
		if(Input.keyboard.key('7')) {
			activeColor = Color.CYAN;
		}
		if(Input.keyboard.key('8')) {
			activeColor = Color.BLUE;
		}
		if(Input.keyboard.key('9')) {
			activeColor = Color.MAGENTA;
		}
		if(Input.keyboard.key('0')) {
			activeColor = new Color(0x654321);
		}
		
		if(activeColor != null) {
			if(selectingFgOrBg) {
				this.activeColor = activeColor;
			} else {
				this.bgColor = activeColor;
				g.background(bgColor);
			}
		}
		
		
		if(Input.keyboard.key('q')) {
			brushSize = 0.5f;
		}
		if(Input.keyboard.key('w')) {
			brushSize = 1f;
		}
		if(Input.keyboard.key('e')) {
			brushSize = 3.5f;
		}
		if(Input.keyboard.key('r')) {
			brushSize = 7f;
		}
		
		if(Input.keyboard.key('c')) {
			clearFlag = true;
		}
		
		if(Input.keyboard.key('b')) {
			showBrush = true;
			g.hideCursor(true);
		}
		if(Input.keyboard.key('n')) {
			showBrush = false;
			g.hideCursor(false);
		}
		
		if(Input.keyboard.key('f')) {
			selectingFgOrBg = true;
		}
		if(Input.keyboard.key('g')) {
			selectingFgOrBg = false;
		}
		
		infoString = ("Size: " + brushSize + "    " + "Color: " + this.activeColor.getRed() + " " + this.activeColor.getGreen() + " " + this.activeColor.getBlue());
	}
	
	public CanvasPoint[] getDrawing() {
		return points.toArray(new CanvasPoint[0]);
	}
	
	private class CanvasPoint extends Vector {
		private boolean isConnected = true;
		private Color nextColor;
		private float nextBrushSize;
		
		public void setConnected(boolean isConnected) {
			this.isConnected = isConnected;
		}
		public boolean isConnected() {
			return isConnected;
		}
		public void setNextColor(Color nextColor) {
			this.nextColor = nextColor;
		}
		public Color getNextColor() {
			return nextColor;
		}
		public void setNextBrushSize(float nextBrushSize) {
			this.nextBrushSize = nextBrushSize;
		}
		public float getNextBrushSize() {
			return nextBrushSize;
		}
		
		public CanvasPoint(Vector v) {
			super(v.x, v.y);
		}
	}
	
}