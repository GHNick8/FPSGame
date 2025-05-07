package com.mygdx.retrofps.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.retrofps.entity.Player;

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
	private int maxAmmo = 50;
	private boolean reloading = false;
	private float reloadTimer = 0f;
	private final float RELOAD_TIME = 1.5f;
	
	private Texture weaponReload1;
	private Texture weaponReload2;
	private Sound reloadSound;
	
	private float reloadAnimTimer = 0f;
	private float reloadAnimSwitchTime = 0.3f; 
	private boolean reloadFrame = false;
	
	/*
	// Hard coded pick-ups 
	private Player player;
	private float pickupX = 10;
	private float pickupY = 10;
	private boolean pickupActive = true;
	*/
	
	private boolean disposed = false;
	
	public WeaponManager(Player player) {
		// this.player = player;
		
		weaponIdle = new Texture("shooterHUB03.png");
		weaponShoot = new Texture("shooterHUB04.png");
		shootSound = Gdx.audio.newSound(Gdx.files.internal("sound/shotgun.wav"));
		emptySound = Gdx.audio.newSound(Gdx.files.internal("sound/empty.wav"));
		
		weaponReload1 = new Texture("reload01.png");
		weaponReload2 = new Texture("reload03.png");
		reloadSound = Gdx.audio.newSound(Gdx.files.internal("sound/reload.wav"));
		
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
        
        if (reloading) {
            reloadTimer -= delta;
            reloadAnimTimer -= delta;
            if (reloadAnimTimer <= 0) {
                reloadAnimTimer = reloadAnimSwitchTime;
                reloadFrame = !reloadFrame; 
            }

            if (reloadTimer <= 0) {
                reloading = false;
                ammo = maxAmmo;
                System.out.println("Reload complete.");
            }
        }
        
        /*
        if (pickupActive) {
            float dx = player.x - pickupX;
            float dy = player.y - pickupY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < 1.0f) {
                ammo = Math.min(ammo + 10, maxAmmo);
                pickupActive = false;
                System.out.println("Picked up ammo!");
            }
        }
        */
    }

    public void render(SpriteBatch batch) {
    	if (disposed) return;
    	
        int screenWidth = Gdx.graphics.getWidth();
        Gdx.graphics.getHeight();

        Texture weaponToDraw;
        
        if (reloading) {
            weaponToDraw = reloadFrame ? weaponReload1 : weaponReload2;
        } else if (isShooting) {
            weaponToDraw = weaponShoot;
        } else {
            weaponToDraw = weaponIdle;
        }

        int weaponWidth = weaponToDraw.getWidth();
        weaponToDraw.getHeight();

        batch.draw(weaponToDraw, screenWidth / 2 - weaponWidth / 2, 0);
        
        String ammoText = "AMMO: " + ammo;
        font.draw(batch, ammoText, screenWidth - 250, 100);  
        
        /*
        // Draw pick-ups (not for later use)
        if (pickupActive) {
            batch.setColor(Color.YELLOW);
            batch.draw(weaponIdle, (int)(pickupX * 32), (int)(pickupY * 32), 32, 32);
            batch.setColor(Color.WHITE);
        }
        */
    }

    public void shoot() {
    	if (reloading) return;
    	
    	if (ammo <= 0) {
            emptySound.play(1.0f);
            return; 
        }
    	
        isShooting = true;
        shootTimer = SHOOT_DURATION;
        shootSound.play(0.8f);
        
        ammo--;
    }
    
    public void reload() {
    	if (ammo == maxAmmo) return;
    	if (reloading) return;
    	
    	reloading = true;
    	reloadTimer = RELOAD_TIME;
    	reloadSound.play(1.0f);
    	
    	System.out.println("Reloading");
    }

    public void dispose() {
    	disposed = true;
    	
        weaponIdle.dispose();
        weaponShoot.dispose();
        
        weaponReload1.dispose();
        weaponReload2.dispose();
        
        shootSound.dispose();
        reloadSound.dispose();
        emptySound.dispose();
        
        font.dispose();
    }
}
