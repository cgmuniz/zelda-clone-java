package com.cgmuniz.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.cgmuniz.main.Game;
import com.cgmuniz.world.Camera;
import com.cgmuniz.world.World;

public class Enemy extends Entity {
	
	private double speed = 0.3;
	
	private int maskx = 4, masky = 4, maskw = 10, maskh = 10;
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(7 * 16, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(8 * 16, 16, 16, 16);
	}
	
	public void tick() {	
		//maskx = 8;
		//masky = 8;
		//maskw = 5;
		//maskh = 5;
		
		if(!isCollidingWithPlayer()) {			
			if((int) x < Game.player.getX()
					&& World.isFree((int) (x + speed), this.getY())
					&& !isColliding((int)(x+speed), this.getY())) {
				x += speed;
			}
			else if((int) x > Game.player.getX()
					&& World.isFree((int) (x - speed), this.getY())
					&& !isColliding((int) (x - speed), this.getY())) {
				x -= speed;
			}
			
			if((int) y < Game.player.getY()
					&& World.isFree(this.getX(), (int) (y + speed))
					&& !isColliding(this.getX(), (int) (y + speed))) {
				y += speed;
			}
			else if((int) y > Game.player.getY()
					&& World.isFree(this.getX(), (int) (y - speed))
					&& !isColliding(this.getX(), (int) (y - speed))) {
				y -= speed;
			}
		}
		else {
			// Colidindo
			if(Game.rand.nextInt(100) < 10) {				
				Game.player.life -= Game.rand.nextInt(3);
				
				if(Game.player.life <= 0) {
					// Game over!
					
				}
				
				System.out.println("Vida" + Game.player.life);
			}
			
		}
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
