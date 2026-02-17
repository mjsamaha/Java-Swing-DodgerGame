package com.mjsamaha.dodger.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.mjsamaha.dodger.Constants;
import com.mjsamaha.dodger.audio.AudioManager;
import com.mjsamaha.dodger.entities.Player;
import com.mjsamaha.dodger.input.InputHandler;
import com.mjsamaha.dodger.rendering.GameRenderer;
import com.mjsamaha.dodger.systems.CollisionDetector;
import com.mjsamaha.dodger.systems.ObjectSpawner;

public class GamePanel extends JPanel {
    
    private GameLoop gameLoop;
    
    // Core game components
    private Player player;
    private GameStateManager gameStateManager;
    private InputHandler inputHandler;
    private ObjectSpawner objectSpawner;
    private GameRenderer gameRenderer;
    private AudioManager audioManager;
    
    public GamePanel() {
        setPreferredSize(new Dimension(Constants.Window.WINDOW_WIDTH, Constants.Window.WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Initialize game components
        gameStateManager = new GameStateManager();
        inputHandler = new InputHandler(gameStateManager);
        objectSpawner = new ObjectSpawner();
        gameRenderer = new GameRenderer();
        audioManager = new AudioManager();
        
        initializeAudio();
        
        addKeyListener(inputHandler);
        
        // Create game loop with separate update/render threads
        gameLoop = new GameLoop(this);
        
        player = new Player(
            Constants.Player.START_X, 
            Constants.Player.START_Y, 
            Constants.Player.PLAYER_WIDTH, 
            Constants.Player.PLAYER_HEIGHT, 
            Constants.Player.PLAYER_SPEED, 
            Constants.Player.PLAYER_COLOR
        );
    }
    
    private void initializeAudio() {
    	audioManager.loadBackgroundMusic(Constants.Audio.BG_MUSIC);
    	
    	audioManager.loadSoundEffect("collision", Constants.Audio.SFX_COLLISION);
    	
    	audioManager.loadSoundEffect("score", Constants.Audio.SCORE_SOUND);
    	
    	audioManager.setMusicVolume(Constants.Audio.DEFAULT_MUSIC_VOL);
    	
    	audioManager.setSfxVolume(Constants.Audio.DEFAULT_SFX_VOL);
    	
    	
    }
    
    public void startGame() {
        gameLoop.start();
        audioManager.playBackgroundMusic();
        requestFocusInWindow();
    }
    
    /**
     * Updates game logic. Called by GameLoop at fixed TPS.
     * @param dt Delta time (fixed timestep)
     */
    public void updateGame(float dt) {
        // Check for restart request
        if (gameStateManager.isRestartRequested()) {
            restartGame();
            return;
        }
        
        if (gameStateManager.isGameOver()) {
            return; // Don't update if game is over
        }
        
        // Handle player movement
        inputHandler.handlePlayerMovement(player, dt);
        player.keepWithinBounds(getWidth(), getHeight());
        
        int previousScore = gameStateManager.getScore();
        
        // Update falling objects and spawning
        objectSpawner.update(dt, getWidth(), getHeight(), gameStateManager);
        
        if (gameStateManager.getScore() > previousScore) {
        	audioManager.playSoundEffect("score");
        }
        
        // Check collisions
        if (CollisionDetector.checkCollisions(player, objectSpawner.getFallingObjects())) {
            gameStateManager.setGameOver(true);
            audioManager.playSoundEffect("collision");
            audioManager.stopBackgroundMusic();
        }
    }
    
    /**
     * Renders the game. Called by GameLoop at target FPS.
     * @param alpha Interpolation factor for smooth rendering
     */
    public void renderGame(double alpha) {
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Use interpolated rendering for smooth visuals
        if (gameLoop != null && gameLoop.isRunning()) {
            gameRenderer.renderInterpolated(g2d, player, objectSpawner.getFallingObjects(), 
                    gameStateManager, getWidth(), getHeight(), 
                    gameLoop.getAlpha(), gameLoop.getPerformanceMonitor());
        } else {
            // Fallback for initial rendering before game loop starts
            gameRenderer.render(g2d, player, objectSpawner.getFallingObjects(), 
                    gameStateManager, getWidth(), getHeight());
        }
    }
    
    private void restartGame() {
        // Reset player position
        player.setX(Constants.Player.START_X);
        player.setY(Constants.Player.START_Y);
        player.updatePreviousPosition(); // Reset interpolation
        
        // Reset all game components
        gameStateManager.reset();
        inputHandler.reset();
        objectSpawner.reset();
        
        // Reset performance monitor
        if (gameLoop != null) {
            gameLoop.getPerformanceMonitor().reset();
        }
        
        audioManager.playBackgroundMusic();
    }
    
    public void cleanup() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    	audioManager.cleanup();
    }
}