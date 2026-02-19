package com.mjsamaha.dodger.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import com.mjsamaha.dodger.Constants;
import com.mjsamaha.dodger.core.GameStateManager;
import com.mjsamaha.dodger.core.PerformanceMonitor;
import com.mjsamaha.dodger.entities.FallingObject;
import com.mjsamaha.dodger.entities.Player;

public class GameRenderer {
    
    public void render(Graphics2D g2d, Player player, ArrayList<FallingObject> fallingObjects, 
                      GameStateManager gameStateManager, int panelWidth, int panelHeight) {
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (!gameStateManager.isGameOver()) {
            // Draw falling objects
            for (FallingObject obj : fallingObjects) {
                obj.draw(g2d);
            }
            
            // Draw player
            player.draw(g2d);
            
            // Render score
            drawScore(g2d, gameStateManager.getScore());
        } else {
            // Draw game over screen
            drawGameOver(g2d, gameStateManager.getScore(), panelWidth, panelHeight);
        }
    }
    
    /**
     * Renders with interpolation for smooth visuals between update ticks.
     */
    public void renderInterpolated(Graphics2D g2d, Player player, ArrayList<FallingObject> fallingObjects, 
                                   GameStateManager gameStateManager, int panelWidth, int panelHeight, 
                                   double alpha, PerformanceMonitor perfMonitor) {
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (!gameStateManager.isGameOver()) {
            // Draw falling objects with interpolation
            for (FallingObject obj : fallingObjects) {
                obj.drawInterpolated(g2d, alpha);
            }
            
            // Draw player with interpolation
            player.drawInterpolated(g2d, alpha);
            
            // Render score
            drawScore(g2d, gameStateManager.getScore());
            
            // Render debug info if enabled
            if (Constants.GameLoop.SHOW_DEBUG_INFO && perfMonitor != null) {
                drawDebugInfo(g2d, perfMonitor, panelHeight);
            }
        } else {
            // Draw game over screen
            drawGameOver(g2d, gameStateManager.getScore(), panelWidth, panelHeight);
        }
    }
    
    private void drawScore(Graphics2D g2d, int score) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + score, 10, 30);
    }
    
    private void drawGameOver(Graphics2D g2d, int score, int panelWidth, int panelHeight) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.drawString("GAME OVER!", panelWidth / 2 - 100, panelHeight / 2 - 20);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Final Score: " + score, panelWidth / 2 - 70, panelHeight / 2 + 20);
        g2d.drawString("Press R to restart", panelWidth / 2 - 80, panelHeight / 2 + 50);
    }
    
    private void drawDebugInfo(Graphics2D g2d, PerformanceMonitor perfMonitor, int panelHeight) {
        g2d.setColor(Color.WHITE); // Semi-transparent yellow
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        
        String debugText = String.format("FPS: %d | TPS: %d | Avg FPS: %.1f | Avg TPS: %.1f",
                perfMonitor.getFps(), perfMonitor.getTps(), 
                perfMonitor.getAverageFps(), perfMonitor.getAverageTps());
        
        g2d.drawString(debugText, 10, panelHeight - 10);
    }
}