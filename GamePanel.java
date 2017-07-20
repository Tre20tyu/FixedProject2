import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;

	private Thread thread;
	private boolean running;
	private TileMap tileMap;

	private BufferedImage image;
	private Graphics2D g;

	private int FPS = 30;
	private int targetTime = 1000 / FPS;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();

	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		init();
		long startTime;
		long urdTime;
		long waitTime;

		while (running) {
			startTime = System.nanoTime();

			update();
			render();
			draw();

			urdTime = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - urdTime;

			try {
				Thread.sleep(waitTime);
			} catch (Exception e) {
			}
		}
	}

	private void init() {
		running = true;

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		tileMap = new TileMap("tileMap.txt", 42);
	}

	private void update() {
		tileMap.update();
	}

	private void draw() {
		// TODO Auto-generated method stub
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		tileMap.draw(g);
		g.dispose();

	}

	private void render() {
		// TODO Auto-generated method stub

	}

}
