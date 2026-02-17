package com.mjsamaha.dodger.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class GameObject {
	
	protected float x, y;
	protected float prevX, prevY; // Previous position for interpolation
	protected int width, height;
	
	public GameObject(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.prevX = x;
		this.prevY = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Updates previous position to current position.
	 * Call this before updating position for interpolation support.
	 */
	public void updatePreviousPosition() {
		prevX = x;
		prevY = y;
	}
	
	/**
	 * Gets interpolated X position for smooth rendering.
	 * @param alpha Interpolation factor (0.0 to 1.0)
	 */
	public float getInterpolatedX(double alpha) {
		return (float) (prevX + (x - prevX) * alpha);
	}
	
	/**
	 * Gets interpolated Y position for smooth rendering.
	 * @param alpha Interpolation factor (0.0 to 1.0)
	 */
	public float getInterpolatedY(double alpha) {
		return (float) (prevY + (y - prevY) * alpha);
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	
	public abstract void draw(Graphics2D g2d);

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
}
