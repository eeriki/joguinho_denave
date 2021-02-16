package com.gcstudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.gcstudios.entities.EnemySpawn;
import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.UI;
import com.gcstudios.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 120;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	private BufferedImage image;
	public BufferedImage background;
	
	public static World world;
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	
	public EnemySpawn enemys;
	public static String gamesta = "menu";
	public static boolean showMessageGameOver;
	public int framesGameOver;
	
	public UI ui;
	
	//game-over//
	public boolean restart;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		try {
			background = ImageIO.read(getClass().getResource("/bacckground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		entities = new ArrayList<Entity>();
		player = new Player(Game.WIDTH/2 - 7,HEIGHT - 24,16,16,1,null);
		world = new World();
		ui = new UI();
		enemys = new EnemySpawn();
		
		entities.add(player);
		
	}
	
	public void initFrame(){
		frame = new JFrame("Space Invaders");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		if(gamesta.equals("normal")) {
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			enemys.tick();
			ui.tick();
		}
		
		else if(gamesta.equals("gameover")) {
			framesGameOver++;
			if(framesGameOver == 30) {
				framesGameOver = 0;
				if(showMessageGameOver)
					showMessageGameOver = false;
				else
					showMessageGameOver = true;
			}
			
			
			
			if(restart) {
				restart = false;
				gamesta = "normal";
				entities.removeAll(entities);
				Player.vida = 3;
				Player.score = 0;
				player.setX(Game.WIDTH/2 - 7);
				
				entities.add(player);
			}
		} else if(gamesta.equals("menu")) {
			restart = false;
			framesGameOver++;
			if(framesGameOver == 30) {
				framesGameOver = 0;
				if(showMessageGameOver)
					showMessageGameOver = false;
				else
					showMessageGameOver = true;
			}
		}
		
	}
	


	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		g.drawImage(background, 0, 0, null);
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		ui.render(g);
		
		if(gamesta.equals("gameover")) {
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(0, 0,WIDTH,HEIGHT);
			g.setFont(new Font("arial", Font.BOLD, 10));
			g.setColor(Color.white);
			g.drawString("Game-Over", 32, 60);
			if(showMessageGameOver) {
				g.drawString("<press enter>", 28, 100);
			}
		} else if(gamesta.equals("menu")) {
			g.setFont(new Font("arial", Font.BOLD, 10));
			g.setColor(Color.white);
			if(showMessageGameOver) {
				g.drawString("<press enter>", 28, 80);
			}
		}
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(gamesta.equals("normal")) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.right = true;
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
			}
			
			
			if(e.getKeyCode() == KeyEvent.VK_X) {
				player.shoot = true;
			}
		}
		else if(gamesta.equals("gameover")){
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				restart = true;
				gamesta = "normal";
			}
		}else if(gamesta.equals("menu")){
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				restart = false;
				gamesta = "normal";
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	
	}

	
}
