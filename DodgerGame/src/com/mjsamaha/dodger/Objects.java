package com.mjsamaha.dodger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Objects {
	
	/**
	 * Represents objects falling from top of screen
	 * - position (x, y)
	 * - size (width, height)
	 * - movement speed
	 * - object color
	 * - method to update position based on speed and time delta
	 * - method to get bounding box for collision detection
	 * - method to draw object on screen
	 * - method to check if object is off screen (for removal)
	 * 
	 * note: GamePanel handles spawning, collision detection, and game state
	 */
	
	private float x, y;
	private int width, height;
	private float speed;
	
	private Color objectColor;
	
	public Objects(float x, float y, int width, int height, float speed, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.objectColor = color;
	}
	
	// update pos based on dt (object falls downward)
	public void update(float dt) {
		y += speed * dt;
	}
	
	// get bounding box for collision detection
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	
	// draw object on screen
	public void draw(Graphics2D g2d) {
		g2d.setColor(objectColor);
		g2d.fillRect((int)x, (int)y, width, height);
	}
	
	// check if objects is off screen (below bottom edge)
	public boolean isOffScreen(int screenHeight) {
		return y > screenHeight;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Color getObjectColor() {
		return objectColor;
	}

	public void setObjectColor(Color objectColor) {
		this.objectColor = objectColor;
	}
}
