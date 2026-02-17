package com.mjsamaha.dodger.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends GameObject {
    private float speed;
    private Color playerColor;
    
    public Player(float x, float y, int width, int height, float speed, Color color) {
        super(x, y, width, height);
        this.speed = speed;
        this.playerColor = color;
    }
    
    // Movement methods
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
    
    // Keep player within game bounds
    public void keepWithinBounds(int panelWidth, int panelHeight) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > panelWidth) x = panelWidth - width;
        if (y + height > panelHeight) y = panelHeight - height;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(playerColor);
        g2d.fillRect((int)x, (int)y, width, height);
    }
    
    // Getters and Setters
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public Color getPlayerColor() { return playerColor; }
    public void setPlayerColor(Color playerColor) { this.playerColor = playerColor; }
}
