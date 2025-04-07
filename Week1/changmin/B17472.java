package changmin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class B17472 {

	static class Bridge implements Comparable<Bridge> {
		int num1, num2, length; // 섬의 번호, 다리의 길이

		public Bridge(int num1, int num2, int length) {
			this.num1 = num1;
			this.num2 = num2;
			this.length = length;
		}

		@Override
		public int compareTo(Bridge o) {

			return this.length - o.length;
		}

	}

	static int[] dr = { -1, 1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };

	static int N, M; // 세로, 가로
	static int[][] map; // 지도 정보
	static boolean[][] island; // 섬이면 true;
	static List<Bridge> bridges; // 모든 건설할 수 있는 다리의 정보
	static int landCount; // 섬의 개수

	static int dir; // 다리 찾을때방향
	static int startNum; // 다리 찾을때 시작 섬 번호

	static int[] parents; // 어떤 집합에 있는지
	static int[] ranks; //

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		map = new int[N][M];
		island = new boolean[N][M];
		bridges = new ArrayList<>();

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if (map[i][j] == 1) { // 섬일경우 true처리
					island[i][j] = true;
				}
			}
		} //입력 끝

		// 지금 섬이 모두 1로 표현되어 있어, 어떤 섬인지 구별을 할 수 없음
		// 섬을 구별할 수 있게 만들어 줘야함

		boolean[][] copyLand = copyArr(island);

		int islandNum = 0; // 섬마다 번호 부여
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] != 0 && copyLand[i][j]) {
					islandNum++;
					nameIsland(islandNum, i, j, copyLand);
				}
			}
		}

		landCount = islandNum; // 섬의 개수

		parents = new int[landCount + 1];
		ranks = new int[landCount + 1];

		// 이 각 섬에서 다른 섬으로 갈 수 있는 다리들을 찾아야함
		// 다리는 중간에 방향을 바꿀 수 없고, 길이가 2이상이여야함
		// 각 섬에서 건설 가능한 다리를 모두 찾기
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] != 0) { // 바다가 아닐
					startNum = map[i][j];

					for (int k = 0; k < 4; k++) {
						dir = k;
						int nr = i + dr[dir];
						int nc = j + dc[dir];
						if (nr >= 0 && nc >= 0 && nr < N && nc < M && map[nr][nc] == 0) {
							findBridge(new int[] { nr, nc }, 0);
						}
					}
				}
			}
		}

		// 모든 다리들을 얻었으면, 섬의 개수 - 1 개만큼 다리를 지어야함
		// MST(최소 신장 트리) 구성을 위한 Union-Find 초기화
		for (int i = 1; i <= landCount; i++) { 
			makeSet(i);
			ranks[i] = 0; // 초기화 근데 꼭 해야함? 차이 없는듯 그냥, 명시적으로 작성한걸로..
		}
		
		//다리 길이 기준으로 정렬(짧은 다리부터)
		Collections.sort(bridges);

		int count = 0;
		int totalCost = 0;
		
		//크루스칼 알고리즘으로 최소 다리 길이 갱신
		for (Bridge b : bridges) {
			int root1 = findSet(b.num1);
			int root2 = findSet(b.num2);

			if (root1 != root2) {
				union(root1, root2);
				totalCost += b.length;
				count++;
			}

			if (count == landCount - 1)
				break;
		}

		if (count == landCount - 1) { //완성한경우
			bw.write(String.valueOf(totalCost));
		} else { //불가능한경우
			bw.write("-1");
		}
		bw.flush();
		bw.close();
		br.close();
	}

	public static void findBridge(int[] position, int length) {
		int x = position[0];
		int y = position[1];

		// 만약 다른 섬을 만났는데(0이 아닌) startNum과 다름
		if (map[x][y] != 0 && map[x][y] != startNum) {
			// 지나온 바다가 2칸 이상이었다면 다리 등록
			if (length >= 2) {
				bridges.add(new Bridge(startNum, map[x][y], length));
			}
			return; // 더 이상 진행하지 않고 끝냄
		}

		// 여기까지 왔다는 것은 아직 다른 섬을 만나지 못했다는 뜻
		// 그런데 만약 map[x][y]가 '현재 출발 섬 번호'라면(즉, 바다가 아님) 탈출
		// => (같은 섬을 다시 만난 거라면 다리 불가능)
		if (map[x][y] == startNum) {
			return;
		}

		// map[x][y]가 0(바다)이면 길이를 1 늘려서 직선 진행
		int nx = x + dr[dir];
		int ny = y + dc[dir];
		if (nx >= 0 && ny >= 0 && nx < N && ny < M) {
			findBridge(new int[] { nx, ny }, length + 1);
		}
	}

	public static void makeSet(int num) {
		parents[num] = num;
	}

	public static int findSet(int num) {
		if (parents[num] != num) {
			parents[num] = findSet(parents[num]);
		}

		return parents[num];
	}

	public static void union(int num1, int num2) {

		int root1 = findSet(num1);
		int root2 = findSet(num2);

		if (ranks[root1] < ranks[root2]) {
			parents[root1] = root2;
		} else {
			parents[root2] = root1;
			if (ranks[root1] == ranks[root2]) {
				ranks[root1]++;
			}
		}
	}
	
	//BFS로 섬에 번호 달아줌
	public static void nameIsland(int num, int x, int y, boolean[][] arr) {
		Queue<int[]> que = new LinkedList<>();
		map[x][y] = num;
		arr[x][y] = false;
		que.offer(new int[] { x, y });

		while (!que.isEmpty()) {
			int[] pos = que.poll();

			for (int i = 0; i < 4; i++) {
				int nr = pos[0] + dr[i];
				int nc = pos[1] + dc[i];

				if (nr >= 0 && nc >= 0 && nr < N && nc < M && arr[nr][nc]) {
					arr[nr][nc] = false;
					map[nr][nc] = num;
					que.offer(new int[] { nr, nc });
				}
			}
		}
	}

	public static boolean[][] copyArr(boolean[][] arr) {
		boolean[][] copy = new boolean[N][M];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				copy[i][j] = arr[i][j];
			}
		}

		return copy;
	}
}
