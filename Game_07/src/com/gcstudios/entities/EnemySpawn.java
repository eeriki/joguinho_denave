package com.gcstudios.entities;

import com.gcstudios.main.Game;

public class EnemySpawn {
	
	public int targetTime = 60;
	public int curTime = 0;
	
	public void tick() {
		curTime++;
		if(curTime == targetTime) {
			Enemy.i = Entity.rand.nextInt(3);
			curTime = 0;
			int yy = 0;
			int xx = Entity.rand.nextInt(Game.WIDTH-16);
			Enemy enemy = new Enemy(xx,yy, 16,16,0.5,Enemy.meteor[Enemy.i]);
			Game.entities.add(enemy);
			if(enemy.getY() > Game.HEIGHT) {
				Game.entities.remove(enemy);
			}
		}
		if(Player.score >= 700) {
			targetTime = 30;
			
		}
		
		
	}
			
}
