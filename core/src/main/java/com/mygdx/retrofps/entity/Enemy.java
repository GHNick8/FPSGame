package com.mygdx.retrofps.entity;

public class Enemy {
	public float x, y;
    public boolean alive = true;
    public boolean justDied = false;
    public float deathSoundTimer = 0f;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
