package com.cgmuniz.graficos;

import java.awt.Color;
import java.awt.Graphics;

import com.cgmuniz.main.Game;

public class UI {

	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 6, 60, 10);
		g.setColor(new Color(0, 184, 22));
		g.fillRect(8, 6, (int)((Game.player.life/Game.player.maxLife) * 60), 10);
		g.setColor(Color.white);
	}
}
