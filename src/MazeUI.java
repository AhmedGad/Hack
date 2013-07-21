import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.Timer;

public class MazeUI extends JFrame implements KeyListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static char[][] mazeMap;
	private static Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private static int mazeWidth, mazeHeight;
	private static int blockSizeWidth, blockSizeHeight;
	private static ArrayList<player> players = new ArrayList<player>();
	private static ArrayList<Integer> games = new ArrayList<Integer>();
	private static boolean joined = false;

	static class player {
		Color color;
		int curri, currj;

		public player(Color color, int curri, int currj) {
			this.color = color;
			this.curri = curri;
			this.currj = currj;
		}

	}

	// @Override
	// public void actionPerformed(ActionEvent evt) {
	// // if (pl.curri != pl.disti) {
	// // if (pl.curri < pl.disti)
	// // pl.curri += di;
	// // else if (pl.curri > pl.distj)
	// // pl.curri -= di;
	// //
	// // if (Math.abs(pl.curri - pl.disti) < di)
	// // pl.curri = pl.disti;
	// // }
	// //
	// // if (pl.currj != pl.distj) {
	// // if (pl.currj < pl.distj)
	// // pl.currj += dj;
	// // else if (pl.currj > pl.distj)
	// // pl.currj -= dj;
	// //
	// // if (Math.abs(pl.currj - pl.distj) < dj)
	// // pl.currj = pl.distj;
	// // }
	// // }
	// repaint();
	// }

	@Override
	public void actionPerformed(ActionEvent evt) {
		try {
			HttpURLConnection httpConn = ClientMethods.initConnection();
			OutputStream os = httpConn.getOutputStream();
			if (!joined) {
				os.write("getFreeGames".getBytes());
				os.close();
				InputStream in = httpConn.getInputStream();
				byte[] buffer = new byte[1000];
				int read;
				String tempstr = "";
				while ((read = in.read(buffer)) != -1)
					tempstr += new String(buffer, 0, read);
				in.close();

				StringTokenizer tok = new StringTokenizer(tempstr);
				games.clear();
				while (tok.hasMoreElements())
					games.add(new Integer(tok.nextToken()));
			} else {
				os.write(("gameState " + gameIndex).getBytes());
				os.close();
				InputStream in = httpConn.getInputStream();
				byte[] buffer = new byte[1000];
				int read;
				String tempstr = "";
				while ((read = in.read(buffer)) != -1)
					tempstr += new String(buffer, 0, read);
				in.close();

				StringTokenizer tok = new StringTokenizer(tempstr);
				int cnt = 0;
				while (tok.hasMoreElements()) {
					players.get(cnt).curri = new Integer(tok.nextToken());
					players.get(cnt).currj = new Integer(tok.nextToken());
					cnt++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
	}

	private Timer serv_timer = new Timer(100, this);

	// private Timer timer = new Timer(50, this);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MazeUI frame = new MazeUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	private static int gameIndex, indexInGame;;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public MazeUI() throws Exception {

		// ///// for test /////
		// int id = ClientMethods.newPlayer(1, 1, "test");
		// gameIndex = ClientMethods.host(id, mazeMap);
		// joined = true;
		// ClientMethods.start(gameIndex);
		// ArrayList<Point> gameState = ClientMethods.gameState(gameIndex);
		// indexInGame = 0;
		// for (int i = 0; i < gameState.size(); i++)
		// players.add(new player(Color.red, 1, 1));
		//
		// serv_timer.start();
		// // ////////////////////

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, screenSize.width, screenSize.height);
//		FullScreenFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dim);

		mazeWidth = 112 + 1;
		mazeHeight = 112 + 1;

		// System.out.println(getHeight() + "   " + dim.height);
		blockSizeWidth = (dim.width) / mazeWidth;
		blockSizeHeight = (dim.height) / mazeHeight;
		MazeGenerator generator = new MazeGenerator(mazeWidth - 1,
				mazeHeight - 1);
		generator.generate();
		mazeMap = generator.getMap();
		// drawMap();
		addKeyListener(this);
	}

	public void FullScreenFrame() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		if (gd.isFullScreenSupported()) {
			setUndecorated(true);
			gd.setFullScreenWindow(this);
		} else {
			System.err.println("Full screen not supported");
			setSize(100, 100); // just something to let you see the window
			setVisible(true);
		}

		// System.err.println("Full screen not supported");
		// setSize(100, 100); // just something to let you see the window
		// setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.blue);
		for (int i = 0; i < mazeMap.length; i++) {
			for (int j = 0; j < mazeMap[i].length; j++) {
				if (mazeMap[i][j] == '#') {
					g.fillRect(j * blockSizeWidth, i * blockSizeHeight,
							blockSizeWidth, blockSizeHeight);
				}
			}
		}
		for (player pl : players) {
			g.setColor(pl.color);
			g.fillOval(pl.currj * blockSizeWidth, pl.curri * blockSizeHeight,
					blockSizeWidth * 2, blockSizeHeight * 2);
		}
	}

	// private void drawMap() throws IOException {
	// for (int i = 0; i < mazeMap.length; i++) {
	// for (int j = 0; j < mazeMap[i].length; j++) {
	// if (mazeMap[i][j] == '#') {
	// contentPane.add(new BlockWall("maze-image.gif", j
	// * blockSizeWidth, i * blockSizeHeight,
	// blockSizeWidth, blockSizeHeight));
	// }
	// }
	// }
	// for (int i = 0; i < mazeMap.length; i++) {
	// mazeMap[0][i] = '#';
	// mazeMap[0][i] = '#';
	// contentPane.add(new BlockWall("maze-image.gif", i * blockSizeWidth,
	// 0, blockSizeWidth, blockSizeHeight));
	// contentPane.add(new BlockWall("maze-image.gif", 0, i
	// * blockSizeHeight, blockSizeWidth, blockSizeHeight));
	// }
	// }

	@Override
	public void keyPressed(KeyEvent k) {
		int dir = -1;
		switch (k.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_UP:
			dir = 0;
			break;
		case KeyEvent.VK_DOWN:
			dir = 1;
			break;
		case KeyEvent.VK_RIGHT:
			dir = 2;
			break;
		case KeyEvent.VK_LEFT:
			dir = 3;
			break;

		default:
			break;
		}

		if (dir > -1) {
			player p = players.get(indexInGame);
			int ni = p.curri + di[dir];
			int nj = p.currj + dj[dir];
			if (ni < 0 || nj < 0)
				return;
			for (int i = 0; i < 2 && i + ni < mazeMap.length; i++)
				for (int j = 0; j < 2 && j + nj < mazeMap[0].length; j++)
					if (mazeMap[ni + i][nj + j] == '#')
						return;
			try {
				ClientMethods.move(gameIndex, indexInGame, dir);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	int[] di = { -1, 1, 0, 0 };
	int[] dj = { 0, 0, 1, -1 };

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
