package gyumin;

import java.io.*;
import java.util.*;

public class B13460 {
    static int N, M;
    static char[][] map;
    static boolean[][][][] visited;
    static int holeX, holeY;
    static int[] dx = {-1, 1, 0, 0}; // 상하좌우
    static int[] dy = {0, 0, -1, 1};

    static class Marble {
        int rx, ry, bx, by, cnt;
        
        Marble(int rx, int ry, int bx, int by, int cnt) {
            this.rx = rx;
            this.ry = ry;
            this.bx = bx;
            this.by = by;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new char[N][M];
        visited = new boolean[N][M][N][M];

        int rx = 0, ry = 0, bx = 0, by = 0;

        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = line.charAt(j);
                if (map[i][j] == 'O') {
                    holeX = i;
                    holeY = j;
                } else if (map[i][j] == 'R') {
                    rx = i;
                    ry = j;
                    map[i][j] = '.';
                } else if (map[i][j] == 'B') {
                    bx = i;
                    by = j;
                    map[i][j] = '.';
                }
            }
        }

        System.out.println(bfs(rx, ry, bx, by));
        br.close();
    }

    private static int bfs(int rx, int ry, int bx, int by) {
        Queue<Marble> queue = new LinkedList<>();
        queue.add(new Marble(rx, ry, bx, by, 0));
        visited[rx][ry][bx][by] = true;

        while (!queue.isEmpty()) {
            Marble cur = queue.poll();
            
            if (cur.cnt >= 10) continue;  // 10회 넘으면 이 경우는 스킵

            for (int i = 0; i < 4; i++) {
                int nRx = cur.rx;
                int nRy = cur.ry;
                int nBx = cur.bx;
                int nBy = cur.by;

                boolean isRedHole = false;
                boolean isBlueHole = false;

                // 파란 구슬 이동
                while (map[nBx + dx[i]][nBy + dy[i]] != '#') {
                    nBx += dx[i];
                    nBy += dy[i];
                    if (nBx == holeX && nBy == holeY) {
                        isBlueHole = true;
                        break;
                    }
                }

                // 빨간 구슬 이동
                while (map[nRx + dx[i]][nRy + dy[i]] != '#') {
                    nRx += dx[i];
                    nRy += dy[i];
                    if (nRx == holeX && nRy == holeY) {
                        isRedHole = true;
                        break;
                    }
                }

                if (isBlueHole) continue; // 파란 구슬이 구멍에 빠지면 실패
                if (isRedHole) return cur.cnt + 1; // 빨간 구슬만 구멍에 빠지면 성공

                // 구슬 충돌 처리
                if (nRx == nBx && nRy == nBy && !isRedHole) {
                    // 이동 거리가 더 긴 구슬을 한 칸 뒤로
                    int redDist = Math.abs(nRx - cur.rx) + Math.abs(nRy - cur.ry);
                    int blueDist = Math.abs(nBx - cur.bx) + Math.abs(nBy - cur.by);

                    if (redDist > blueDist) {
                        nRx -= dx[i];
                        nRy -= dy[i];
                    } else {
                        nBx -= dx[i];
                        nBy -= dy[i];
                    }
                }

                // 방문하지 않은 새로운 상태면 큐에 추가
                if (!visited[nRx][nRy][nBx][nBy]) {
                    visited[nRx][nRy][nBx][nBy] = true;
                    queue.add(new Marble(nRx, nRy, nBx, nBy, cur.cnt + 1));
                }
            }
        }
        
        return -1;  // 모든 경우를 봤는데도 성공 못한 경우
    }
}