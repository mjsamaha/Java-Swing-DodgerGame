package com.mjsamaha.dodger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
	
	public Timer timer;
	public int frameCount;
	
	// dt time tracking
	private long lastUpdateTime;
	private boolean moveLeft, moveRight, moveUp, moveDown;
	
	public boolean gameOver;
	
	// objects
	public Player player;
	
	private ArrayList<Objects> fallingObjects;
	private Random random;
	
	// obj spawning
	private float spawnTimer;
	private float spawnInterval;
	
	private int score;
	
	public GamePanel() {
		setPreferredSize(new Dimension(Constants.Window.WINDOW_WIDTH, Constants.Window.WINDOW_HEIGHT));
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		
		frameCount = 0;
		gameOver = false;
		
		timer = new Timer(1000 / 60, this);
		
		player = new Player(Constants.Player.START_X, Constants.Player.START_Y, Constants.Player.PLAYER_WIDTH, Constants.Player.PLAYER_HEIGHT, Constants.Player.PLAYER_SPEED, Constants.Player.PLAYER_COLOR);
		
		fallingObjects = new ArrayList<>();
		random = new Random();
		
		spawnTimer = 0;
		spawnInterval = 1.0f; // spawn every 1 second
		
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
		
		score = 0;
	}
	
	public void startGame() {
		lastUpdateTime = System.nanoTime();
		timer.start();
		requestFocusInWindow();
	}
	
	private void updateGame(float dt) {
		frameCount++;
		
		// handle player movement based on flags
		if (moveLeft) {
			player.moveLeft(dt);
		}
		if (moveRight) {
			player.moveRight(dt);
		}
		if (moveUp) {
			player.moveUp(dt);
		}
		if (moveDown) {
			player.moveDown(dt);
		}
		
		player.keepWithinBounds(getWidth(), getHeight());
		
		// update spawn timer and spawn objects
		spawnTimer += dt;
		if (spawnTimer >= spawnInterval) {
			spawnObjects();
			spawnTimer = 0;
		}
		
		// update falling objects
		for (Objects obj : fallingObjects) {
			obj.update(dt);
		}
		
		// remove off-screen objects
		Iterator<Objects> iterator = fallingObjects.iterator();
		while (iterator.hasNext()) {
			Objects obj = iterator.next();
			if (obj.isOffScreen(getHeight())) {
				iterator.remove();
				score++; // increase score for each object that falls off screen
			}
		}
		
		// check collisions
		checkCollisions();
	}
	
	private void spawnObjects() {
		int maxX = getWidth() - Constants.Objects.OBJECT_WIDTH;
		float randomX = random.nextInt(Math.max(1, maxX));
		
		float startY = -Constants.Objects.OBJECT_HEIGHT;
		
		Objects newObj = new Objects(
				randomX, startY,
				Constants.Objects.OBJECT_WIDTH, Constants.Objects.OBJECT_HEIGHT,
				Constants.Objects.OBJECT_SPEED,
				Constants.Objects.OBJECT_COLOR
				);
		fallingObjects.add(newObj);
	}
	
	private void checkCollisions() {
		for (Objects obj : fallingObjects) {
			if (player.getBounds().intersects(obj.getBounds())) {
				gameOver = true;
				break;
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (!gameOver) {
			// draw falling objects
			for (Objects obj : fallingObjects) {
				obj.draw(g2d);
			}
			
			// Draw game elements here
			player.draw(g2d);
			
			// render score
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Arial", Font.BOLD, 24));
			g2d.drawString("Score: " + score, 10, 30);
		}
		
		// draw game over message
		if (gameOver) {
	        g2d.setColor(Color.WHITE);
	        g2d.setFont(new Font("Arial", Font.BOLD, 32));
	        g2d.drawString("GAME OVER!", getWidth() / 2 - 100, getHeight() / 2 - 20);
	        
	        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
	        g2d.drawString("Final Score: " + score, getWidth() / 2 - 70, getHeight() / 2 + 20);
	        g2d.drawString("Press R to restart", getWidth() / 2 - 80, getHeight() / 2 + 50);
	    }
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// handle player movement based on key presses
		int key = e.getKeyCode();
		// arrow keys and AWSD for movement, R to restart if game over
		if (!gameOver) {
			if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
				moveLeft = true;
			}
			if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
				moveRight = true;
			}
			if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
				moveUp = true;
			}
			if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
				moveDown = true;
			}
		} else {
			if (key == KeyEvent.VK_R) {
				restartGame();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			moveLeft = false;
		}
		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			moveRight = false;
		}
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			moveUp = false;
		}
		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			moveDown = false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameOver) {
			// calc dt
			long currentTime = System.nanoTime();
			float dt = (currentTime - lastUpdateTime) / 1_000_000_000.0f;
			lastUpdateTime = currentTime;
			
			updateGame(dt);
		}
		repaint();
	}
	
	private void restartGame() {
		frameCount = 0;
		gameOver = false;
		lastUpdateTime = System.nanoTime();
		
		// reset player position
		player.setX(getWidth() / 2 - player.getWidth() / 2);
		player.setY(getHeight() - 100);
		
		// clear all falling objects
		fallingObjects.clear();
		
		// reset spawn timer
		spawnTimer = 0;
		spawnInterval = 1.0f;
		
		score = 0;
		
		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;
	}
}
