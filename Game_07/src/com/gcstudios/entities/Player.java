package com.gcstudios.entities;



import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;




public class Player extends Entity{
	
	public static int vida = 3;
	public static int score = 0;
	
	public boolean right, left, shoot;

	public static BufferedImage nave = Game.spritesheet.getSprite(0, 16, 16, 16);
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
	}
	
	public void tick(){
		if(right) {
			x+=speed;
		}else if(left){
			x-=speed;
		}
		
		if(x > Game.WIDTH) {
			x = -16;
		}else if(x+16 < 0) {
			x = Game.WIDTH;
		}
		
		if(shoot) {
			shoot = false;
			double xx = this.getX() + 5.5;
			double yy = this.getY() - 10;
			
			Bullet bullet = new Bullet(xx, yy, 16, 16, 2, null);
			Game.entities.add(bullet);
			
		}
		
		if(vida <= 0) {
			Game.gamesta = "gameover";
		}
	}
	
	
	
	public void render(Graphics g) {
		g.drawImage(nave, this.getX(), this.getY(), null);
	}

	

	


}
