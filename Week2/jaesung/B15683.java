package jaesung;

/*
 문제 : 15683(감시)
 시간 : 352ms
 풀이 : 각 CCTV 각 방향을 감시하는 경우에 대해 감시칸을 체크하고 최소 사각지대 칸을 계산

  */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

class Pos {
	int row, col;

	public Pos(int row, int col) {
		this.row = row;
		this.col = col;
	}
}

class CCTV {
	int type, curDirection;
	Pos pos;
	List<Integer> Ways;

	public CCTV(int type, Pos pos) {
		curDirection = 0;

		this.type = type;
		this.pos = pos;

		makeWays();
	}

	void makeWays() {
		if (type == 1 || type == 3 || type == 4) {
			Ways = new LinkedList<>(Arrays.asList(0, 1, 2, 3));
		} else if (type == 2) {
			Ways = new LinkedList<>(Arrays.asList(0, 1));
		} else {
			Ways = new LinkedList<>(Arrays.asList(0));
		}
	}
}

public class B15683 {
	static int N, M, ans;
	static List<CCTV> CCTVList;
	static int[][] board;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		board = new int[N][M];
		CCTVList = new ArrayList<>();

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());

				if (board[i][j] >= 1 && board[i][j] <= 5) {
					CCTVList.add(new CCTV(board[i][j], new Pos(i, j)));
				}
			}
		}

		ans = Integer.MAX_VALUE;

		int[][] arr = new int[N][M];
		for (int i = 0; i < N; i++) {
			arr[i] = board[i].clone();
		}

		func(arr, 0);

		System.out.println(ans);
	}

	static void func(int[][] arr, int depth) {
		if (depth == CCTVList.size()) {
			int cnt = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (arr[i][j] == 0) cnt++;
				}
			}
			ans = Math.min(ans, cnt);
			return;
		}
 
		CCTV now = CCTVList.get(depth);
		for (int dir : now.Ways) {
			int[][] tmp = new int[N][M];
			for (int i = 0; i < N; i++) tmp[i] = arr[i].clone();

			now.curDirection = dir;
			watching(tmp, now);
			func(tmp, depth + 1);
		}
	}

	static int watching(int[][] arr, CCTV cctv) {
		int count = 0;

		int direction = cctv.curDirection;

		if (cctv.type == 1) {
			count += counting(arr, cctv, direction);
		} else if (cctv.type == 2) {
			count += counting(arr, cctv, direction);

			direction += 2;

			count += counting(arr, cctv, direction);
		} else if (cctv.type == 3) {
			count += counting(arr, cctv, direction);

			direction = (direction + 1) % 4;

			count += counting(arr, cctv, direction);
		} else if (cctv.type == 4) {
			for (int i = 0; i < 3; i++) {
				count += counting(arr, cctv, direction);
				direction = (direction + 1) % 4;
			}
		} else {
			for (int i = 0; i < 4; i++) {
				count += counting(arr, cctv, direction);
				direction = (direction + 1) % 4;
			}
		}

		return count;
	}

	static int counting(int[][] arr, CCTV cctv, int direction) {
		int count = 0;
		int row = cctv.pos.row;
		int col = cctv.pos.col;

		if (direction == 0) {
			for (int i = col + 1; i < M; i++) {
				if (board[row][i] == 6) {
					break;
				}

				if (arr[row][i] == -1 || board[row][i] != 0) {
					continue;
				}

				arr[row][i] = -1;
				++count;
			}
		} else if (direction == 1) {
			for (int i = row + 1; i < N; i++) {
				if (board[i][col] == 6) {
					break;
				}

				if (arr[i][col] == -1 || board[i][col] != 0) {
					continue;
				}

				arr[i][col] = -1;
				++count;
			}
		} else if (direction == 2) {
			for (int i = col - 1; i >= 0; i--) {
				if (board[row][i] == 6) {
					break;
				}

				if (arr[row][i] == -1 || board[row][i] != 0) {
					continue;
				}

				arr[row][i] = -1;
				++count;
			}
		} else {
			for (int i = row - 1; i >= 0; i--) {
				if (board[i][col] == 6) {
					break;
				}

				if (arr[i][col] == -1 || board[i][col] != 0) {
					continue;
				}

				arr[i][col] = -1;
				++count;
			}
		}

		return count;
	}
}