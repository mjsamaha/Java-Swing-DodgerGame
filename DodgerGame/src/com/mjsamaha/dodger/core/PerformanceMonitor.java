package com.mjsamaha.dodger.core;

/**
 * Monitors game performance metrics including FPS and TPS.
 * Provides real-time statistics for debugging and optimization.
 */
public class PerformanceMonitor {
    
    private int fps;
    private int tps;
    private int frameCount;
    private int tickCount;
    private long lastSecond;
    
    private double averageFps;
    private double averageTps;
    private int sampleCount;
    
    public PerformanceMonitor() {
        this.fps = 0;
        this.tps = 0;
        this.frameCount = 0;
        this.tickCount = 0;
        this.lastSecond = System.currentTimeMillis();
        this.averageFps = 0;
        this.averageTps = 0;
        this.sampleCount = 0;
    }
    
    /**
     * Call this method every time a frame is rendered.
     */
    public void recordFrame() {
        frameCount++;
        updateMetrics();
    }
    
    /**
     * Call this method every time a tick/update occurs.
     */
    public void recordTick() {
        tickCount++;
        updateMetrics();
    }
    
    /**
     * Updates FPS and TPS counters every second.
     */
    private void updateMetrics() {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastSecond >= 1000) {
            fps = frameCount;
            tps = tickCount;
            
            // Update running averages
            sampleCount++;
            averageFps = ((averageFps * (sampleCount - 1)) + fps) / sampleCount;
            averageTps = ((averageTps * (sampleCount - 1)) + tps) / sampleCount;
            
            frameCount = 0;
            tickCount = 0;
            lastSecond = currentTime;
        }
    }
    
    /**
     * Resets all performance metrics.
     */
    public void reset() {
        fps = 0;
        tps = 0;
        frameCount = 0;
        tickCount = 0;
        averageFps = 0;
        averageTps = 0;
        sampleCount = 0;
        lastSecond = System.currentTimeMillis();
    }
    
    // Getters
    public int getFps() {
        return fps;
    }
    
    public int getTps() {
        return tps;
    }
    
    public double getAverageFps() {
        return averageFps;
    }
    
    public double getAverageTps() {
        return averageTps;
    }
    
    /**
     * Returns a formatted debug string with performance metrics.
     */
    public String getDebugString() {
        return String.format("FPS: %d | TPS: %d | Avg FPS: %.1f | Avg TPS: %.1f",
                fps, tps, averageFps, averageTps);
    }
}
