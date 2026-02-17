package com.mjsamaha.dodger.core;

import com.mjsamaha.dodger.Constants;

/**
 * Advanced game loop implementation with separate update and render threads.
 * Uses fixed timestep for updates and frame interpolation for smooth rendering.
 */
public class GameLoop {
    
    private GamePanel gamePanel;
    private PerformanceMonitor perfMonitor;
    
    private volatile boolean running;
    private Thread updateThread;
    private Thread renderThread;
    
    // Timing constants
    private final double TICK_DURATION = 1.0 / Constants.GameLoop.TARGET_TPS;
    private final double FRAME_DURATION = 1.0 / Constants.GameLoop.TARGET_FPS;
    
    // Interpolation factor for smooth rendering
    private volatile double alpha = 0.0;
    
    // Thread synchronization
    private final Object updateLock = new Object();
    
    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.perfMonitor = new PerformanceMonitor();
        this.running = false;
    }
    
    /**
     * Starts the game loop threads.
     */
    public void start() {
        if (running) {
            return;
        }
        
        running = true;
        perfMonitor.reset();
        
        // Start update thread
        updateThread = new Thread(this::updateLoop, "Update-Thread");
        updateThread.setDaemon(false);
        updateThread.start();
        
        // Start render thread
        renderThread = new Thread(this::renderLoop, "Render-Thread");
        renderThread.setDaemon(false);
        renderThread.start();
    }
    
    /**
     * Stops the game loop threads gracefully.
     */
    public void stop() {
        running = false;
        
        try {
            if (updateThread != null) {
                updateThread.join(1000);
            }
            if (renderThread != null) {
                renderThread.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Game loop shutdown interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Update loop - runs at fixed TPS (Ticks Per Second).
     * Handles all game logic with fixed timestep for deterministic updates.
     */
    private void updateLoop() {
        double accumulator = 0.0;
        long lastUpdateTime = System.nanoTime();
        
        while (running) {
            long currentTime = System.nanoTime();
            double frameTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = currentTime;
            
            // Prevent spiral of death - cap frame time
            if (frameTime > Constants.GameLoop.MAX_FRAME_TIME) {
                frameTime = Constants.GameLoop.MAX_FRAME_TIME;
            }
            
            accumulator += frameTime;
            
            // Fixed timestep updates
            while (accumulator >= TICK_DURATION) {
                synchronized (updateLock) {
                    gamePanel.updateGame((float) TICK_DURATION);
                }
                
                accumulator -= TICK_DURATION;
                perfMonitor.recordTick();
            }
            
            // Calculate interpolation factor for smooth rendering
            alpha = accumulator / TICK_DURATION;
            
            // Sleep briefly to prevent CPU hogging
            sleepNanos(100_000); // 0.1ms
        }
    }
    
    /**
     * Render loop - runs at target FPS with smooth frame limiting.
     * Only handles rendering, uses interpolation for smooth visuals.
     */
    private void renderLoop() {
        long lastFrameTime = System.nanoTime();
        
        while (running) {
            long frameStartTime = System.nanoTime();
            
            // Render the frame
            synchronized (updateLock) {
                gamePanel.renderGame(alpha);
            }
            
            perfMonitor.recordFrame();
            
            // Frame limiting with high-precision timing
            if (!Constants.GameLoop.VSYNC_ENABLED) {
                long frameTime = System.nanoTime() - frameStartTime;
                long targetFrameTime = (long) (FRAME_DURATION * 1_000_000_000);
                long sleepTime = targetFrameTime - frameTime;
                
                if (sleepTime > 0) {
                    sleepNanos(sleepTime);
                }
            }
            
            lastFrameTime = System.nanoTime();
        }
    }
    
    /**
     * High-precision sleep using a combination of Thread.sleep and busy-waiting.
     * More accurate than Thread.sleep alone, especially for short durations.
     */
    private void sleepNanos(long nanos) {
        long start = System.nanoTime();
        long end = start + nanos;
        
        // Use Thread.sleep for most of the time (not very precise)
        if (nanos > 2_000_000) { // 2ms
            try {
                Thread.sleep((nanos - 1_000_000) / 1_000_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Busy-wait for the remaining time (very precise)
        while (System.nanoTime() < end) {
            Thread.onSpinWait(); // Java 9+ hint for busy-waiting
        }
    }
    
    /**
     * Returns the performance monitor for accessing FPS/TPS metrics.
     */
    public PerformanceMonitor getPerformanceMonitor() {
        return perfMonitor;
    }
    
    /**
     * Returns the current interpolation factor (0.0 to 1.0).
     * Used for smooth rendering between update ticks.
     */
    public double getAlpha() {
        return alpha;
    }
    
    /**
     * Checks if the game loop is currently running.
     */
    public boolean isRunning() {
        return running;
    }
}
