
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

	private double maxHeight = 600;

	private boolean isMovingRight = false;
	private boolean isMovingLeft = false;
	private boolean jumping;
	private boolean falling;
	private boolean shoot;
	private boolean canShoot;

	// ground

	Vector b = new Vector(30, 30);
	// gravity vector
	Vector gr = new Vector(5, 5);
	// slowing vector
	Vector s = new Vector(1, 1);
	// speed vector
	Vector v = new Vector(5, 5);
	// positional vector
	Vector p = new Vector(30, 30);

	public Game(int width, int height, int fps) {
		super("JFrame Demo");
		this.MAX_FPS = fps;
		this.WIDTH = width;
		this.HEIGHT = height;

		p = new Vector(WIDTH / 2 - WIDTH / 10, 900);
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
		if (isMovingLeft) {
			p.ix -= v.ix;
		}
		if (isMovingRight) {
			p.ix += v.ix;
		}
		if (p.ix < 0) {
			p.ix = 0;
		}
		if (p.ix < 0) {
			p.ix += s.ix;
			if (p.ix > 0) {
				p.ix = 0;
			}
		}
		if (jumping) {
			falling = false;
			p.iy += .01;
			if (p.iy >= maxHeight) {
				p.iy = (int) maxHeight;
				jumping = false;
				falling = true;
				System.out.println(jumping);
			}
		}
		if (p.iy >= maxHeight) {
			falling = true;
			System.err.println("I am now falling");
			if (falling) {
				jumping = false;
				p.iy += v.iy;
				if (p.iy >= 900) {
					p.iy = 900;
				}
			}
		}
		else
			canShoot = false;
		
		if(canShoot) {
			shoot(b, v, 4);
		}
		else
			shoot = false;

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

		g.setColor(Color.PINK);
		g.drawOval(p.ix, p.iy, WIDTH / 7, HEIGHT / 7);
		if (canShoot) {
			g.setColor(Color.BLACK);
			g.fillOval(b.ix, b.iy, WIDTH / 20, HEIGHT / 20);			
			}

		// release resources, show the buffer
		g.dispose();
		strategy.show();
		addKeyListener(this);
		setFocusable(true);

	}
	private int shoot(Vector x, Vector v, float a) {
		x.setX(x.ix);
		for(;;) {
		x.ix += v.ix * a;
		return (int) v.ix;
		}
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
			isMovingRight = true;
			break;
		case KeyEvent.VK_LEFT:
			isMovingLeft = true;
			break;
		case KeyEvent.VK_UP:
			jumping = true;
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_SPACE: 
			canShoot = true;
			while(b.ix )
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			isMovingRight = false;
			break;
		case KeyEvent.VK_LEFT:
			isMovingLeft = false;
			break;
		case KeyEvent.VK_UP:
			falling = true;
			break;
		case KeyEvent.VK_SPACE:		
			while(b.ix >= 1200 && canShoot) {
			b.setX(WIDTH/7);
			}
			
			
		}
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