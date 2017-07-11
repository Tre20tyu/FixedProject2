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
	//window vars
    private final int MAX_FPS;
    private final int WIDTH;
    private final int HEIGHT;

    //double buffer
    private BufferStrategy strategy;

    //loop variables
    private boolean isRunning = true;
    private long rest = 0;

    //timing variables
    private float dt;
    private long lastFrame;
    private long startFrame;
    private int fps;

    //sprite1 variables
    private float x = 50;
    private float v = 10;

    //sprite2 variables
    private float x2 = 50.0f;
    private float v2 = 100.0f;

    ArrayList<Integer> keys = new ArrayList<>();

    private void handleKeys() {
        for(int key : keys) {
            switch(key) {
                case KeyEvent.VK_UP:
                    break;
                case KeyEvent.VK_DOWN:
                    break;
            }
        }
    }

    
    //ground
    private int ground;


    public Game(int width, int height, int fps){
        super("JFrame Demo");
        this.MAX_FPS = fps;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    void init(){
        //initialize JFrame
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, WIDTH, HEIGHT);

        setIgnoreRepaint(true);

        setResizable(false);
        setVisible(true);

        //create double buffer strategy
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        lastFrame = System.currentTimeMillis();
    }

    private void update(){
        //update current fps
        fps = (int)(1f/dt);

        handleKeys();

        //update sprite
        x += v;
        if( x < 0 || x > (WIDTH - 100)) v *= -1;

        x2 += v2 * dt;
        if( x2 < 0 || x2 > (WIDTH - 50)) v2 *= -1.0f;
    }
 

    private void draw(){
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.setColor(Color.blue);
        g.fillRect(0,0,WIDTH, HEIGHT);
        g.setColor(Color.GRAY);
        g.fillRect(0, 700, WIDTH, HEIGHT);

        //draw fps
        g.setColor(Color.GREEN);
        g.drawString(Long.toString(fps), 10, 40);

        //draw sprite
        g.setColor(Color.white);
        g.fillRect((int)x, HEIGHT/2 - 25, 100, 100);

        g.setColor(Color.red);
        g.fillRect((int)x2, HEIGHT/2, 50, 50);
        
       
        

        //release resources, show the buffer
        g.dispose();
        strategy.show();
        addKeyListener(this);
        setFocusable(true);
    }


    public void run(){
        init();

        while(isRunning){
            //new loop, clock the start
            startFrame = System.currentTimeMillis();
            //calculate delta time
            dt = (float)(startFrame - lastFrame)/1000;
            //log the current time
            lastFrame = startFrame;

            //call update and draw methods
            update();
            draw();

            //dynamic thread sleep, only sleep the time we need to cap the framerate
            rest = (1000/MAX_FPS) - (System.currentTimeMillis() - startFrame);
            if(rest > 0){
                try{ Thread.sleep(rest); }
                catch (InterruptedException e){ e.printStackTrace(); }
            }
        }

    }


    public static void main(String[] args){
        Game game = new Game(800, 800, 60);
        game.run();
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    for(int i = keys.size() - 1; 1 >= 0; i--) {
    	if(keys.get(i) == keyEvent.getKeyCode())
    		keys.remove(i);
    }
    }

	@Override
	public void keyReleased(KeyEvent e) {
		if(!keys.contains(e.getKeyCode()))
		    keys.remove(e.getKeyCode());
		
	}

}