package com.mygdx.retrofps.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.retrofps.entity.Enemy;
import com.mygdx.retrofps.entity.Player;

public class EnemyManager {
	private ArrayList <Enemy> enemies = new ArrayList<>();
	private Texture enemyTexture;
	private Texture deadEnemyTexture;
	private Player player;
	private SpriteBatch batch;
	private Sound deathSound;
	
	public EnemyManager(Player player, SpriteBatch batch) {
		this.player = player;
		this.setBatch(batch);
		
		enemyTexture = new Texture("enemy.png");
		deadEnemyTexture = new Texture("enemyDead.png");
		
		deathSound = Gdx.audio.newSound(Gdx.files.internal("sound/enemyDead01.wav"));
		
		enemies.add(new Enemy(7, 5));
		enemies.add(new Enemy(3, 8));
	}
	
	public void update(float delta) {
		for (Enemy enemy : enemies) {
	        if (enemy.justDied) {
	            enemy.deathSoundTimer -= delta;

	            if (enemy.deathSoundTimer <= 0) {
	                deathSound.play(1.0f);
	                enemy.justDied = false; 
	            }
	        }
	    } 
	}
	
	public void render(SpriteBatch batch, int screenWidth, int screenHeight) {
		for (Enemy enemy : enemies) {

	        float dx = enemy.x - player.x;
	        float dy = enemy.y - player.y;

	        float distance = (float) Math.sqrt(dx * dx + dy * dy);
	        float angleToEnemy = (float)Math.toDegrees(Math.atan2(dy, dx)) - player.angle;
	        angleToEnemy = MathUtils.atan2(MathUtils.sinDeg(angleToEnemy), MathUtils.cosDeg(angleToEnemy)) * MathUtils.radiansToDegrees;

	        if (angleToEnemy > -30 && angleToEnemy < 30) {

	            float scale = 300 / distance;
	            float screenX = screenWidth / 2 + (angleToEnemy / 30f) * (screenWidth / 2) - scale / 2;
	            float screenY = (screenHeight / 2) - scale / 2;

	            if (enemy.alive) {
	                batch.draw(enemyTexture, screenX, screenY, scale, scale);
	            } else {
	                batch.setColor(Color.DARK_GRAY); 
	                batch.setColor(Color.WHITE);
	                batch.draw(deadEnemyTexture, screenX, screenY, scale, scale);
	            }
	        }
	    }
    }
	
	public void shoot() {
        for (Enemy enemy : enemies) {
            if (!enemy.alive) continue;

            float dx = enemy.x - player.x;
            float dy = enemy.y - player.y;

            float distance = (float)Math.sqrt(dx * dx + dy * dy);
            float angleToEnemy = (float)Math.toDegrees(Math.atan2(dy, dx)) - player.angle;
            angleToEnemy = MathUtils.atan2(MathUtils.sinDeg(angleToEnemy), MathUtils.cosDeg(angleToEnemy)) * MathUtils.radiansToDegrees;

            if (angleToEnemy > -5 && angleToEnemy < 5 && distance < 5) {
                enemy.alive = false; 
                enemy.justDied = true;
                enemy.deathSoundTimer = 0.2f;
                System.out.println("Enemy shot!");
                break;
            }
        }
    }

    public void dispose() {
        enemyTexture.dispose();
        deadEnemyTexture.dispose();
        deathSound.dispose();
    }

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}
}
