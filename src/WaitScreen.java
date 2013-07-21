import java.awt.BorderLayout;
import java.awt.EventQueue;

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

public class WaitScreen extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WaitScreen frame = new WaitScreen(false, null, 0, 0);
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
	 * @param maze
	 * @param gameIndex
	 *            ,int indexInGame
	 */
	public WaitScreen(boolean isHost, final MazeUI maze, final int gameIndex,
			int indexInGame) {
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
							s += names.get(i);
						textArea.setText(s);

						if (ClientMethods.isRunning(gameIndex)) {
							maze.setVisible(true);
							maze.FullScreenFrame();
							setVisible(false);
							break;
						}

						Thread.sleep(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		t.start();
		
	}
}
