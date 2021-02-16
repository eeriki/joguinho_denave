package com.gcstudios.graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.entities.Player;
import com.gcstudios.main.Game;

public class UI {
	
	public BufferedImage vida = Game.spritesheet.getSprite(16, 16, 16, 16);
	
	public void tick() {
		
	}

	public void render(Graphics g) {
		for(int i = 0; i < Player.vida; i++) {
			g.drawImage(vida, 70+(i*16), 5, null);
		}
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.setColor(Color.white);
		g.drawString("Score: " + Player.score, 5, 15);
		
	}
	
}
