package changmin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

/* 
 * 구슬이 어떻게 움직여야할까? 경로를 찾고 -> 굴리고 -> 공 위치 확인(파란공과 동시에 빠지거나,파란공이 먼저 빠지면 X) ->
 * 카운트세기 반복인듯? 언제까지 기울이냐? 구슬이 움직이지 않을경우 빨간공이 구멍에 먼저 빠져도, 파란공이 아직 움직일 수 있으면 계속
 * 기울어진 상태라는 뜻
 * 
 * 꼭 경로를 찾아야할까???? -> 그냥 백트래킹으로 완탐을 해야하는거 아닐까???? 
 * 그러면 방금전에 왔던길만 갈수있는 다른 방향으로 가는식으로?? -> 방금 어떤 방향으로 움직였는지를 boolean배열로 매번 바꿔가면서 넘겨주면(이라고 생각하고 그냥 인자값으로 넘겨줌)
 * 백트래킹을 할때 어떤값을 들고 이동해야할까? (방문처리, 깊이, 위치,
 *  
 *  
 * 빨간공 파란공위치에따라서 먼저 움직이는것도 고민해봐야함 (공은 겹칠 수 없기 때문) -> 언제 이런게 필요할까? -> 같은 축에 있을 때
 * 
 * 만약 백트래킹을 한다면 depth(움직인 횟수가 10초과면 돌아가야함) 10이상은 -1 임
 * 
 * 배열 범위 밖으로 나가는건 고려하지 않아도 괜찮음, 테두리에 #으로 벽이 있으니 벽을 만나면 멈추면 됨
 */
public class Main {

	static int[] dr = { -1, 1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };

	static int N, M; // N세로 , M가로
	static char[][] map;

	static int recentDir;
	static int[] red, blue, hole; // 빨간공 파란공 위치 //근데 구멍의 위치를 저장할 필요가 있을까? 짜피 공이 움직일때 알아서 찾는데 //흠 백트래킹할때도 그냥 인자로
									// 넣어봐 일단
	static int ans;

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		recentDir = -1; // 처음엔 -1
		ans = Integer.MAX_VALUE;

		map = new char[N][M];

		for (int i = 0; i < N; i++) {
			String input = br.readLine();
			for (int j = 0; j < M; j++) {
				map[i][j] = input.charAt(j);
				if (map[i][j] == 'B') {
					blue = new int[] { i, j };
				} else if (map[i][j] == 'R') {
					red = new int[] { i, j };
				} else if (map[i][j] == 'O') {
					hole = new int[] { i, j };
				}
			}
		} //여기까지 입력

		bt(-1, 0, red, blue);

		if (ans == Integer.MAX_VALUE) {
			bw.write(-1 + "");
		} else {
			bw.write(ans + "");
		}

		bw.flush();
		bw.close();
		br.close();
	}

	public static void bt(int recentDir, int depth, int[] red, int[] blue) {

		if (depth > 10) { //깊이 10 초과시 실패로 처리
			return;
		}

		if (blue[0] == -1) {
			return; // 파란공이 빠지면 실패
		}
		if (red[0] == -1) { //빨간공만 빠졌다면 값 갱신
			ans = Math.min(ans, depth);
			return;
		}

		char[][] tmpMap = new char[N][M]; //백트래킹 후 복원용 맵
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				tmpMap[i][j] = map[i][j];
			}
		}

		for (int i = 0; i < 4; i++) { // 사방으로 굴려잇
			if (i == recentDir) { //같은방향 연속으로 기울이는 경우 방지 + (반대 방향으로 기울이는 것도 방지하면 더 좋은듯?)
				continue;
			}
			int[] newRed, newBlue;

			if (isRedFirst(red, blue, i)) {
				// 기존 공 위치 초기화 (공이 이동하므로 . 처리)
			    if (red[0] != -1) map[red[0]][red[1]] = '.';
			    if (blue[0] != -1) map[blue[0]][blue[1]] = '.';

			    newRed = redMove(red, i);
			    // 공을 이동시키고, 구멍에 빠지지 않았을 경우만 맵에 위치 표시
			    if (newRed[0] != -1) map[newRed[0]][newRed[1]] = 'R';

			    newBlue = blueMove(blue, i);
			    // 공을 이동시키고, 구멍에 빠지지 않았을 경우만 맵에 위치 표시
			    if (newBlue[0] != -1) map[newBlue[0]][newBlue[1]] = 'B';

			} else {
			    if (red[0] != -1) map[red[0]][red[1]] = '.';
			    if (blue[0] != -1) map[blue[0]][blue[1]] = '.';

			    newBlue = blueMove(blue, i);
			    if (newBlue[0] != -1) map[newBlue[0]][newBlue[1]] = 'B';

			    newRed = redMove(red, i);
			    if (newRed[0] != -1) map[newRed[0]][newRed[1]] = 'R';
			}

			if (red[0] == newRed[0] && red[1] == newRed[1] && blue[0] == newBlue[0] && blue[1] == newBlue[1]) {
				continue;
			} //공이 그대로인 경우 넘어가


			bt(i, depth + 1, newRed, newBlue);
			
			// 백트래킹 후 원래 맵 상태로 복원
			for (int k = 0; k < N; k++) {
				for (int j = 0; j < M; j++) {
					map[k][j] = tmpMap[k][j];
				}
			}

		}

	}
	
	//공이 움직일때, 같은 자리에 가는것을 방지하기위해 한개씩 움직여야하는데, 먼저 움직여야하는공을 판단하기 위함
	public static boolean isRedFirst(int[] red, int[] blue, int dir) {

		if (dir == 0) {
			if (red[0] < blue[0]) {
				return true;
			}
		} else if (dir == 1) {
			if (red[0] > blue[0]) {
				return true;
			}
		} else if (dir == 2) {
			if (red[1] < blue[1]) {
				return true;
			}
		} else if (dir == 3) {
			if (red[1] > blue[1]) {
				return true;
			}
		}

		return false;
	}
	
	//파란공 움직이기
	public static int[] blueMove(int[] pos, int dir) { // 한쪽방향으로 계속 가고, 구멍에 빠지면 or 벽이거나 다른공이면 더이상 움직이지 않음

		int r = pos[0];
		int c = pos[1];

		while (true) {
			int nr = r + dr[dir];
			int nc = c + dc[dir];

			if (map[nr][nc] == 'O') {
				return new int[] { -1, -1 }; // 구멍에 들어간 경우
			}

			if (map[nr][nc] == '#' || map[nr][nc] == 'R') { // 새로 온 위치가 벽이거나 공이면 멈춰
				return new int[] { r, c };
			}

			r = nr;
			c = nc;
		}
	}
	
	//빨간공 움직이기
	public static int[] redMove(int[] pos, int dir) {
		int r = pos[0];
		int c = pos[1];

		while (true) {
			int nr = r + dr[dir];
			int nc = c + dc[dir];

			if (map[nr][nc] == 'O') {
				return new int[] { -1, -1 }; // 구멍에 들어간 경우
			}

			if (map[nr][nc] == '#' || map[nr][nc] == 'B') { // 새로 온 위치가 벽이거나 공이면 멈춰
				return new int[] { r, c };
			}

			r = nr;
			c = nc;
		}
	}
}
