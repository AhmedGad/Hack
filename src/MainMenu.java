import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	ArrayList<Integer> hosts;
	boolean run = true;

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public MainMenu() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);

		textField = new JTextField();
		textField.setBounds(79, 6, 325, 28);
		layeredPane.add(textField);
		textField.setColumns(10);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(6, 6, 61, 28);
		layeredPane.add(lblName);

		final JComboBox comboBox = new JComboBox();
		comboBox.setBounds(529, 99, 196, 27);
		layeredPane.add(comboBox);

		JLabel lblAvailableHosts = new JLabel("Available Hosts");
		lblAvailableHosts.setBounds(405, 98, 130, 27);
		layeredPane.add(lblAvailableHosts);

		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.setBounds(395, 57, 330, 29);
		layeredPane.add(btnJoinGame);

		JButton btnHostGame = new JButton("Host Game");
		btnHostGame.setBounds(52, 57, 260, 69);
		layeredPane.add(btnHostGame);

		JLabel lblOr = new JLabel("OR");
		lblOr.setBounds(342, 82, 41, 16);
		layeredPane.add(lblOr);

		final Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (run) {
						hosts = ClientMethods.getFreeGames();
						Vector<String> v = new Vector<String>();
						for (int i = 0; i < hosts.size(); i++)
							v.add("Game no. " + hosts.get(i));
						ComboBoxModel model = new DefaultComboBoxModel(v);
						comboBox.setModel(model);
						Thread.sleep(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		t.start();

		final MazeUI maze = new MazeUI();
		maze.setVisible(false);

		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textField.getText().trim().length() != 0) {
					run = false;
					int gameIndex = 0, indexInGame = 0;
					try {
						int id = ClientMethods.newPlayer(1, 1,
								textField.getText());
						ArrayList<String> gameMap = new ArrayList<String>();
						indexInGame = ClientMethods.join(
								gameIndex = hosts.get(comboBox
										.getSelectedIndex()), id, gameMap);
						maze.mazeMap = new char[gameMap.size()][];
						for (int i = 0; i < gameMap.size(); i++)
							maze.mazeMap[i] = gameMap.get(i).toCharArray();

					} catch (Exception e) {
						e.printStackTrace();
					}
					setVisible(false);
					WaitScreen frame = new WaitScreen(true, maze, gameIndex,
							indexInGame);
					frame.setVisible(true);
				}
			}
		});

		btnHostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().trim().length() != 0) {
					run = false;
					int id, gameIndex = 0;
					try {
						id = ClientMethods.newPlayer(1, 1, textField.getText());
						ArrayList<String> gameMap = new ArrayList<String>();
						maze.mazeMap = new char[gameMap.size()][];
						for (int i = 0; i < gameMap.size(); i++)
							maze.mazeMap[i] = gameMap.get(i).toCharArray();
						gameIndex = ClientMethods.host(id, maze.mazeMap);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setVisible(false);
					WaitScreen frame = new WaitScreen(false, maze, gameIndex, 0);
					frame.setVisible(true);

				}
			}
		});
	}
}
