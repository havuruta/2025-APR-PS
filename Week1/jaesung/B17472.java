package jaesung;

/*
 문제 : 17472(다리 만들기 2)
 시간 : 108ms
 풀이 : 섬을 파악한 후 각 섬에서 단방향으로 다른 섬에 도달하는 간선을 생성한 뒤, 최소 신장 트리를 구성하여 최솟값 계산
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

class Pos {
	int row, col;
	
	public Pos(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public String toString() {
		return "Pos [row=" + row + ", col=" + col + "]";
	}
}

class Bridge implements Comparable<Bridge>{
	int start, end, length;
	
	public Bridge(int start, int end, int length) {
		this.start = start;
		this.end = end;
		this.length = length;
	}

	@Override
	public int compareTo(Bridge o) {
		return this.length - o.length;
	}

	@Override
	public String toString() {
		return "Bridge [start=" + start + ", end=" + end + ", length=" + length + "]";
	}
}

public class B17472 {
	static int[] p;

    public static void main(String[] args) throws IOException {
		// 빠른 입력 사용.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][M];
		List<List<Pos>> islandList = new ArrayList<>();
		
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		boolean[][] visited = new boolean[N][M];
		int[] dRow = {0, -1, 0, 1};
		int[] dCol = {1, 0, -1, 0};
		
		// 섬(노드)을 파악
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				
				// 섬인 경우
				if (map[i][j] == 1) {
					if (visited[i][j]) continue;
					
					List<Pos> isLand = new ArrayList<>();
					
					// bfs를 수행하여 인접한 땅 좌표를 모두 저장
					Queue<Pos> q = new LinkedList<>();
					
					q.add(new Pos(i, j));
					visited[i][j] = true;
					
					while (!q.isEmpty()) {
						Pos cur = q.poll();
						
						isLand.add(cur);
						
						for (int dir = 0; dir < 4; dir++) {
							int nRow = cur.row + dRow[dir];
							int nCol = cur.col + dCol[dir];
							
							if (nRow < 0 || nRow >= N || nCol < 0 || nCol >= M) continue;
							
							if (map[nRow][nCol] != 1 || visited[nRow][nCol]) continue;
							
							visited[nRow][nCol] = true;
							q.add(new Pos(nRow, nCol));
						}
					}
					
					// bfs 종료 후 섬 리스트에 저장
					islandList.add(isLand);
				}
			}
		}
		
		PriorityQueue<Bridge> bridgePQ = new PriorityQueue<>();
		
		// 각 섬에서 단방향 bfs를 수행하여 다리(간선) 후보 생성
		for (int i = 0; i < islandList.size(); i++) {
			// 중복 간선 생성을 막기 위해 i+1번 섬부터 다리 생성
			for (int j = i+1; j < islandList.size(); j++) {
				
				// 각 섬의 모든 땅에서 놓을 수 있는 다리를 생성하고 리스트에 저장
				for (Pos start : islandList.get(i)) {
					find:
					for (Pos end : islandList.get(j)) {
						// 땅이 같은 행애 위치한 경우
						if (start.row == end.row) {
							// 다리를 놓을 방향에 인접한 땅이 위치한다면 패스
							int left = 0;
							int right = 0;
							if (start.col > end.col) { // i가 오른쪽
								left = end.col + 1;
								right = start.col;

							}
							else { // i가 왼쪽
								left = start.col + 1;
								right = end.col;
							}
							
							for (int dir = left; dir < right; dir++) {
								if (map[start.row][dir] == 1) {
									continue find;
								}
							}
							
							int length = Math.abs(start.col - end.col)-1;
							// 다리 길이는 2 이상
							if (length >= 2) {
								bridgePQ.add(new Bridge(i, j, length));
							}
						}
						// 땅이 같은 열에 위치한 경우
						else if (start.col == end.col) {
							// 다리를 놓을 방향에 인접한 땅이 위치한다면 패스
							int up = 0;
							int bottom = 0;
							if (start.row < end.row) { // i가 위
								up = start.row + 1;
								bottom = end.row;

							}
							else { // i가 아래
								up = end.row + 1;
								bottom = start.row;
							}
							
							for (int dir = up; dir < bottom; dir++) {
								if (map[dir][start.col] == 1) {
									continue find;
								}
							}
							
							int length = Math.abs(start.row - end.row)-1;
							// 다리 길이는 2 이상
							if (length >= 2) {
								bridgePQ.add(new Bridge(i, j, length));
							}
						}
					}
				}
			}
		}
		
		// 다리 리스트로부터 각 섬을 연결하는 최소신장트리 생성
		int islandCount = islandList.size();

		p = new int[islandCount];
		for (int i = 0; i < p.length; i++) {
			p[i] = -1;
		}

		int count = 0;
		int ans = 0;
		while (count < islandCount-1) {
			if (bridgePQ.isEmpty()) {
				break;
			}
			
			Bridge cur = bridgePQ.poll();
						
			if (find(cur.start) == find(cur.end)) {
				continue;
			}
			
			union(cur.start, cur.end);
			
			ans += cur.length;
			++count;
		}

		if (count == islandCount-1) {
			System.out.println(ans);
		}
		else {
			System.out.println(-1);
		}
	}
	
	static int find(int x) {
		if (p[x] < 0) {
			return x;
		}
		
		return p[x] = find(p[x]);
	}
	
	static void union(int u, int v) {
		u = find(u);
		v = find(v);
		
		if (u == v) {
			return;
		}
		
		if (p[u] == p[v]) {
			--p[u];
		}
		
		if (p[u] < p[v]) {
			p[v] = u;
		}
		else {
			p[u] = v;
		}
	}
}