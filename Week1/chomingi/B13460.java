package Week1.chomingi;

import java.io.*;
import java.util.*;

public class B13460{
    static int N,M, redY, redX, blueY, blueX;
    static int[] dy = {0,0,-1,1};
    static int[] dx = {-1,1,0,0};
    static int[][] map;
    static boolean isOver;

    static class MoveResult {
        int y, x, dist;
        boolean isInHole;
    
        MoveResult(int y, int x, int dist, boolean isInHole) {
            this.y = y;
            this.x = x;
            this.dist = dist;
            this.isInHole = isInHole;
        }
    }
    
    static MoveResult move(int y, int x, int dir) {
        int dist = 0;
        while (true) {
            int ny = y + dy[dir];
            int nx = x + dx[dir];
            if (map[ny][nx] == -1) break; // 벽
            if (map[ny][nx] == 1) return new MoveResult(ny, nx, dist + 1, true); // 구멍
            y = ny;
            x = nx;
            dist++;
        }
        return new MoveResult(y, x, dist, false);
    }

    static class State {
        int ry, rx, by, bx, depth;
    
        State(int ry, int rx, int by, int bx, int depth) {
            this.ry = ry;
            this.rx = rx;
            this.by = by;
            this.bx = bx;
            this.depth = depth;
        }
    }
    
    static int bfs() {
        boolean[][][][] visited = new boolean[N][M][N][M];
        Queue<State> q = new LinkedList<>();
        q.offer(new State(redY, redX, blueY, blueX, 0));
        visited[redY][redX][blueY][blueX] = true;
    
        while (!q.isEmpty()) {
            State cur = q.poll();
    
            if (cur.depth >= 10) return -1;
    
            for (int dir = 0; dir < 4; dir++) {
                MoveResult red = move(cur.ry, cur.rx, dir);
                MoveResult blue = move(cur.by, cur.bx, dir);
    
                if (blue.isInHole) continue; // 파란 구슬이 빠지면 무효
                if (red.isInHole) return cur.depth + 1; // 빨간 구슬만 빠지면 정답
    
                // 겹친 위치 조정
                if (red.y == blue.y && red.x == blue.x) {
                    if (red.dist > blue.dist) {
                        red.y -= dy[dir];
                        red.x -= dx[dir];
                    } else {
                        blue.y -= dy[dir];
                        blue.x -= dx[dir];
                    }
                }
    
                if (!visited[red.y][red.x][blue.y][blue.x]) {
                    visited[red.y][red.x][blue.y][blue.x] = true;
                    q.offer(new State(red.y, red.x, blue.y, blue.x, cur.depth + 1));
                }
            }
        }
    
        return -1;
    }
    
    

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        redY = -1;
        redX = -1;

        blueY = -1;
        blueX = -1;

        map = new int[N][M];

        for (int i = 0; i<N; i++){
            String line = br.readLine();
            for (int j = 0; j<M; j++){
                char now = line.charAt(j);
                switch (now) {
                    case '#':
                        map[i][j] = -1;
                        break;
                    case '.':
                        map[i][j] = 0;
                        break;
                    case 'O':
                        map[i][j] = 1;
                        break;
                    case 'R':
                        map[i][j] = 0;
                        redY = i;
                        redX = j;
                        break;
                    case 'B':
                        map[i][j] = 0;
                        blueY = i;
                        blueX = j;
                        break;
                    default:
                        break;
                }
            }
        }
        
        System.out.println(bfs());

    }

}