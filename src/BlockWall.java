import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BlockWall extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image backgroundImage;
	private int x, y, w, h;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.

	/*
	 * Constructor with parameter file picture name
	 */
	public BlockWall(String fileName, int x, int y, int blockWidth,
			int blockHeight) throws IOException {
		backgroundImage = ImageIO.read(new File(fileName));
		this.w = blockWidth;
		this.h = blockHeight;
		this.x = x;
		this.y = y;
		setBounds(x, y, w, h);
	}

	/*
	 * (non-Javadoc) PaintComponent
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	}
}