package com.mjsamaha.dodger.systems;

import java.util.ArrayList;

import com.mjsamaha.dodger.entities.FallingObject;
import com.mjsamaha.dodger.entities.Player;

public class CollisionDetector {
    
    public static boolean checkCollisions(Player player, ArrayList<FallingObject> fallingObjects) {
        for (FallingObject obj : fallingObjects) {
            if (player.getBounds().intersects(obj.getBounds())) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }
}