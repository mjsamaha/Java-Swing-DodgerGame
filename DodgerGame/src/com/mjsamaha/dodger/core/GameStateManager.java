package com.mjsamaha.dodger.core;

public class GameStateManager {
	
	private boolean gameOver;
	
	private int score;
	
	private boolean restartRequested;
	
	public GameStateManager() {
		this.gameOver = false;
		this.score = 0;
		this.restartRequested = false;
	}
	
	public void incrementScore() {
		score++;
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void requestRestart() {
		restartRequested = true;
	}
	
	public boolean isRestartRequested() {
		return restartRequested;
	}
	
	public void reset() {
		gameOver = false;
		score = 0;
		restartRequested = false;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public int getScore() {
		return score;
	}
 
}
