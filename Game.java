
//import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends JFrame implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// window vars
	private final int MAX_FPS;
	private final int WIDTH;
	private final int HEIGHT;

	// double buffer
	private BufferStrategy strategy;

	// loop variables
	private boolean isRunning = true;
	private long rest = 0;

	// timing variables
	private float dt;
	private long lastFrame;
	private long startFrame;
	private int fps;

	// sprite1 variables
	private float x = 50;
	private float y = 1;

	// sprite2 variables
	private float x2 = 56.0f;
	private float y2 = 100.0f;

	// ground
	int px = 400;
	int py = 400;

	public Game(int width, int height, int fps) {
		super("JFrame Demo");
		this.MAX_FPS = fps;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	void init() {
		// initialize JFrame
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(0, 0, WIDTH, HEIGHT);

		setIgnoreRepaint(true);

		setResizable(false);
		setVisible(true);

		// create double buffer strategy
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		lastFrame = System.currentTimeMillis();
	}

	private void update() {
		// update current fps
		fps = (int) (1f / dt);

		// update sprite

	}

	private void draw() {
		// get canvas
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

		// clear screen
		g.setColor(Color.blue);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.GRAY);
		g.fillRect(0, 900, WIDTH, HEIGHT);

		// draw fps
		g.setColor(Color.GREEN);
		g.drawString(Long.toString(fps), 10, 40);

		// draw sprite
		g.setColor(Color.white);
		g.fillRect((int) x, HEIGHT / 2 - 25, 100, 100);

		g.setColor(Color.red);
		g.fillRect((int) x2, HEIGHT / 2, 50, 50);

		g.setColor(Color.PINK);
		g.fillOval(px, py, 50, 70);

		// release resources, show the buffer
		g.dispose();
		strategy.show();
		addKeyListener(this);
		setFocusable(true);
	}

	public void run() {
		init();

		while (isRunning) {
			// new loop, clock the start
			startFrame = System.currentTimeMillis();
			// calculate delta time
			dt = (float) (startFrame - lastFrame) / 1000;
			// log the current time
			lastFrame = startFrame;

			// call update and draw methods
			update();
			draw();

			// dynamic thread sleep, only sleep the time we need to cap the framerate
			rest = (1000 / MAX_FPS) - (System.currentTimeMillis() - startFrame);
			if (rest > 0) {
				try {
					Thread.sleep(rest);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		Game game = new Game(1200, 1200, 60);
		game.run();
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		switch (keyEvent.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			//p.setX(p.x + .01f);
			px += 1;
			break;
		case KeyEvent.VK_LEFT:
			//p.setX(p.x - .01f);
			px += 1;
			break;
		case KeyEvent.VK_UP:
			//p.setY(p.y - .01f);
			px += -1;
			break;
		case KeyEvent.VK_DOWN:
			//p.setY(p.y + .01f);
			px += -1;
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
/*
 * BufferedImage makeImage(String path) { try return ImageIO.read({
 * newFile(System.getProperty("user.dir") + path); catch(Exception e) {
 * e.printStackTrace(); return null; } } }) }
 * 
 * Manu.add(Menu_Play);
 * 
 * Menu.setVisible(true); this.ContentPane().add(Menu, BorderLayout.SOUTH);
 * 
 * Score = JPanel(new BorderLayout()));
 * 
 * JPanel Score_Top = new JPanel(new GridLayout(1,2));
 * 
 * Score_TOP.setPreferredSize(new Dimension(WIDTH,200)
 * 
 **/