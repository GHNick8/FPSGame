package com.mygdx.retrofps.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.retrofps.Map;

public class Player {
	public float x = 5.0f;
	public float y = 5.0f;
	public float angle = 0.0f;
	public float moveSpeed = 7.0f;
	public float rotSpeed = 90.0f;
	public float playerRadius = 0.2f;
	
	private Map map;
	
	public Player(Map map) {
		this.map = map;
	}
	
	public void update(float delta) {
		float moveStep = moveSpeed * delta;
		float rotStep = rotSpeed * delta;
		
		float newX = x;
		float newY = y;
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			newX += Math.cos(Math.toRadians(angle)) * moveStep;
			newY += Math.sin(Math.toRadians(angle)) * moveStep;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			newX -= Math.cos(Math.toRadians(angle)) * moveStep;
			newY -= Math.sin(Math.toRadians(angle)) * moveStep;
        }
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle -= rotStep;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle += rotStep;
        }
        
        if (!isWall(newX + playerRadius, y) && !isWall(newX - playerRadius, y)) {
            x = newX;
        }

        if (!isWall(x, newY + playerRadius) && !isWall(x, newY - playerRadius)) {
            y = newY;
        }
	}
	
	private boolean isWall(float checkX, float checkY) {
        int gridX = (int) checkX;
        int gridY = (int) checkY;

        if (gridX < 0 || gridX >= Map.MAP_WIDTH || gridY < 0 || gridY >= Map.MAP_HEIGHT) {
            return true; 
        }
        return map.get(gridX, gridY) != 0;
    }
}
