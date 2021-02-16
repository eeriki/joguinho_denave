package com.gcstudios.entities;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;

public class Enemy extends Entity{
	
	public static BufferedImage meteor[] = {Game.spritesheet.getSprite(0, 32, 16, 16), Game.spritesheet.getSprite(16, 32, 16, 16), Game.spritesheet.getSprite(32, 32, 16, 16)};
	public static int i;
	
	public int vida = 2;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}
	
	public void tick() {
		y += speed;
		if(this.getY() > Game.HEIGHT) {
			Game.entities.remove(this);
			return;
		}
		
		if(vida <= 0) {
			destroySelf();
		}
		
		//
		collidingPlayer();
		collidingBullet();
	}
	
	public void collidingPlayer() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Player) {
				if(isColidding(this, e)) {
					Player.vida--;
					Game.entities.remove(this);
					return;
				}
			}
		}
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Bullet) {
				if(isColidding(this, e)) {
					vida--;
					Game.entities.remove(e);
					return;
				}
			}
		}
	}
	
	public void destroySelf() {
		Game.entities.remove(this);
		Player.score += 20;
		return;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX(), this.getY(), null);
	}


}
