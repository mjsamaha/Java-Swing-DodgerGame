package com.mjsamaha.dodger.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
    
    private Map<String, Clip> soundEffects;
    private Clip backgroundMusic;
    private boolean musicEnabled;
    private boolean sfxEnabled;
    private float musicVolume;
    private float sfxVolume;
    
    public AudioManager() {
        this.soundEffects = new HashMap<>();
        this.musicEnabled = true;
        this.sfxEnabled = true;
        this.musicVolume = 0.7f;
        this.sfxVolume = 0.8f;
    }
    
    /**
     * Load a sound effect from file with format conversion if needed
     */
    public void loadSoundEffect(String name, String filePath) {
        Clip clip = loadAudioClip(filePath);
        if (clip != null) {
            soundEffects.put(name, clip);
            System.out.println("Loaded sound effect: " + name);
        } else {
            System.err.println("Failed to load sound effect: " + name);
        }
    }
    
    /**
     * Load background music from file with format conversion if needed
     */
    public void loadBackgroundMusic(String filePath) {
        backgroundMusic = loadAudioClip(filePath);
        if (backgroundMusic != null) {
            System.out.println("Loaded background music");
        } else {
            System.err.println("Failed to load background music");
        }
    }
    
    /**
     * Load an audio clip with automatic format conversion
     */
    private Clip loadAudioClip(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + filePath);
                System.err.println("Looking for: " + audioFile.getAbsolutePath());
                return null;
            }
            
            // Check file extension
            String fileName = audioFile.getName().toLowerCase();
            if (fileName.endsWith(".mp3") || fileName.endsWith(".ogg")) {
                System.err.println("Unsupported format: " + fileName);
                System.err.println("Please convert to WAV (16-bit, 44.1kHz)");
                System.err.println("Supported formats: WAV, AIFF, AU");
                return null;
            }
            
            // Try to load the audio file
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat originalFormat = originalStream.getFormat();
            
            // Print format info for debugging
            System.out.println("File: " + fileName);
            System.out.println("  Format: " + originalFormat.getEncoding());
            System.out.println("  Sample Rate: " + originalFormat.getSampleRate() + " Hz");
            System.out.println("  Sample Size: " + originalFormat.getSampleSizeInBits() + " bit");
            System.out.println("  Channels: " + originalFormat.getChannels());
            
            // Convert to supported format if necessary
            AudioInputStream decodedStream = getDecodedStream(originalStream, originalFormat);
            
            // Create and open clip
            AudioFormat decodedFormat = decodedStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, decodedFormat);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(decodedStream);
            
            return clip;
            
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + e.getMessage());
            System.err.println("Please convert to WAV format (16-bit PCM, 44.1kHz)");
            return null;
        } catch (IOException e) {
            System.err.println("Error reading audio file: " + e.getMessage());
            return null;
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error loading audio: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convert audio stream to a supported format (PCM_SIGNED)
     */
    private AudioInputStream getDecodedStream(AudioInputStream originalStream, AudioFormat originalFormat) {
        AudioFormat.Encoding encoding = originalFormat.getEncoding();
        
        // If already PCM_SIGNED with 8 or 16-bit, no conversion needed
        if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED) && 
            (originalFormat.getSampleSizeInBits() == 16 || originalFormat.getSampleSizeInBits() == 8)) {
            return originalStream;
        }
        
        // Convert to 16-bit PCM_SIGNED format
        AudioFormat decodedFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            originalFormat.getSampleRate(),
            16, // 16-bit
            originalFormat.getChannels(),
            originalFormat.getChannels() * 2, // frame size
            originalFormat.getSampleRate(),
            false // little-endian
        );
        
        try {
            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, originalStream);
            System.out.println("  Converted to 16-bit PCM format");
            return decodedStream;
        } catch (Exception e) {
            System.err.println("  Could not convert format: " + e.getMessage());
            return originalStream; // Return original and hope for the best
        }
    }
    
    /**
     * Play a sound effect once
     */
    public void playSoundEffect(String name) {
        if (!sfxEnabled || !soundEffects.containsKey(name)) {
            return;
        }
        
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            setVolume(clip, sfxVolume);
            clip.start();
        }
    }
    
    /**
     * Start playing background music on loop
     */
    public void playBackgroundMusic() {
        if (backgroundMusic != null && musicEnabled) {
            backgroundMusic.setFramePosition(0);
            setVolume(backgroundMusic, musicVolume);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    /**
     * Stop background music
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    /**
     * Pause background music
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    /**
     * Resume background music from where it was paused
     */
    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && musicEnabled) {
            backgroundMusic.start();
        }
    }
    
    /**
     * Set volume for a clip (0.0 to 1.0)
     */
    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;
        
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        } catch (Exception e) {
            // Volume control not available
        }
    }
    
    /**
     * Set music volume (0.0 to 1.0)
     */
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        if (backgroundMusic != null) {
            setVolume(backgroundMusic, musicVolume);
        }
    }
    
    /**
     * Set sound effects volume (0.0 to 1.0)
     */
    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    /**
     * Toggle music on/off
     */
    public void toggleMusic() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }
    
    /**
     * Toggle sound effects on/off
     */
    public void toggleSoundEffects() {
        sfxEnabled = !sfxEnabled;
    }
    
    /**
     * Clean up audio resources
     */
    public void cleanup() {
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
        for (Clip clip : soundEffects.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        soundEffects.clear();
    }
    
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    public boolean isSfxEnabled() {
        return sfxEnabled;
    }
}