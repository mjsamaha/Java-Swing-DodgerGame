package com.mjsamaha.dodger;

import java.awt.Color;

public class Constants {
	
	public static final class Window {
		public static final int WINDOW_WIDTH = 800;
		public static final int WINDOW_HEIGHT = 600;
		
		public static final String WINDOW_TITLE = "Dodger Game";
		public static final String VER = "0.1";
		
	}

	public static final class Player {
		public static final int START_X = 200;
		public static final int START_Y = 350;
		public static final int PLAYER_WIDTH = 40;
		public static final int PLAYER_HEIGHT = 40;
		public static final float PLAYER_SPEED = 300;
		
		public static final Color PLAYER_COLOR = Color.BLUE;
		
	}
	
	public static final class Objects {
		public static final int OBJECT_WIDTH = 40;
		public static final int OBJECT_HEIGHT = 40;
		public static final float OBJECT_SPEED = 200;
		
		public static final Color OBJECT_COLOR = Color.RED;
		
		// Array of possible colors for falling objects
		public static final Color[] OBJECT_COLORS = {
			Color.RED,
			Color.GREEN,
			Color.YELLOW,
			Color.ORANGE,
			Color.MAGENTA,
			new Color(255, 0, 255),   // Purple/Pink
			new Color(0, 255, 255),   // Cyan
			new Color(255, 100, 100)  // Light Red
		};
		
		// Difficulty progression settings
		public static final float INITIAL_SPAWN_INTERVAL = 1.0f;  // Start spawning every 1 second
		public static final float MIN_SPAWN_INTERVAL = 0.3f;      // Maximum difficulty: spawn every 0.3 seconds
		public static final float DIFFICULTY_INCREASE_RATE = 0.01f; // Decrease spawn interval by 0.01 every second
	}
	
	public static final class Audio {
		public static final String BG_MUSIC = "assets/audio/background.wav";
		public static final String SFX_COLLISION = "assets/audio/collision.wav";
		public static final String SCORE_SOUND = "assets/audio/score.wav";
		
		public static final float DEFAULT_MUSIC_VOL = 0.9f;
		public static final float DEFAULT_SFX_VOL = 0.8f;
	}
	
	public static final class GameLoop {
		// Target updates per second (game logic)
		public static final int TARGET_TPS = 60;
		
		// Target frames per second (rendering)
		public static final int TARGET_FPS = 60;
		
		// Enable VSync if available (overrides TARGET_FPS)
		public static final boolean VSYNC_ENABLED = false;
		
		// Display FPS/TPS debug information
		public static final boolean SHOW_DEBUG_INFO = true;
		
		// Maximum frame time to prevent spiral of death
		public static final float MAX_FRAME_TIME = 0.25f;
	}
}
