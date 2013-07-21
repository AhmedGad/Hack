import java.awt.Point;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientMethods {

	public static HttpURLConnection initConnection() {
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL("http://hackserver.herokuapp.com");
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.connect();
		} catch (Exception e) {
		}
		return httpConn;
	}

	// return playerId
	static public int newPlayer(int i, int j, String name) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("new " + i + " " + j + " " + name).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		return new Integer(tempstr.trim());
	}

	// doesn't return any fucking thing
	static public void move(int gameIndex, int indexInGame, int dir)
			throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("move " + gameIndex + " " + indexInGame + " " + dir)
				.getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
	}

	// return game Index
	static public int host(int id, char[][] map) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("host " + id + " ").getBytes());
		for (int i = 0; i < map.length; i++)
			os.write((new String(map[i]) + " ").getBytes());

		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		return new Integer(tempstr.trim());
	}

	// return Index in game
	static public int join(int gameIndex, int id, ArrayList<String> gameMap)
			throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("join " + gameIndex + " " + id).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		StringTokenizer tok = new StringTokenizer(tempstr);
		int indexInGame = new Integer(tok.nextToken());

		while (tok.hasMoreElements())
			gameMap.add(tok.nextToken());

		return indexInGame;
	}

	// return list of free games
	static public ArrayList<Integer> getFreeGames() throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("getFreeGames").getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		StringTokenizer tok = new StringTokenizer(tempstr);
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (tok.hasMoreElements())
			list.add(new Integer(tok.nextToken()));
		return list;
	}

	// also doesn't return a fucking thing
	static public void start(int gameIndex) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("start " + gameIndex).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
	}

	// same
	static public void stop(int gameIndex, int indexInGame) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("stop " + gameIndex).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
	}

	// mohemma fash5 - return list of player's i and j
	static public ArrayList<Point> gameState(int gameIndex) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
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
		ArrayList<Point> lst = new ArrayList<Point>();
		while (tok.hasMoreElements())
			lst.add(new Point(new Integer(tok.nextToken()), new Integer(tok
					.nextToken())));
		return lst;
	}

	// return if this game has started
	public static boolean isRunning(int gameIndex) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
		os.write(("isRunning " + gameIndex).getBytes());
		os.close();
		InputStream in = httpConn.getInputStream();
		byte[] buffer = new byte[1000];
		int read;
		String tempstr = "";
		while ((read = in.read(buffer)) != -1)
			tempstr += new String(buffer, 0, read);
		in.close();
		return tempstr.equals("0") ? false : true;
	}

	// return name list
	public static ArrayList<String> getNameList(int gameIndex) throws Exception {
		HttpURLConnection httpConn = initConnection();
		OutputStream os = httpConn.getOutputStream();
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
		ArrayList<String> lst = new ArrayList<String>();
		while (tok.hasMoreElements())
			lst.add(tok.nextToken());
		return lst;
	}

}
