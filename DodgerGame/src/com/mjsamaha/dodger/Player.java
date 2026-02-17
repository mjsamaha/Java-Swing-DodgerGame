package com.mjsamaha.dodger;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {
	
	/**
	 * Represents player in game
	 * - position (x, y)
	 * - size (width, height)
	 * - movement speed
	 * - player color
	 * - methods to move left, right, up, down
	 * - method to get bounding box for collision detection
	 * - method to draw player on screen
	 * - method to keep player within game bounds
	 */
	
	private float x, y;
	private int width, height;
	private float speed;
	
	private Color playerColor = Constants.Player.PLAYER_COLOR;
	
	public Player(float x, float y, int width, int height, float speed, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.playerColor = color;
	}
	

	
	// methods to move player: left, right, up, down
	public void moveLeft(float dt) {
		x -= speed * dt;
	}
	public void moveRight(float dt) {
		x += speed * dt;
	}
	public void moveUp(float dt) {
		y -= speed * dt;
	}
	public void moveDown(float dt) {
		y += speed * dt;
	}
	
	// method to get bounding box for collision detection
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
	
	// method to draw player on screen
	public void draw(Graphics2D g2d) {
		g2d.setColor(playerColor);
		g2d.fillRect((int)x, (int)y, width, height);
	}
	
	// method to keep player within game bounds
	public void keepWithinBounds(int panelWidth, int panelHeight) {
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		if (x + width > panelWidth) x = panelWidth - width;
		if (y + height > panelHeight) y = panelHeight - height;
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

	public Color getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}

}
