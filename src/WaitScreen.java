import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WaitScreen extends JFrame {

	private JPanel contentPane;

	Color colors[] = { Color.red, Color.black, Color.cyan, Color.YELLOW,
			Color.GREEN, Color.magenta, Color.DARK_GRAY };

	/**
	 * Create the frame.
	 * 
	 * @param maze
	 * @param gameIndex
	 *            ,int indexInGame
	 */
	public WaitScreen(boolean isHost, final MazeUI maze, final int gameIndex,
			final int indexInGame) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(82, 39, 248, 260);
		layeredPane.add(scrollPane);

		final JTextArea textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JButton btnNewButton = new JButton("GO !");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ClientMethods.start(gameIndex);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(82, 311, 248, 56);
		layeredPane.add(btnNewButton);

		JLabel lblWaitForHost = new JLabel("Wait for host to start");
		lblWaitForHost.setHorizontalAlignment(SwingConstants.CENTER);
		lblWaitForHost.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblWaitForHost.setBounds(82, 316, 248, 42);
		layeredPane.add(lblWaitForHost);

		if (isHost)
			lblWaitForHost.setVisible(false);
		else
			btnNewButton.setVisible(false);

		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						ArrayList<String> names = ClientMethods
								.getNameList(gameIndex);
						textArea.setText("");
						String s = "";
						for (int i = 0; i < names.size(); i++)
							s += names.get(i)+"\n";
						textArea.setText(s);

						if (ClientMethods.isRunning(gameIndex)) {
							ArrayList<Point> state = ClientMethods
									.gameState(gameIndex);
							for (int i = 0; i < state.size(); i++) {
								maze.players.add(new player(colors[i], state
										.get(i).x, state.get(i).y));
							}
							
							maze.playerNames = ClientMethods.getNameList(gameIndex);
							
							 maze.FullScreenFrame();
							maze.gameIndex = gameIndex;
							maze.indexInGame = indexInGame;
							maze.setVisible(true);
							maze.repaint();
							setVisible(false);
							maze.serv_timer.start();
							maze.joined = true;
							break;
						}

						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		t.start();

	}
}
