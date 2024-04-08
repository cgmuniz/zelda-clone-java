package com.cgmuniz.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.cgmuniz.graficos.Spritesheet;
import com.cgmuniz.main.Game;
import com.cgmuniz.world.Camera;
import com.cgmuniz.world.World;

public class Player extends Entity {
	
	public boolean right, left, up, down;
	public double speed = 1;
	
	private int right_dir = 0, left_dir = 1;
	private int dir = right_dir;
	
	private int frames = 0, maxFrames = 8, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage damageRight;
	private BufferedImage damageLeft;
	
	public int ammo = 0;
	
	public boolean isDamaged = false;
	public int damageFrames = 0;
	
	public double life = 100, maxLife = 100;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		damageRight = Game.spritesheet.getSprite(16 * 2, 32, 16, 16);
		damageLeft = Game.spritesheet.getSprite(16 * 3, 32, 16, 16);
		for(int i = 0; i < 4; i++) {			
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 4; i++) {			
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
	}
	
	public void tick() {
		moved = false;
		
		// double speedSQRT = Math.sqrt(2)/2;
		
		if(right && !left && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			// if(up ^ down) x+= speedSQRT;
			// else x+=speed;
			x+=speed;
		}
		else if(left && !right && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			// if(up ^ down) x-= speedSQRT;
			// else x-=speed;
			x-=speed;
		}
		
		if(up && !down && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			// if(up ^ down) y-= speedSQRT;
			// else y-=speed;
			y-=speed;
		}
		else if (down && !up && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			// if(up ^ down) y+= speedSQRT;
			// else y+=speed;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if(!moved) {
			frames = 0;
			index = 0;
		}
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 10) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(life <= 0) {
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			
			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
			
			Game.entities.add(Game.player);
			
			Game.world = new World("/map.png");
			
			return;
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2) + 8, 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2) + 8, 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					ammo+=10;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColliding(this, atual)) {
					life+=10;
					if(life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {			
			if(dir == right_dir) {			
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			else if(dir == left_dir) {			
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
		else {
			if(dir == right_dir) {			
				g.drawImage(damageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
			else if(dir == left_dir) {			
				g.drawImage(damageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}

}
