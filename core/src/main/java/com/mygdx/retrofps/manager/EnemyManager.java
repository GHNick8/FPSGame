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

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Texture[] enemyWalkFrames = new Texture[4];
    private Texture[] enemyAttackFrames = new Texture[2];
    private Texture enemyIdleTexture;
    private Texture deadEnemyTexture;

    private Player player;
    private SpriteBatch batch;
    private Sound deathSound;

    private boolean disposed = false;

    public EnemyManager(Player player, SpriteBatch batch) {
        this.player = player;
        this.batch = batch;

        enemyIdleTexture = new Texture("entity/enemy01f.png");
        
        enemyWalkFrames[0] = enemyIdleTexture;
        
        enemyWalkFrames[1] = new Texture("entity/enemy02.png");
        enemyWalkFrames[2] = new Texture("entity/enemy03.png");
        enemyWalkFrames[3] = new Texture("entity/enemy04f.png");

        enemyAttackFrames[0] = new Texture("entity/enemyAttack01f.png");
        enemyAttackFrames[1] = new Texture("entity/enemyAttack02f.png");

        deadEnemyTexture = new Texture("entity/enemyDead.png");
        deathSound = Gdx.audio.newSound(Gdx.files.internal("sound/enemyDead01.wav"));

        enemies.add(new Enemy(7, 5));
        enemies.add(new Enemy(3, 8));
    }

    public void update(float delta) {
        if (disposed) return;

        for (Enemy enemy : enemies) {

            if (enemy.justDied) {
                enemy.deathSoundTimer -= delta;
                if (enemy.deathSoundTimer <= 0) {
                    deathSound.play(1.0f);
                    enemy.justDied = false;
                }
                continue;
            }

            if (!enemy.alive) continue;

            float dx = player.x - enemy.x;
            float dy = player.y - enemy.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance > 0.5f) {
                enemy.attacking = false; 

                float dirX = dx / distance;
                float dirY = dy / distance;

                enemy.x += dirX * enemy.speed * delta;
                enemy.y += dirY * enemy.speed * delta;

                enemy.walkAnimTimer -= delta;
                if (enemy.walkAnimTimer <= 0) {
                    enemy.walkAnimTimer = 0.2f;
                    enemy.currentWalkFrame++;
                    if (enemy.currentWalkFrame >= enemyWalkFrames.length) {
                        enemy.currentWalkFrame = 0;
                    }
                }

            } else {
                enemy.attacking = true;

                enemy.attackAnimTimer -= delta;
                if (enemy.attackAnimTimer <= 0) {
                    enemy.attackAnimTimer = 0.3f;
                    enemy.currentAttackFrame++;
                    if (enemy.currentAttackFrame >= enemyAttackFrames.length) {
                        enemy.currentAttackFrame = 0;
                    }

                    player.takeDamage(5); 
                }
            }
        }
    }

    public void render(SpriteBatch batch, int screenWidth, int screenHeight) {
        if (disposed) return;

        for (Enemy enemy : enemies) {

            float dx = enemy.x - player.x;
            float dy = enemy.y - player.y;

            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            float angleToEnemy = (float) Math.toDegrees(Math.atan2(dy, dx)) - player.angle;

            angleToEnemy = MathUtils.atan2(MathUtils.sinDeg(angleToEnemy), MathUtils.cosDeg(angleToEnemy)) * MathUtils.radiansToDegrees;

            if (angleToEnemy > -30 && angleToEnemy < 30) {

                float scale = 300 / distance;
                scale = Math.max(scale, 20);
                float screenX = screenWidth / 2 + (angleToEnemy / 30f) * (screenWidth / 2) - scale / 2;
                float screenY = (screenHeight / 2) - scale / 2;

                if (!enemy.alive) {
                    batch.setColor(Color.WHITE);
                    batch.draw(deadEnemyTexture, screenX, screenY, scale, scale);
                } else {

                    Texture tex;

                    if (enemy.attacking) {
                        tex = enemyAttackFrames[enemy.currentAttackFrame];
                    } else {
                        tex = enemyWalkFrames[enemy.currentWalkFrame];
                    }

                    batch.setColor(Color.WHITE);
                    batch.draw(tex, screenX, screenY, scale, scale);
                }
            }
        }
    }

    public void shoot() {
        for (Enemy enemy : enemies) {
            if (!enemy.alive) continue;

            float dx = enemy.x - player.x;
            float dy = enemy.y - player.y;

            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            float angleToEnemy = (float) Math.toDegrees(Math.atan2(dy, dx)) - player.angle;
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
        disposed = true;

        enemyIdleTexture.dispose();
        for (Texture t : enemyWalkFrames) {
            if (t != null) t.dispose();
        }
        for (Texture t : enemyAttackFrames) {
            if (t != null) t.dispose();
        }
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
