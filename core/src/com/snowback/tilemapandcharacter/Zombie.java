package com.snowback.tilemapandcharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by shoong on 2017-05-18.
 */

public class Zombie extends Sprite {

    private Rectangle hitBox;
    private Integer health;

    public Zombie(float x, float y) {
        hitBox = new Rectangle(x, y, 50, 100);
        health = 5;
    }

    public void draw(SpriteBatch spritebatch) {
        spritebatch.draw(AssetManager.getInstance().getZombie(), hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
        spritebatch.draw(AssetManager.getInstance().getHPBar(), hitBox.getX()-18, hitBox.getY()+100, 80*(health/5.f), 10);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void update(float delta, Player player) {
//        hitBox.x += speed * (float)Math.cos(a*Math.PI/180) * delta;
//        hitBox.y += speed * (float)Math.sin(a*Math.PI/180) * delta;
//        time -= delta;

        chasePlayer(player);
        HitPlayer(player);
    }

    private void HitPlayer(Player player) {
        if(hitBox.overlaps(player.getHitBox())){
            player.Hit(1);
        }
    }

    private void chasePlayer(Player player) {
        Vector2 direction = caculateDirection(player);
        hitBox.x += direction.x;
        hitBox.y += direction.y;
    }

    public void hit(int damage){
        health-=damage;
    }
    public Integer getHealth(){
        return health;
    }

    private Vector2 caculateDirection(Player player) {
        float pX;
        float pY;
        float dx;
        float dy;

        pX = player.getX();
        pY = player.getY();
        dx = pX-hitBox.x;
        dy = pY-hitBox.y;

        double length = Math.sqrt(dx*dx+dy*dy);

        Vector2 normalizedDirection = new Vector2(dx/(float)length, (float) (dy/length));

        return normalizedDirection;
    }
}
