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
		
	}
}
