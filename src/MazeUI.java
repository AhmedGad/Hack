import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class MazeUI extends JFrame implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static char[][] mazeMap;
	private static Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private static int mazeWidth, mazeHeight;
	private static int blockSizeWidth, blockSizeHeight;
	private static ArrayList<player> players = new ArrayList<player>();
	private static ArrayList<Integer> games = new ArrayList<Integer>();
	private static boolean joined = false;

	static class player {
		Color color;
		int curri, currj, disti, distj;

		public player(Color color, int curri, int currj, int disti, int distj) {
			this.color = color;
			this.curri = curri;
			this.currj = currj;
			this.disti = disti;
			this.distj = distj;
		}

	}

	static ActionListener taskPerformer = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent evt) {
		}
	};

	static ActionListener serv_taskPerformer = new ActionListener() {
		private int gameIndex = -1;
		private int id = -1;
		private int indexInGame = -1;

		@Override
		public void actionPerformed(ActionEvent evt) {
			URL url;
			try {
				url = new URL("http://hackserver.herokuapp.com");
				HttpURLConnection httpConn = (HttpURLConnection) url
						.openConnection();
				httpConn.setDoOutput(true);
				httpConn.setRequestMethod("POST");
				httpConn.connect();
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
						players.get(cnt).disti = new Integer(tok.nextToken())
								* blockSizeHeight;
						players.get(cnt).distj = new Integer(tok.nextToken())
								* blockSizeWidth;
						cnt++;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private static Timer serv_timer = new Timer(100, serv_taskPerformer);
	private static Timer timer = new Timer(50, taskPerformer);

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

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public MazeUI() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, screenSize.width, screenSize.height);
		FullScreenFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dim);

		mazeWidth = 200;
		mazeHeight = 200;

//		System.out.println(getHeight() + "   " + dim.height);
		blockSizeWidth = (dim.width) / mazeWidth;
		blockSizeHeight = (dim.height) / mazeHeight;
		MazeGenerator generator = new MazeGenerator(mazeWidth, mazeHeight);
		generator.generate();
		mazeMap = generator.getMap();
		// drawMap();
		addKeyListener(this);
	}

	private void FullScreenFrame() {

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
//		System.err.println("Full screen not supported");
//		setSize(100, 100); // just something to let you see the window
//		setVisible(true);
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
		if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
			// GraphicsDevice gd = GraphicsEnvironment
			// .getLocalGraphicsEnvironment().getDefaultScreenDevice();
			// gd.setFullScreenWindow(null);
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
