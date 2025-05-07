package com.mygdx.retrofps;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.retrofps.entity.Player;
import com.mygdx.retrofps.manager.EnemyManager;
import com.mygdx.retrofps.manager.WeaponManager;

public class Main extends ApplicationAdapter {
	Map map;
    Player player;
    SpriteBatch batch;
    Renderer renderer;
    WeaponManager weapon;
    EnemyManager enemies;

    private boolean running = true;
    
    @Override
    public void create() {
    	batch = new SpriteBatch();
        map = new Map();
        player = new Player(map);
        renderer = new Renderer(map, player, batch);
        enemies = new EnemyManager(player, batch);
        weapon = new WeaponManager(player);
    }

    @Override
    public void render() {
    	if (!running) return;
    	
        float delta = Gdx.graphics.getDeltaTime();
        
        player.update(delta);
        enemies.update(delta);
        
        batch.begin();
        batch.setColor(Color.WHITE);
        renderer.render(batch);
        enemies.render(batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        weapon.render(batch);
        batch.end();
        
	     if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
	         enemies.shoot();
	         weapon.shoot();
	     }
	     
	     weapon.update(delta);
	     
	     if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
	    	    weapon.reload();
	    	}
    }

    @Override
    public void dispose() {
    	running = false;
    	
        renderer.dispose();
        enemies.dispose();
        weapon.dispose();
        
        batch.dispose();
    }
}
