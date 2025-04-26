
import java.util.Scanner;

public class B15684 {

	static int n, m, h;
	static boolean[][] ladder;
	static int result = 4;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		m = sc.nextInt();
		h = sc.nextInt();

		ladder = new boolean[h + 1][n + 1];

		for (int i = 0; i < m; i++) {
			int a = sc.nextInt();
			int b = sc.nextInt();
			ladder[a][b] = true;
		}

		dfs(0, 1, 1);
		if (result < 3||result==3) {
			System.out.println(result);
		} else {
			System.out.println(-1);
		}

	}

	static void dfs(int count, int x, int y) {
		if (count >= result)
			return;
		if (check()) {
			result = count;
			return;
		}
		if (count == 3)
			return;

		for (int i = x; i <= h; i++) {
			int jstart = (i == x) ? y : 1;
			for (int j = jstart; j < n; j++) {
				if (ladder[i][j])
					continue;
				if (j > 1 && ladder[i][j - 1])
					continue;
				if (j < n - 1 && ladder[i][j + 1])
					continue;

				ladder[i][j] = true;
				dfs(count + 1, i, j);
				ladder[i][j] = false;
			}
		}
	}

	static boolean check() {
		for (int i = 1; i < n + 1; i++) {
			int tmp = i;
			for (int j = 1; j < h + 1; j++) {
				if (ladder[j][tmp]) {
					tmp++;
				} else if (tmp > 1 && ladder[j][tmp - 1]) {
					tmp--;
				}
			}
			if (tmp != i)
				return false;
		}
		return true;
	}
}
