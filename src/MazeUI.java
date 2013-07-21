import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MazeUI extends JFrame {

	private JPanel contentPane;
	private static char[][] mazeMap;
	private static Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private static int mazeWidth, mazeHeight;
	private static int blockSizeWidth, blockSizeHeight;

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

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		mazeWidth = 100;
		mazeHeight = 100;

		System.out.println(getHeight() + "   " + dim.height);
		blockSizeWidth = (dim.width) / mazeWidth;
		blockSizeHeight = (dim.height) / mazeHeight;
		MazeGenerator generator = new MazeGenerator(mazeWidth, mazeHeight);
		generator.generate();
		mazeMap = generator.getMap();
		drawMap();
		this.addKeyListener(null);
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
	}

	private void drawMap() throws IOException {
		for (int i = 0; i < mazeMap.length; i++) {
			for (int j = 0; j < mazeMap[i].length; j++) {
				if (mazeMap[i][j] == '#') {
					contentPane.add(new BlockWall("maze-image.gif", j
							* blockSizeWidth, i * blockSizeHeight,
							blockSizeWidth, blockSizeHeight));
				}
			}
		}
		for (int i = 0; i < mazeMap.length; i++) {
			mazeMap[0][i] = '#';
			mazeMap[0][i] = '#';
			contentPane.add(new BlockWall("maze-image.gif", i * blockSizeWidth,
					0, blockSizeWidth, blockSizeHeight));
			contentPane.add(new BlockWall("maze-image.gif", 0, i
					* blockSizeHeight, blockSizeWidth, blockSizeHeight));
		}
	}
}
