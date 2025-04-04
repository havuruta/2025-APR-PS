package jaesung;

/*
 문제 : 13460(구슬 탈출 2)
 시간 : 1060ms
 풀이 : 이동 가능한 방향으로 두 구슬을 이동시키며 최소 횟수를 계산.
 
 ※ 자료구조에 객체 타입을 삽입할 땐 새로 생성해서 넣자...그렇지 않으면 원본 값이 변경된다...
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Pos{
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

class Move{
	Pos rPos, bPos;
	int direction, count;
	
	public Move(Pos rPos, Pos bPos, int direction, int count) {
		this.rPos = rPos;
		this.bPos = bPos;
		this.direction = direction;
		this.count = count;
	}

	@Override
	public String toString() {
		return "Move [rPos=" + rPos + ", bPos=" + bPos + ", direction=" + direction + ", count=" + count + "]";
	}
}

public class B13460 {
    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		// 우 상 좌 하
		int[] dRow = {0, -1, 0, 1};
		int[] dCol = {1, 0, -1, 0};
		
		// 각 구슬 초기 위치
		Pos rPosStart = null;
		Pos bPosStart = null;
		
		// 구멍 위치
		Pos hPos = null;
		
		char[][] board = new char[N][M];
		
		for (int i = 0; i < N; i++) {
			String line = br.readLine();
			
			for (int j = 0; j < M; j++) {
				board[i][j] = line.charAt(j);
				
				if (board[i][j] == 'R') {
					rPosStart = new Pos(i, j);
				}
				else if (board[i][j] == 'B') {
					bPosStart = new Pos(i, j);
				}
				else if (board[i][j] == 'O') {
					hPos = new Pos(i, j);
				}
			}
		}
		
		Queue<Move> q = new LinkedList<>();
		
		// R이 이동 가능한 방향 큐에 추가
		for (int dir = 0; dir < 4; dir++) {
			int nRow = rPosStart.row + dRow[dir];
			int nCol = rPosStart.col + dCol[dir];
			
			if (nRow < 0 || nRow >= N || nCol < 0 || nCol >= M) {
				continue;
			}
			
			if (board[nRow][nCol] == '#' || board[nRow][nCol] == 'B') {
				continue;
			}
			
			q.add(new Move(new Pos(rPosStart.row, rPosStart.col), new Pos(bPosStart.row, bPosStart.col), dir, 1));
		}
		
		// B가 이동 가능한 방향 큐에 추가
		for (int dir = 0; dir < 4; dir++) {
			int nRow = bPosStart.row + dRow[dir];
			int nCol = bPosStart.col + dCol[dir];
			
			if (nRow < 0 || nRow >= N || nCol < 0 || nCol >= M) {
				continue;
			}
			
			if (board[nRow][nCol] == '#' || board[nRow][nCol] == 'R') {
				continue;
			}
			
			q.add(new Move(new Pos(rPosStart.row, rPosStart.col), new Pos(bPosStart.row, bPosStart.col), dir, 1));
		}
		
		int ans = Integer.MAX_VALUE;
		
		// bfs
		bfs:
		while (!q.isEmpty()) {
			Move cur = q.poll();
			
			// 해당 방향으로 쭉 이동			
			while (true) {
				boolean isMove = false;
				
				int nRow = cur.rPos.row + dRow[cur.direction];
				int nCol = cur.rPos.col + dCol[cur.direction];
								
				// R 이동
				// 탈출 하지 않았으면 현재 방향으로 이동
				if (board[cur.rPos.row][cur.rPos.col] != 'O') {
					// 벽이 아니면서 이동할 위치가 현재 B 위치가 아닌 경우
					if (board[nRow][nCol] != '#' && (nRow != cur.bPos.row || nCol != cur.bPos.col)) {						
						cur.rPos.row = nRow;
						cur.rPos.col = nCol;
						isMove = true;
					}
				}
				
				// B 이동
				// 탈출 하지 않았으면
				nRow = cur.bPos.row + dRow[cur.direction];
				nCol = cur.bPos.col + dCol[cur.direction];
				
				// 어떤 경우에도 파란색이 먼저 탈출하면 패스
				if (board[nRow][nCol] == 'O') {
					continue bfs;
				}
				
				// 벽이 아니면서 이동할 위치가 현재 R 위치가 아닌 경우
				if (board[nRow][nCol] != '#' && (nRow != cur.rPos.row || nCol != cur.rPos.col)) {					
					cur.bPos.row = nRow;
					cur.bPos.col = nCol;
					isMove = true;
				}

				// 더이상 이동할 수 없는 경우
				if (!isMove) {
					// 빨간 구슬 탈출한 경우
					if (cur.rPos.row == hPos.row && cur.rPos.col == hPos.col) {
						ans = Math.min(ans, cur.count);
						continue bfs;
					}
					
					break;
				}
			}
			
			if (cur.count + 1 > 10) {
				continue;
			}
			
			// 새로운 방향으로 이동
			// R이 이동 가능한 방향 큐에 추가
				for (int dir = 0; dir < 4; dir++) {
					int nRow = cur.rPos.row + dRow[dir];
					int nCol = cur.rPos.col + dCol[dir];
					
					if (nRow < 0 || nRow >= N || nCol < 0 || nCol >= M) {
						continue;
					}
					
					if (board[nRow][nCol] == '#' || (nRow == cur.bPos.row && nCol == cur.bPos.col)) {
						continue;
					}
					
					q.add(new Move(new Pos(cur.rPos.row, cur.rPos.col), new Pos(cur.bPos.row, cur.bPos.col), dir, cur.count + 1));
				}
				
				// B가 이동 가능한 방향 큐에 추가
				for (int dir = 0; dir < 4; dir++) {
					int nRow = cur.bPos.row + dRow[dir];
					int nCol = cur.bPos.col + dCol[dir];
					
					if (nRow < 0 || nRow >= N || nCol < 0 || nCol >= M) {
						continue;
					}

					if (board[nRow][nCol] == '#' || (nRow == cur.rPos.row && nCol == cur.rPos.col)) {
						continue;
					}

					q.add(new Move(new Pos(cur.rPos.row, cur.rPos.col), new Pos(cur.bPos.row, cur.bPos.col), dir, cur.count + 1));
				}
		}
		
		if (ans > 10) {
			System.out.println(-1);
		}
		else {
			System.out.println(ans);
		}
		
	}
}