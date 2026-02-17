package com.mjsamaha.dodger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.mjsamaha.dodger.core.GamePanel;

public class Main {
	
	public static void init() {
		JFrame w = new JFrame();
		
		GamePanel gp = new GamePanel();
		
		w.add(gp);
		w.setTitle(Constants.Window.WINDOW_TITLE + " " + Constants.Window.VER);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setResizable(false);
		w.pack();
		w.setLocationRelativeTo(null);
		w.setVisible(true);
		
		// Add shutdown hook for proper cleanup
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			gp.cleanup();
		}));
		
		gp.startGame();
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				init();
			}
		});
	}

}
