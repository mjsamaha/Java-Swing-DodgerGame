package com.mjsamaha.dodger.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.mjsamaha.dodger.core.GameStateManager;
import com.mjsamaha.dodger.entities.Player;

public class InputHandler implements KeyListener {

	private boolean moveLeft, moveRight, moveUp, moveDown;
    private GameStateManager gameStateManager;
    
    public InputHandler(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
    }
    
    public void handlePlayerMovement(Player player, float dt) {
        if (moveLeft) player.moveLeft(dt);
        if (moveRight) player.moveRight(dt);
        if (moveUp) player.moveUp(dt);
        if (moveDown) player.moveDown(dt);
    }
    
    public void reset() {
        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (!gameStateManager.isGameOver()) {
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
                gameStateManager.requestRestart();
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
}
