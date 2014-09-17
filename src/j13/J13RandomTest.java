package j13;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

public class J13RandomTest extends Canvas implements Runnable{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		J13RandomTest game = new J13RandomTest();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		JFrame frame = new JFrame("RANDOM TESTS");
		frame.add(game,BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();
		new Thread(game).run();
	}
	
	public static final int HEIGHT = 512;
	public static final int WIDTH = 512;
	public static final int SCALE = 1;
	
	private boolean running;
	private BufferedImage buffer;
	private int[] pixels;

	
	public J13RandomTest() {
		buffer = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
	}
	
	public void start() {
		running = true;
		new Thread(this).run();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double idealTime = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastFPS = System.currentTimeMillis();
		
		init();
		
		while(running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / idealTime;
			lastTime = now;
			
			while(unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
			}
			
			try {
				Thread.sleep(2);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			frames++;
			render();
			
			if(System.currentTimeMillis() - lastFPS > 1000) {
				lastFPS += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private void tick() {
	}

	private void init() {
		RandomCA gen = new RandomCA();
		//Random gen = new Random();
		gen.seedCA(1<<16);
		int b = 0;
		int w = 0;
		for(int y=0;y<512;y++)
			for(int x=0;x<512;x++)
				if(gen.nextInt(2) == 0)
					w++;
				else
					b++;
		
		System.out.println((double)b/(double)(512*512));
		
		for(int y=0;y<512;y++)
			for(int x=0;x<512;x++)
				if(b-- > 0)
					pixels[x+y*512] = 0;
				else
					pixels[x+y*512] = 0xffffff;
		
	}

	private void render() {
		BufferStrategy strategy = getBufferStrategy();
		if (strategy == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		
		Graphics g = buffer.getGraphics();
		
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, WIDTH, HEIGHT);
//		
		// draw on buffer
		g.dispose();
		
		// render on screen
		g = strategy.getDrawGraphics();
		
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, getHeight(), getWidth());
		
		g.drawImage(buffer, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		
		strategy.show();
	}
}
