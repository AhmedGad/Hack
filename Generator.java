import java.util.ArrayList;
import java.util.Stack;

public class Generator {
	public static void main(String[] args) {
		generate();
	}

	public static void generate() {
		int width = 60;
		int height = 60;
		char[][] map = new char[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				map[i][j] = '.';
			}
		}
		w = width / 4;
		h = height / 4;

		p = new int[w * h];
		for (int i = 0; i < p.length; i++)
			p[i] = i;

		ArrayList<wall> a = new ArrayList<wall>();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				a.add(new wall(i, j, 0));
				a.add(new wall(i, j, 1));
			}
		}
		boolean[][][] v = new boolean[h][w][2];
		for(int i = 0 ; i < h;i++){
			for(int j = 0 ; j < w;j++){
				if(i==h-1||j==w-1){
					int pos = (i==h-1?0:1);
					v[i][j][pos] = true;
					int ci = i * 4 + 3;
					int cj = j * 4 + 3;
					if (pos == 1) {
						map[ci][cj] = '#';
						map[ci - 1][cj] = '#';
						map[ci - 2][cj] = '#';
						map[ci - 3][cj] = '#';
					} else {
						map[ci][cj] = '#';
						map[ci][cj - 1] = '#';
						map[ci][cj - 2] = '#';
						map[ci][cj - 3] = '#';
					}
				}
			}
		}
		int czi = h-1;
		int czj = w-1;
		v[czi][czj][1]=true;
		czi = czi*4+3;
		czj = czj*4+3;
		map[czi][czj] = '#';
		map[czi - 1][czj] = '#';
		map[czi - 2][czj] = '#';
		map[czi - 3][czj] = '#';

		while (!a.isEmpty()) {
			int ind = (int) (a.size() * Math.random());
			wall cur = a.remove(ind);
			if ( ok(cur, v)) {
				v[cur.i][cur.j][cur.pos] = true;
				int ci = cur.i * 4 + 3;
				int cj = cur.j * 4 + 3;
				if (cur.pos == 1) {
					map[ci][cj] = '#';
					map[ci - 1][cj] = '#';
					map[ci - 2][cj] = '#';
					map[ci - 3][cj] = '#';
				} else {
					map[ci][cj] = '#';
					map[ci][cj - 1] = '#';
					map[ci][cj - 2] = '#';
					map[ci][cj - 3] = '#';
				}
			}
		}

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (!v[i][j][1])
					union(i, j, i, j + 1);
				if (!v[i][j][0])
					union(i, j, i + 1, j);

			}
		}
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int ci = i*4+3;
				int cj = j*4+3;
				if (v[i][j][1] && j != w - 1
						&& find(i * w + j) != find(i * w + j + 1)) {
					v[i][j][1] = false;
					map[ci][cj] = '.';
					map[ci - 1][cj] = '.';
					map[ci - 2][cj] = '.';
					map[ci - 3][cj] = '.';
					union(i,j,i,j+1);
				} else if (v[i][j][0] && i != h - 1
						&& find(i * w + j) != find((i + 1) * w + j)) {
					v[i][j][0] = false;
					map[ci][cj] = '.';
					map[ci][cj - 1] = '.';
					map[ci][cj - 2] = '.';
					map[ci][cj - 3] = '.';
					union(i,j,i+1,j);
				}
			}
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
	}

	static int[] di = { 1, -1, 0, 0 };
	static int[] dj = { 0, 0, 1, -1 };

	public static boolean ok(wall cur, boolean[][][] v) {
		boolean ret = true;
		if (cur.pos == 1) {
			ret = check(cur.i, cur.j, v);
			if (cur.j + 1 < v[0].length)
				ret &= check(cur.i, cur.j + 1, v);
		} else if (cur.pos == 0) {
			ret = check(cur.i, cur.j, v);
			if (cur.i + 1 < v.length)
				ret &= check(cur.i + 1, cur.j, v);
		}
		return ret;
	}

	public static boolean check(int i, int j, boolean[][][] v) {
		boolean left = (j == 0 || v[i][j - 1][1]);
		boolean right = v[i][j][1];
		boolean up = (i == 0 || v[i - 1][j][0]);
		boolean down = v[i][j][0];
		int s = (left ? 1 : 0) + (right ? 1 : 0) + (up ? 1 : 0)
				+ (down ? 1 : 0);
		return s < 2;
	}

	static class wall {
		int i, j, pos;

		public wall(int i, int j, int pos) {
			this.i = i;
			this.j = j;
			this.pos = pos;
		}

	}

	static int w, h;
	static int[] p;

	public static void union(int i, int j, int i1, int j1) {
		int p1 = i * w + j;
		int p2 = i1 * w + j1;
		p[find(p1)] = find(p2);
	}

	public static int find(int p2) {
		if (p[p2] == p2)
			return p2;
		return find(p[p2]);
	}
}
