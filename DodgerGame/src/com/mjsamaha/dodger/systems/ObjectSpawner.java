package com.mjsamaha.dodger.systems;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.mjsamaha.dodger.Constants;
import com.mjsamaha.dodger.core.GameStateManager;
import com.mjsamaha.dodger.entities.FallingObject;

public class ObjectSpawner {
    private ArrayList<FallingObject> fallingObjects;
    private Random random;
    private float spawnTimer;
    private float spawnInterval;
    private float gameTime;  // Track total elapsed game time
    
    public ObjectSpawner() {
        this.fallingObjects = new ArrayList<>();
        this.random = new Random();
        this.spawnTimer = 0;
        this.spawnInterval = Constants.Objects.INITIAL_SPAWN_INTERVAL;
        this.gameTime = 0;
    }
    
    public void update(float dt, int panelWidth, int panelHeight, GameStateManager gameStateManager) {
        // Update game time and gradually increase difficulty
        gameTime += dt;
        updateDifficulty(dt);
        
        // Update spawn timer and spawn objects
        spawnTimer += dt;
        if (spawnTimer >= spawnInterval) {
            spawnObject(panelWidth);
            spawnTimer = 0;
        }
        
        // Update falling objects
        for (FallingObject obj : fallingObjects) {
            obj.update(dt);
        }
        
        // Remove off-screen objects and increment score
        Iterator<FallingObject> iterator = fallingObjects.iterator();
        while (iterator.hasNext()) {
            FallingObject obj = iterator.next();
            if (obj.isOffScreen(panelHeight)) {
                iterator.remove();
                gameStateManager.incrementScore();
            }
        }
    }
    
    private void updateDifficulty(float dt) {
        // Gradually decrease spawn interval to increase difficulty
        spawnInterval -= Constants.Objects.DIFFICULTY_INCREASE_RATE * dt;
        
        // Clamp to minimum spawn interval
        if (spawnInterval < Constants.Objects.MIN_SPAWN_INTERVAL) {
            spawnInterval = Constants.Objects.MIN_SPAWN_INTERVAL;
        }
    }
    
    private void spawnObject(int panelWidth) {
        int maxX = panelWidth - Constants.Objects.OBJECT_WIDTH;
        float randomX = random.nextInt(Math.max(1, maxX));
        float startY = -Constants.Objects.OBJECT_HEIGHT;
        
        // Select a random color from the array
        Color randomColor = Constants.Objects.OBJECT_COLORS[
            random.nextInt(Constants.Objects.OBJECT_COLORS.length)
        ];
        
        FallingObject newObj = new FallingObject(
            randomX, startY,
            Constants.Objects.OBJECT_WIDTH, 
            Constants.Objects.OBJECT_HEIGHT,
            Constants.Objects.OBJECT_SPEED,
            randomColor  // Use random color instead of constant
        );
        fallingObjects.add(newObj);
    }
    
    public void reset() {
        fallingObjects.clear();
        spawnTimer = 0;
        spawnInterval = Constants.Objects.INITIAL_SPAWN_INTERVAL;
        gameTime = 0;  // Reset game time
    }
    
    public ArrayList<FallingObject> getFallingObjects() {
        return fallingObjects;
    }
    
    // Getter for current spawn interval (useful for debugging/display)
    public float getSpawnInterval() {
        return spawnInterval;
    }
}