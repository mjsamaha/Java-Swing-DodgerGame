package com.mjsamaha.dodger.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class FallingObject extends GameObject {
    private float speed;
    private Color objectColor;
    
    public FallingObject(float x, float y, int width, int height, float speed, Color color) {
        super(x, y, width, height);
        this.speed = speed;
        this.objectColor = color;
    }
    
    // Update position based on delta time (object falls downward)
    public void update(float dt) {
        updatePreviousPosition();
        y += speed * dt;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(objectColor);
        g2d.fillRect((int)x, (int)y, width, height);
    }
    
    /**
     * Draws the falling object with interpolation for smooth rendering.
     * @param g2d Graphics context
     * @param alpha Interpolation factor (0.0 to 1.0)
     */
    public void drawInterpolated(Graphics2D g2d, double alpha) {
        g2d.setColor(objectColor);
        float interpX = getInterpolatedX(alpha);
        float interpY = getInterpolatedY(alpha);
        g2d.fillRect((int)interpX, (int)interpY, width, height);
    }
    
    // Check if object is off screen (below bottom edge)
    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
    
    // Getters and Setters
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