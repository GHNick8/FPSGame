package com.mygdx.retrofps.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WeaponManager {
	private Texture weaponIdle;
	private Texture weaponShoot;
	private Sound shootSound;
	private Sound emptySound;
	
	private boolean isShooting = false;
	private float shootTimer = 0f;
	private final float SHOOT_DURATION = 0.1f;
	
	private BitmapFont font;
	private int ammo = 50;
	
	public WeaponManager() {
		weaponIdle = new Texture("shooterHUB03.png");
		weaponShoot = new Texture("shooterHUB04.png");
		shootSound = Gdx.audio.newSound(Gdx.files.internal("sound/shotgun.wav"));
		emptySound = Gdx.audio.newSound(Gdx.files.internal("sound/empty.wav"));
		
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(3); 
	}
	
	public void update(float delta) {
        if (isShooting) {
            shootTimer -= delta;
            if (shootTimer <= 0) {
                isShooting = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        int screenWidth = Gdx.graphics.getWidth();
        Gdx.graphics.getHeight();

        Texture weaponToDraw = isShooting ? weaponShoot : weaponIdle;

        int weaponWidth = weaponToDraw.getWidth();
        weaponToDraw.getHeight();

        batch.draw(weaponToDraw, screenWidth / 2 - weaponWidth / 2, 0);
        
        String ammoText = "AMMO: " + ammo;
        font.draw(batch, ammoText, screenWidth - 250, 100);  
    }

    public void shoot() {
    	if (ammo <= 0) {
            emptySound.play(1.0f);
            return; 
        }
    	
        isShooting = true;
        shootTimer = SHOOT_DURATION;
        shootSound.play(0.8f);
        
        ammo--;
    }

    public void dispose() {
        weaponIdle.dispose();
        weaponShoot.dispose();
        shootSound.dispose();
        emptySound.dispose();
        font.dispose();
    }
}
