package com.mjsamaha.dodger.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.mjsamaha.dodger.Constants;
import com.mjsamaha.dodger.audio.AudioManager;
import com.mjsamaha.dodger.entities.Player;
import com.mjsamaha.dodger.input.InputHandler;
import com.mjsamaha.dodger.rendering.GameRenderer;
import com.mjsamaha.dodger.systems.CollisionDetector;
import com.mjsamaha.dodger.systems.ObjectSpawner;

public class GamePanel extends JPanel implements ActionListener {
    
    private Timer timer;
    private long lastUpdateTime;
    
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
        
        timer = new Timer(1000 / 60, this);
        
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
        lastUpdateTime = System.nanoTime();
        timer.start();
        audioManager.playBackgroundMusic();
        requestFocusInWindow();
    }
    
    private void updateGame(float dt) {
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
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        gameRenderer.render(g2d, player, objectSpawner.getFallingObjects(), 
                          gameStateManager, getWidth(), getHeight());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check for restart request
        if (gameStateManager.isRestartRequested()) {
            restartGame();
        }
        
        if (!gameStateManager.isGameOver()) {
            // Calculate delta time
            long currentTime = System.nanoTime();
            float dt = (currentTime - lastUpdateTime) / 1_000_000_000.0f;
            lastUpdateTime = currentTime;
            
            updateGame(dt);
        }
        repaint();
    }
    
    private void restartGame() {
        lastUpdateTime = System.nanoTime();
        
        // Reset player position
        player.setX(Constants.Player.START_X);
        player.setY(Constants.Player.START_Y);
        
        // Reset all game components
        gameStateManager.reset();
        inputHandler.reset();
        objectSpawner.reset();
        
        audioManager.playBackgroundMusic();
    }
    
    public void cleanup() {
    	timer.stop();
    	audioManager.cleanup();
    }
}