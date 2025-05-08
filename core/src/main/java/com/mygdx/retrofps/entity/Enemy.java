package com.mygdx.retrofps.entity;

public class Enemy {
	public float x, y;
    public boolean alive = true;
    public boolean justDied = false;
    public float deathSoundTimer = 0f;
    
    public float speed = 1.0f;
    
    public int currentWalkFrame = 0;    
    public float walkAnimTimer = 0.0f;  
    public float walkAnimSpeed = 0.2f;
    
    public boolean attacking = false;
    public float attackAnimTimer = 0f;
    public int currentAttackFrame = 0;
    
    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;
        
        this.walkAnimSpeed = 0.15f + (float)Math.random() * 0.1f; 
    }
}
