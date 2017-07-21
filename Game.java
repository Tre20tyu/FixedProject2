//import javax.imageio.ImageIO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

	int i = 0;
	private static final long serialVersionUID = 1L;
	// window vars
	private final int MAX_FPS;
	private final int WIDTH;
	private final int HEIGHT;

	// double buffer
	private BufferStrategy strategy;
	// private TileMap tileMap;

	// loop variables
	private boolean isRunning = true;
	private long rest = 0;

	// timing variables
	private float dt;
	private long lastFrame;
	private long startFrame;
	private int fps;

	private double maxSpeed = 8;
	private double maxHeight;
	private double maxFalling;

	private boolean isMovingRight = false;
	private boolean isMovingLeft = false;
	private boolean jumping;
	private boolean falling;
	private boolean canShoot;
	private long Score;

	float Timer = 0.0f;
	// Boss variables
	private double hrate;
	private double maxBossHealth;
	private double hBarLength;
	private double cBossHealth;
	private double bossDmg;

	public enum GAME_STATE {
		MENU, PLAY, WIN, EXIT
	}

	GAME_STATE gameState = GAME_STATE.MENU;

	private JPanel MENU;
	private JButton MENU_PLAY;
	private JButton MENU_EXIT;

	// ground

	// gravity vector
	Vector gr = new Vector(0.64f, 12);
	// speed vector
	// Drag
	Vector s = new Vector((float) 0.52, 1);
	// Speed
	Vector v = new Vector(8, 15);
	// positional vectors
	// Player
	Vector b = new Vector(30, 30);
	Vector fb = new Vector(100, 100);
	Vector fbv = new Vector(10, 10);
	Vector p = new Vector(30, 30);

	public Game(int width, int height, int fps) {
		super("Don't Ask");
		this.MAX_FPS = fps;
		this.WIDTH = width;
		this.HEIGHT = height;

		Score = 0;
		p = new Vector(WIDTH / 10, 900);
		// fbv

		 b = new Vector(WIDTH / 2 - WIDTH / 10, 950);
		maxBossHealth = 100;

	}

	void init() {
		// initialize JFrame
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(0, 0, WIDTH, HEIGHT);

		MENU = new JPanel();
		// tileMap = new TileMap("tilemap.txt", 42);
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
		// this.pack();
		
		setIgnoreRepaint(true);

		setResizable(false);
		setVisible(true);

		// create double buffer strategy
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		addKeyListener(this);
		setFocusable(true);
		lastFrame = System.currentTimeMillis();
	}

	private void update() {
		// update current fps
		switch (gameState) {
		case MENU:
			break;
		case PLAY:
			fps = (int) (1f / dt);
			// tileMap.update();
			// update sprite
			////////////////////////////////////////////////////////////////////////////////////
			if (isMovingLeft) {
				p.ix -= v.ix;
				if (v.ix < maxSpeed) {
					v.ix = (int) maxSpeed;
				}
			}
			if (isMovingRight) {
				p.ix += v.ix;
				if (v.ix < maxSpeed) {
					v.ix = (int) maxSpeed;
				}
			} else {
				if (p.ix > 0) {
					p.ix -= s.ix;
					if (p.ix < 0) {
						p.ix = 0;
					}
				} else if (p.ix < 0) {
					p.ix += s.ix;
					if (p.ix > 0) {
						p.ix = 0;
					}
				}
				if (jumping) {
					p.iy = 11;
					jumping = false;
					falling = true;
				}
				if (falling) {
					p.iy += gr.iy;
					if (p.iy > 900) {
						p.iy = 900;
					}
				} else {
					v.iy = 0;
				}
			}

			if (canShoot) {
				shoot(b, v, 4);
			} else
				canShoot = false;
			
			if (Vector.sub(fb, b).sqmag() < Math.pow((WIDTH / 10) + (WIDTH / 20), 2)) {
				System.err.println("Bullet hit boss");				
				bossDmg++;
				Score++;
				cBossHealth = maxBossHealth - bossDmg;
				hrate = cBossHealth / maxBossHealth;
				hBarLength = 100 * hrate;
				if (bossDmg == 100) {
					gameState = GAME_STATE.WIN;
				}
			}
			if(Vector.sub(p, fb).sqmag() < (WIDTH /3.5) + WIDTH/10) {
				System.err.println("Player hit boss");
				
			}
			
			System.out.println("Bullet Position " + b.ix);
			System.out.println("Final Boss Position " + fb.ix);
			System.out.println("Player Position" + p.ix);
			
			///////////////////////////////////////////////////////////////////////////////////////////
			// If so, adjust the position and speed.
			fb.ix += fbv.ix;
			fb.iy += fbv.iy;
			
			if(fb.ix < WIDTH/100 || fb.ix > 1200 - WIDTH/100) {
				fbv.setX(-fbv.ix);
			}
			if(fb.iy < HEIGHT/100 || fb.iy > 900 - HEIGHT/100) {
				fbv.setY(-fbv.iy);
			}
			Timer+=dt;
			Score = (long) Timer;
				
		/*	if (fb.ix - WIDTH / 5 < 0) {
				fbv.ix = -fbv.ix; // Reflect along normal
				fb.ix = WIDTH / 5; // Re-position the ball at the edge
			} else if (fb.ix + WIDTH / 5 > WIDTH) {
				fbv.ix = -fbv.ix;
				fb.ix = WIDTH - WIDTH / 5;
			}
			// May cross both x and y bounds
			if (fb.iy - WIDTH / 5 < 0) {
				fbv.iy = -fbv.iy;
				fb.iy = WIDTH / 5;
			} else if (fb.iy + WIDTH / 5 > HEIGHT) {
				fbv.iy = -fbv.iy;
				fb.iy = HEIGHT - WIDTH / 5;
		*/	
			/////////////////////////////////////////////////////////////////////////////////////////////
			break;
		case WIN:
			break;
		case EXIT:
			break;
		// default:
		// break;
		default:
			break;
		}

	}

	@SuppressWarnings("null")
	private void draw() {
		// get canvas

		// init graphics
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		switch (gameState) {
		case MENU:
			break;

		case PLAY:

			// clear screen
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			// draw tilemap
			// tileMap.draw(g);
			// draw fps
			g.setColor(Color.GREEN);
			g.drawString(Long.toString(fps), 10, 40);
			g.drawString(Long.toString(Score), 10,60);

			// draw player
			g.setColor(Color.PINK);
			g.fillOval(p.ix, p.iy, WIDTH / 7, HEIGHT / 7);

			// draw bullet
			if (canShoot) {
				g.setColor(Color.YELLOW.brighter());
				g.fillRect(b.ix, b.iy, WIDTH / 20, 10);
			}

			// draw final boss
			g.setColor(makeRandomColor());
			g.fillOval(fb.ix, fb.iy, WIDTH / 10, HEIGHT / 10);

			// draw health bar

			g.setColor(Color.BLACK.darker());
			g.fillRect(fb.ix, fb.iy - 50, 100, 20);

			g.setColor(Color.RED);
			g.fillRect(fb.ix, fb.iy - 50, (int) hBarLength, 20);

			// release resources, show the buffer
			g.dispose();

			strategy.show();
			break;
		case EXIT:
			System.exit(0);
		case WIN:
			Font font = new Font("Courier", Font.BOLD, 60);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(makeRandomColor());
			g.setFont(font);
			g.drawString("You Win !", WIDTH / 2 - 150, HEIGHT / 2);
			g.drawString(Long.toString(Score), WIDTH / 2 - 150,  HEIGHT / 4);
			g.dispose();

			strategy.show();
			break;
		default:
			break;
		}

	}

	private int shoot(Vector x, Vector v, float a) {
		x.setX(x.ix);
		for (;;) {
			x.setX(x.ix + v.ix*a);
			return (int) v.ix;
		}
	}

	public Color makeRandomColor() {
		return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 5));

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
		case KeyEvent.VK_SPACE:
			canShoot = true;
			while (b.ix >= 1200 && canShoot) {
				b.setX(p.ix);
				b.setY(p.iy + 50);
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
			break;
		case KeyEvent.VK_SPACE:
			canShoot = true;
		}
	}
	public void crash() {
		for(;;) {
			for(;;) {
				for(;;) {
					for(;;) {
						for(;;) {
							for(;;) {
								for(;;) {
									for(;;) {
										for(;;) {
											for(;;) {
												for(;;) {
													for(;;) {
														for(;;) {
															for(;;) {
																for(;;) {
																	for(;;) {
																		for(;;) {
																			for(;;) {
																				for(;;) {
																					for(;;) {
																						for(;;) {
																							for(;;) {
																								for(;;) {
																									for(;;) {
																										for(;;) {
																											for(;;) {
																												for(;;) {
																													for(;;) {
																														for(;;) {
																															for(;;) {
																																for(;;) {
																																	for(;;) {
																																		for(;;) {
																																			for(;;) {
																																				for(;;) {
																																					for(;;) {
																																						for(;;) {
																																							for(;;) {
																																								for(;;) {
																																									for(;;) {
																																									crash();	
																																									}	
																																								}	
																																							}	
																																						}	
																																					}	
																																				}	
																																			}	
																																		}	
																																	}	
																																}	
																															}	
																														}	
																													}	
																												}	
																											}	
																										}	
																									}	
																								}	
																							}	
																						}	
																					}	
																				}	
																			}	
																		}	
																	}	
																}	
															}	
														}	
													}	
												}	
											}	
										}	
									}	
								}	
							}	
						}	
					}	
				}	
			}
		}
	}
}