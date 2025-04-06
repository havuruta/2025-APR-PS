package gyumin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class B17472 {
	static final int INF = 987654321;
	static int N;
	static int M;
	static int[][] map;
	static int[][] recode;
	static int[] dr = { 1, -1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };
	static int marker = -1;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		map = new int[N][];

		for (int i = 0; i < N; i++) {
			map[i] = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
		}

		recode = new int[6 + 1][6 + 1]; // 섬은 최대 6개니까 일단 만들어놓고 생각하기

		for (int i = 1; i <= 6; i++) {
			Arrays.fill(recode[i], INF);
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] == 1) {
					dfs(i, j);
					marker--;
				}
			}
		}

		for (int r = 0; r < N; r++) {
			for (int c = 0; c < M; c++) {
				if (map[r][c] < 0) {
					for (int d = 0; d < 4; d++) {
						int newR = r + dr[d];
						int newC = c + dc[d];

						if (newR >= 0 && newR < N && newC >= 0 && newC < M) {
							if (map[newR][newC] == 0)
								dist(r, c, d);// 바다다면 dist를
						}
					}
				}
			}
		}

		int islandCnt = (int) Math.abs(marker);
		int res = 0;
		boolean[] visit = new boolean[islandCnt]; // 어차피 1부터 할꺼라 1을 더할 거
		visit[1] = true;

		PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[1], o2[1]));

		for (int i = 1; i < islandCnt; i++) {
			if (recode[1][i] != INF)
				pq.add(new int[] { i, recode[1][i] });
		}

		int cnt = 1;

		while (!pq.isEmpty() && cnt < islandCnt) {
			int[] curr = pq.poll();

			if (visit[curr[0]])
				continue;

			visit[curr[0]] = true;
			res += curr[1];
			cnt++;

			for (int i = 1; i < islandCnt; i++) {
				if (recode[curr[0]][i] != INF) {
					pq.add(new int[] { i, recode[curr[0]][i] });
				}
			}

		} // 프림

			if (cnt != islandCnt - 1)
				res = -1; // 어 사실 MST 완성 못했는데?

		bw.write(res + "");
		bw.flush();

		br.close();
		bw.close();

	} // main

	// 섬 주위를 탐방
	private static void dfs(int r, int c) {
		map[r][c] = marker;

		for (int d = 0; d < 4; d++) {
			int newR = r + dr[d];
			int newC = c + dc[d];

			if (newR >= 0 && newR < N && newC >= 0 && newC < M) {
				if (map[newR][newC] == 1)
					dfs(newR, newC); // 섬이면 dfs를
			}
		}
	} // dfs

	// 바다를 봤으면 그 뱡항 고정하고 뛰어가서 거리를 재와라
	private static void dist(int r, int c, int idx) {
		int start = map[r][c];
		int curr = start;
		int newR = r + dr[idx];
		int newC = c + dc[idx];
		int temp = 0;

		while (true) {
			curr = map[newR][newC];

			if (curr < 0) {
				// 섬에 왔는데
				if (temp <= 1) {
					break;
				} // 다리 길이가 1이면 그냥 부숴
				if (start < 0) {
					recode[(int) Math.abs(start)][(int) Math.abs(curr)] = Math
							.min(recode[(int) Math.abs(start)][(int) Math.abs(curr)], temp);
					recode[(int) Math.abs(curr)][(int) Math.abs(start)] = Math
							.min(recode[(int) Math.abs(curr)][(int) Math.abs(start)], temp);
					break;
				} // 출발점에 번호가 잡혀있으면
			}

			newR += dr[idx];
			newC += dc[idx];

			if (newR < 0 || newR >= N || newC < 0 || newC >= M) {
				break;
			} // 한방향으로 쭉 뛰었는데 섬 없으면 의미 없음

			temp++;
		}

	} // dist

}
