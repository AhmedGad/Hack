import java.util.ArrayList;

public class Generator {
	public static void main(String[] args) {
		generate();
	}

	public static void generate() {
		int width = 88;
		int height = 88;
		int[][] map = new int[height][width];
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
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (i == h - 1 || j == w - 1) {
					int pos = (i == h - 1 ? 0 : 1);
					v[i][j][pos] = true;
					int ci = i * 4 + 3;
					int cj = j * 4 + 3;
					if (pos == 1) {
						map[ci][cj]++;
						map[ci - 1][cj]++;
						map[ci - 2][cj]++;
						map[ci - 3][cj]++;
					} else {
						map[ci][cj]++;
						map[ci][cj - 1]++;
						map[ci][cj - 2]++;
						map[ci][cj - 3]++;
					}
				}
			}
		}
		int czi = h - 1;
		int czj = w - 1;
		v[czi][czj][1] = true;
		czi = czi * 4 + 3;
		czj = czj * 4 + 3;
		map[czi][czj]++;
		map[czi - 1][czj]++;
		map[czi - 2][czj]++;
		map[czi - 3][czj]++;
		ArrayList<wall> used = new ArrayList<wall>();
		while (!a.isEmpty()) {
			int ind = (int) (a.size() * Math.random());
			wall cur = a.remove(ind);
			if (ok(cur, v)) {
				v[cur.i][cur.j][cur.pos] = true;
				int ci = cur.i * 4 + 3;
				int cj = cur.j * 4 + 3;
				if (cur.pos == 1) {
					map[ci][cj]++;
					map[ci - 1][cj]++;
					map[ci - 2][cj]++;
					map[ci - 3][cj]++;
				} else {
					map[ci][cj]++;
					map[ci][cj - 1]++;
					map[ci][cj - 2]++;
					map[ci][cj - 3]++;
				}
				used.add(cur);
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
		ArrayList<Integer> li = new ArrayList<Integer>();
		ArrayList<Integer> lj = new ArrayList<Integer>();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				li.add(i);
				lj.add(j);
			}
		}
		while (!li.isEmpty()) {
			int ind = (int) (Math.random() * li.size());
			int i = li.remove(ind);
			int j = lj.remove(ind);
			int ci = i * 4 + 3;
			int cj = j * 4 + 3;
			if (v[i][j][1] && j != w - 1
					&& find(i * w + j) != find(i * w + j + 1)) {
				v[i][j][1] = false;
				map[ci][cj]--;
				map[ci - 1][cj]--;
				map[ci - 2][cj]--;
				map[ci - 3][cj]--;
				union(i, j, i, j + 1);
			} else if (v[i][j][0] && i != h - 1
					&& find(i * w + j) != find((i + 1) * w + j)) {
				v[i][j][0] = false;
				map[ci][cj]--;
				map[ci][cj - 1]--;
				map[ci][cj - 2]--;
				map[ci][cj - 3]--;
				union(i, j, i + 1, j);
			}
		}

		int oldsize = used.size();
		while (used.size() > 4 * oldsize / 5) {
			wall cur = used.remove((int) (Math.random() * used.size()));
			if (getNum(cur.i, cur.j, v) < 2)
				continue;
			int i = cur.i;
			int j = cur.j;
			v[i][j][cur.pos] = false;
			int ci = i * 4 + 3;
			int cj = j * 4 + 3;
			if (cur.pos == 1) {
				map[ci][cj]--;
				map[ci - 1][cj]--;
				map[ci - 2][cj]--;
				map[ci - 3][cj]--;
			} else {
				map[ci][cj]--;
				map[ci][cj - 1]--;
				map[ci][cj - 2]--;
				map[ci][cj - 3]--;
			}
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int cnt = 0;
				for (int k = 0; k < 4; k++) {
					int ni = i + di[k];
					int nj = j + dj[k];
					if (ni >= 0 && ni < height && nj >= 0 && nj < width) {
						if (map[ni][nj] != 0) {
							int nni = ni + di[k];
							int nnj = nj + dj[k];
							if (nni >= 0 && nni < height && nnj >= 0 && nnj < width) {
								if (map[nni][nnj] != 0) {
									cnt++;
								}
							}
						}
					}
				}
				if(cnt>=2)map[i][j]++;
			}
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				char c = ' ';
				if (map[i][j] == 0)
					c = '.';
				else
					c = '#';
				System.out.print(c + " ");
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

	public static int getNum(int i, int j, boolean[][][] v) {
		boolean left = (j == 0 || v[i][j - 1][1]);
		boolean right = v[i][j][1];
		boolean up = (i == 0 || v[i - 1][j][0]);
		boolean down = v[i][j][0];
		int s = (left ? 1 : 0) + (right ? 1 : 0) + (up ? 1 : 0)
				+ (down ? 1 : 0);
		return s;
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
