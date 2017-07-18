
//import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

	private double maxHeight = 500;

	private boolean isMovingRight = false;
	private boolean isMovingLeft = false;
	private boolean jumping;
	private boolean falling;
	private boolean inAir;
	private boolean canShoot;

	public enum GAME_STATE {
		MENU, PLAY, EXIT
	}

	GAME_STATE gameState = GAME_STATE.MENU;

	private JPanel MENU;
	private JButton MENU_PLAY;
	private JButton MENU_EXIT;

	// ground

	// gravity vector
	Vector gr = new Vector(2.5f, 12);
	// speed vector
	// Drag
	Vector s = new Vector(1, 1);
	// Speed
	Vector v = new Vector(8, 15);
	// positional vectors
	// Player
	Vector b = new Vector(30, 30);
	Vector p = new Vector(30, 30);

	public Game(int width, int height, int fps) {
		super("Shooty Gun");
		this.MAX_FPS = fps;
		this.WIDTH = width;
		this.HEIGHT = height;

		p = new Vector(WIDTH / 2 - WIDTH / 10, 900);
		b = new Vector(WIDTH / 2 - WIDTH / 10, 950);
	}

	void init() {
		// initialize JFrame
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(0, 0, WIDTH, HEIGHT);

		setIgnoreRepaint(true);

		setResizable(false);
		setVisible(true);

		MENU = new JPanel();

		this.getContentPane().setLayout(new BorderLayout());

		MENU_PLAY = new JButton("Play!");
		MENU_EXIT = new JButton("Exit!");

		MENU.setLayout(new GridLayout(2, 1));

		MENU_PLAY.setSize(300, 300);
		MENU_EXIT.setSize(300, 300);

		MENU_PLAY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MENU.setVisible(false);
				gameState = GAME_STATE.PLAY;
			}
		});
		MENU_EXIT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameState = GAME_STATE.EXIT;
				;
			}
		});
	

		MENU.add(MENU_PLAY);
		MENU.add(MENU_EXIT);

		MENU.setVisible(true);
		this.getContentPane().add(MENU, BorderLayout.CENTER);
		this.pack();
	
		// create double buffer strategy
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		lastFrame = System.currentTimeMillis();
	}

	private void update() {
		// update current fps
		switch (gameState) {
		case MENU:
			break;
		case PLAY:
			fps = (int) (1f / dt);

			// update sprite
			if (p.iy > HEIGHT || p.iy <= maxHeight) {
				inAir = true;
			}
			if (p.iy > HEIGHT && !falling) {
				jumping = true;
			} else if (inAir)
				falling = true;
			if (jumping) {
				falling = false;
				p.iy -= v.iy;
				if (p.iy <= maxHeight) {
					p.iy = (int) maxHeight;
				}
			}
			if (p.iy <= maxHeight) {
				falling = true;
			}
			if (falling) {
				jumping = false;
				p.iy += gr.iy;
				if (p.iy >= 900) {
					p.iy = 900;
				}
			}
			if (isMovingLeft) {
				p.ix -= v.ix;
				if (inAir) {
					p.ix += gr.ix;
				}
			}
			if (isMovingRight) {
				p.ix += v.ix;
				if (inAir) {
					p.ix -= gr.ix;
				}
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
			if (canShoot) {
				shoot(b, v, 4);
			} else
				canShoot = false;
			break;
		case EXIT:
			break;
	//	default:
	//		break;
		}
	}

	private void draw() {
		// get canvas
		switch (gameState) {
		case MENU:
			break;
		case PLAY:
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

			// clear screen
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.GRAY);
			g.fillRect(0, 900, WIDTH, HEIGHT);

			// draw fps
			g.setColor(Color.GREEN);
			g.drawString(Long.toString(fps), 10, 40);

			g.setColor(Color.PINK);
			g.fillOval(p.ix, p.iy, WIDTH / 7, HEIGHT / 7);
			if (canShoot) {
				g.setColor(Color.BLACK);
				g.fillOval(b.ix, b.iy, WIDTH / 20, HEIGHT / 20);
			}

			// release resources, show the buffer
			g.dispose();

			
			addKeyListener(this);
			setFocusable(true);
			setVisible(true);
			strategy.show();
			break;
		case EXIT:
			System.exit(0);
		}

	}

	private int shoot(Vector x, Vector v, float a) {
		x.setX(x.ix);
		for (;;) {
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
			if (p.iy <= maxHeight) {
				jumping = false;
			}
			break;
		case KeyEvent.VK_SPACE:
			canShoot = true;
			while (b.ix >= 1200 && canShoot) {
				b.setX(p.ix);
				b.setY(p.iy);                           
			}
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
			while (b.ix >= 1200 && canShoot) {
				b.setX(p.ix);
				b.setX(p.iy);
			}

		}
	}
}

/*
 * if (jumping) { falling = false; p.iy += .01; if (p.iy >= maxHeight) { p.iy =
 * (int) maxHeight; jumping = false; falling = true;
 * System.out.println(jumping); } } if (p.iy >= maxHeight) { falling = true;
 * System.err.println("I am now falling"); if (falling) { jumping = false; p.iy
 * += v.iy; if (p.iy >= 900) { p.iy = 900; } }
 **/