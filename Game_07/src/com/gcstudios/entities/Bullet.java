package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;

public class Bullet extends Entity{
	
	public BufferedImage tiro = Game.spritesheet.getSprite(21, 2, 5, 13);
	
	public Bullet(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}
	
	public void tick() {
		y-=speed;
		if(y < 0) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(tiro, this.getX(), this.getY(), null);
	}

}
