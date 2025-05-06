package com.mygdx.retrofps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.retrofps.entity.Player;

public class Renderer {
	private SpriteBatch batch;
    private Texture wallTexture;
    private Pixmap wallPixmap;
    private Map map;
    private Player player;

    public Renderer(Map map, Player player, SpriteBatch batch) {
        this.map = map;
        this.player = player;
        this.batch = batch;
        
        wallTexture = new Texture(Gdx.files.internal("wall2.png"));
        wallPixmap = new Pixmap(Gdx.files.internal("wall2.png"));
    }

    public void render(SpriteBatch batch) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        
        Color ceilingColor = new Color(9f, 0.1f, 0.1f, 1);
        Color floorColor = new Color(9f, 0.1f, 0.1f, 1);

        for (int x = 0; x < screenWidth; x++) {
            float rayAngle = (player.angle - 30) + (x / (float) screenWidth) * 60;

            float rayX = player.x;
            float rayY = player.y;

            float rayDirX = (float) Math.cos(Math.toRadians(rayAngle));
            float rayDirY = (float) Math.sin(Math.toRadians(rayAngle));

            float distanceToWall = 0;
            boolean hitWall = false;

            while (!hitWall && distanceToWall < 16) {
                distanceToWall += 0.05f;

                int testX = (int) (rayX + rayDirX * distanceToWall);
                int testY = (int) (rayY + rayDirY * distanceToWall);

                if (testX < 0 || testX >= Map.MAP_WIDTH || testY < 0 || testY >= Map.MAP_HEIGHT) {
                    hitWall = true;
                    distanceToWall = 16;
                } else if (map.get(testX, testY) > 0) {
                    hitWall = true;
                }
            }

            float hitX = rayX + rayDirX * distanceToWall;
            float hitY = rayY + rayDirY * distanceToWall;

            float wallX;
            
            if ((int)hitX == (int)(hitX + rayDirX * 0.01f)) {
                wallX = hitY - (float)Math.floor(hitY);
            } else {
                wallX = hitX - (float)Math.floor(hitX);
            }

            int texX = (int)(wallX * wallPixmap.getWidth());
            
            if (texX < 0) texX = 0;
            if (texX >= wallPixmap.getWidth()) texX = wallPixmap.getWidth() - 1;

            int ceiling = (int)((screenHeight / 2.0) - screenHeight / distanceToWall);
            int floor = screenHeight - ceiling;
            
            for (int y = 0; y < ceiling; y++) {
            	batch.setColor(ceilingColor);
            	batch.draw(wallTexture, x, y, 1, 1, 0, 0, 1, 1, false, false);
            	batch.setColor(Color.WHITE);
            }

            for (int y = ceiling; y < floor; y++) {
                float distanceFromTop = y - ceiling;
                float texYRatio = distanceFromTop / (floor - ceiling);
                int texY = (int)(texYRatio * wallPixmap.getHeight());

                int pixel = wallPixmap.getPixel(texX, texY);
                Color color = new Color(pixel);
                
                float fogFactor = Math.min(distanceToWall / 8.0f, 1.0f);
                color.lerp(Color.GRAY, fogFactor);

                batch.setColor(color);
                batch.draw(wallTexture, x, y, 1, 1, texX, texY, 1, 1, false, false);
                batch.setColor(Color.WHITE);
            }
            
            for (int y = floor; y < screenHeight; y++) {
            	batch.setColor(floorColor);
            	batch.draw(wallTexture, x, y, 1, 1, 0, 0, 1, 1, false, false);
            	batch.setColor(Color.WHITE);
            }
        }
    }

    public void dispose() {
        batch.dispose();
        wallTexture.dispose();
        wallPixmap.dispose();
    }
    
    public SpriteBatch getBatch() {
        return batch;
    }

}
