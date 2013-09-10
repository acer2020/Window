package Window;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable, WindowListener,
		KeyListener {

	private static final long serialVersionUID = 1L;
	long milliseconds = System.currentTimeMillis();
	long fpswait = 1000 / 32;

	public int checkcollision = 0;
	public int checkcollisionx = 0;

	public boolean running;

	int movingdir;

	int speed = 7;

	public int closingfalures = -1;

	public int jumpingspeed = 10;

	public int landx = 160;
	public int landy = 160;
	public int movelandx = 0;
	public int movelandy = 0;

	public int xcord = 0;
	public int ycord = 0;

	public int ranonce = 0;

	public boolean collided = false;

	public int[] cx = new int[] { 100, 185, 275, 220, 250, 350 };
	public int[] cy = new int[] { 100, 75, 75, 100, 100, 100 };

	public int cloudspeed = 1;

	BufferedImage Sky = null;
	BufferedImage Dirt = null;
	BufferedImage Grass = null;
	BufferedImage Character = null;

	int frictionjumping = 1;
	int numberoftimesjumped = 0;

	int frictionwalking = 5;

	public static final int levelwidth = 160;
	public static final int levelheight = 240;
	public int levelpixelstotal = (levelheight * levelwidth);

	public int[] leveldata = new int[levelpixelstotal];

	public int jumpingpower = 10;

	public int xDisplay = 0;
	public int yDisplay = -170;

	boolean trytostopagain;

	public int gravity = 1;

	public int levelloadednothingcount = 0;

	static final public int Airid = 0;
	static final public int Grassid = 1;
	static final public int Dirtid = 2;

	static final int WIDTH = 1600;
	static final int HEIGHT = 800;
	static final String TITLE = "SandBox Pre Alpha 2.0";

	int volleft = 1;
	int volright = 1;

	public int volup = 1;
	public int voldown = 1;

	public boolean isupkeypressed = false;

	public boolean isleftkeypressed = false;
	public boolean isrightkeypressed = false;

	int volmultiplier = 1;

	boolean starting;

	public static void main(String[] args) throws IOException {
		Game game = new Game();
		JFrame frame = new JFrame();

		frame.add(game);
		frame.pack();
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		frame.addKeyListener(game);
		frame.setFocusable(true);
		frame.setFocusTraversalKeysEnabled(true);

		frame.addWindowListener(game);

		game.start();

		game.startthread();

	}

	public void startthread() {

		Thread thread = new Thread(this);
		thread.start();

	}

	public void stop() throws IOException {

		closingfalures++;
		running = false;
		saveLevel();
		System.exit(0);
	}

	public void start() throws IOException {

		try {
			Sky = ImageIO.read(new File("res//background.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "WARNING:Error loading Image: "
					+ e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		try {
			Dirt = ImageIO.read(new File("res//Dirt.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "WARNING:Error loading Image: "
					+ e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		try {
			Grass = ImageIO.read(new File("res//Grass.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "WARNING:Error loading Image: "
					+ e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		try {
			Character = ImageIO.read(new File("res//Character.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "WARNING:Error loading Image: "
					+ e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		running = true;
		loadlevel();
		System.out.println(levelpixelstotal);

	}

	public void update() {

		try {
			Thread.sleep(fpswait);
			movevol();

			repaint();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {

		while (running) {

			update();

		}

	}

	public void paint(Graphics g) {

		super.paintComponent(g);

		// sky
		g.drawImage(Sky, 0, 0, 1600, 800, this);

		// clouds
		// for (int i = 0; i < cx.length; i++) {
		// g.setColor(Color.white);
		// g.fillOval(cx[i], cy[i], 200, 75);
		// }

		// land

		for (int i = 0; i < levelpixelstotal; i++) {

			xDisplay += 80;
			if (movelandx >= 0) {
				landx = ((xDisplay + movelandx) + 640);
			}

			if (movelandy >= 0) {
				landy = ((yDisplay + movelandy) - 5750);
			}

			if (movelandx < 0) {
				landx = (xDisplay - Math.abs(movelandx) + 640);
			}

			if (movelandy < 0) {
				landy = ((yDisplay - Math.abs(movelandy)) - 5750);
			}

			if (xDisplay >= 19200) {
				xDisplay = 0;
				yDisplay += 80;
			}

			if (leveldata[i] == Dirtid) {

				g.drawImage(Dirt, landx, landy, 80, 80, this);

			}

			if (leveldata[i] == Grassid) {

				g.drawImage(Grass, landx, landy, 80, 80, this);

			}

		}

		// character
		g.drawImage(Character, 720, 320, 80, 160, this);

		xDisplay = 0;

		yDisplay = -170;

	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {

		try {
			trytostopagain = true;
			if (trytostopagain) {
				stop();

				if (closingfalures == 1) {
					System.exit(0);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			trytostopagain = true;
		}

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	public Object getLevelWidth() {
		return levelwidth;
	}

	public Object getLevelHeight() {
		return levelheight;
	}

	public int getLevelPixelsTotal() {
		return levelpixelstotal;
	}

	public int[] getLevelData(int i) {
		return leveldata;
	}

	public void actionPerformed(ActionEvent e) {

	}

	public void keyPressed(KeyEvent e) {

		int c = e.getKeyCode();

		if ((c == KeyEvent.VK_UP) || (c == KeyEvent.VK_SPACE)
				|| (c == KeyEvent.VK_W)) {
			isupkeypressed = true;
			this.move(1);
		}
		if ((c == KeyEvent.VK_RIGHT) || (c == KeyEvent.VK_D)) {
			this.move(3);
			isrightkeypressed = true;
		}
		if ((c == KeyEvent.VK_LEFT) || (c == KeyEvent.VK_A)) {
			this.move(7);
			isleftkeypressed = true;
		}
	}

	public void movevol() {

		if ((isleftkeypressed)) {
			movelandx += speed;
			xcord--;
			System.out.println("x: " + xcord);

			volright -= speed;
			volleft -= frictionwalking;
		}
		if ((isrightkeypressed)) {
			movelandx -= speed;
			xcord++;
			System.out.println("x: " + xcord);

			volleft -= speed;
			volright -= frictionwalking;
		}
		if ((volup > 1)) {

			movelandy += speed;

			ycord++;
			System.out.println("y: " + ycord);

			voldown -= speed;
			volup -= frictionjumping;
			numberoftimesjumped = 1;
		}
		if ((voldown > 1) && (!hascollided())) {
			movelandy -= speed;
			ycord--;
			System.out.println("y: " + ycord);

			volup -= speed;
			voldown -= frictionwalking;
		}

		if (volup <= 0) {
			volup = 1;
		}
		if (voldown <= 0) {
			voldown = 1;
		}
		if (volright <= 0) {
			volright = 1;
		}
		if (volleft <= 0) {
			volleft = 1;
		}

		volup -= gravity;
		voldown += gravity;

		for (int i = 0; i < cx.length; i++) {
			if (cx[i] > 1600) {

				cx[i] = -200;
			}

			cx[i]++;

		}
	}

	public void move(int movingdir) {

		if (movingdir == 1) {
			volup += jumpingpower;
		}
		if (movingdir == 3) {
			volright += speed;
		}
		if (movingdir == 7) {
			volleft += speed;
		}

	}

	public void keyReleased(KeyEvent e) {
		int c = e.getKeyCode();

		if ((c == KeyEvent.VK_UP) || (c == KeyEvent.VK_SPACE)
				|| (c == KeyEvent.VK_W)) {
			isupkeypressed = false;
		}
		if ((c == KeyEvent.VK_RIGHT) || (c == KeyEvent.VK_D)) {
			isrightkeypressed = false;
		}
		if ((c == KeyEvent.VK_LEFT) || (c == KeyEvent.VK_A)) {
			isleftkeypressed = false;
		}

	}

	public void keyTyped(KeyEvent e) {

	}

	public void loadlevel() {

		try {
			Scanner scanner = new Scanner(new File("res//leveldata.txt"));

			try {

				while (scanner.hasNext()) {

					for (int i = 0; i < levelpixelstotal; i++) {
						int data = scanner.nextInt();
						System.out.println(data);
						leveldata[i] = data;
					}

				}

			} catch (Exception e) {

				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"WARNING:Error loading level: " + e.getMessage()
								+ " Hit ok to create the level", "ERROR",
						JOptionPane.ERROR_MESSAGE);
				generatelevel();

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "WARNING:Error loading level: "
					+ e.getMessage() + " Hit ok to create the file", "ERROR!",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			generatelevel();
		}
		;

	}

	public void saveLevel() throws IOException {
		Formatter formatter = new Formatter("res//leveldata.txt");
		try {
			for (int i = 0; i < levelpixelstotal; i++) {
				System.out.println(leveldata[i]);
				formatter.format("%d\n", leveldata[i]);
			}
			formatter.flush();
		} finally {
			formatter.close();
		}
	}

	public boolean hascollided() {

		for (int i = 0; i < levelpixelstotal; i++) {

			if (checkcollision < (levelpixelstotal - 240)) {
				checkcollision = i + 240;
			}
			
			if (checkcollisionx < (levelpixelstotal - 160)) {
				checkcollisionx = i + 160;
			}

			if (ycord >= 0) {

				if ((i >= (((levelpixelstotal / 2) + (ycord * 240))))) {

						if ((leveldata[checkcollisionx] == Dirtid)
								|| (leveldata[checkcollisionx] == Grassid)) {
							collided = true;
						}else {
							collided = false;
						}

				}else {
					collided = false;
				}

			}

			if (checkcollision < (levelpixelstotal - 240)) {
				checkcollision = i + 240;
			}
			
			if (checkcollisionx < (levelpixelstotal - 80)) {
				checkcollisionx = i + 80;
			}

			if (ycord < 0) {

				if ((i >= (((levelpixelstotal / 2) - (Math.abs(ycord) * 240))))) {

					if ((i >= (((levelpixelstotal / 2) - (Math.abs(xcord) * 80))))) {

						if ((leveldata[checkcollisionx] == Dirtid)
								|| (leveldata[checkcollisionx] == Grassid)) {
							collided = true;
						} else {
							collided = false;
						}

					}

				}else {
					collided = false;
				}

			} else {
				collided = false;
			}

		}
		return collided;

	}

	public void generatelevel() {

		for (int i = 0; i < levelpixelstotal; i++) {

			if (i >= (levelpixelstotal / 2)) {

				leveldata[i] = Grassid;

			}

		}

		try {
			stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
