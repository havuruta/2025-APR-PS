import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class B17472 {
    // 17472 - 다리 만들기 2
    // 각 섬을 연결한 다리의 최솟값 구하기

    // 섬이 노드인 mst
    static class bridge implements Comparable<bridge> {
        int from, to, length;

        public bridge(int from, int to, int length) {
            this.from = from;
            this.to = to;
            this.length = length;
        }

        @Override
        public int compareTo(bridge o) {
            return this.length - o.length;
        }
    }

    static int[] parents;
    static int[][] map;
    static int n, m;
    static int[] dy = { 1, -1, 0, 0 };
    static int[] dx = { 0, 0, 1, -1 };
    static List<bridge> list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        boolean[][] visited = new boolean[n][m];
        int num = 2; // 1로 해보니 고려해야할 처리가 여러개라서 2부터
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] == 1 && !visited[i][j]) {
                    bfs(i, j, num, visited);
                    num++;
                }
            }
        }

        build();

        parents = new int[num];
        for (int i = 2; i < num; i++) {
            parents[i] = i;
        }
        Collections.sort(list);

        int total = 0;
        int count = 0;
        for (bridge b : list) {// 모든 다리 연결
            if (union(b.from, b.to)) {
                total += b.length;
                count++;
            }
        }

        if (count == num - 3) {
            System.out.println(total);
        } else {
            System.out.println(-1);
        }

    }

    static void bfs(int i, int j, int num, boolean[][] visited) {
        // 하나의 섬에 같은 번호로 칠하기
        Queue<int[]> que = new ArrayDeque<>();
        que.add(new int[] { i, j });
        map[i][j] = num;
        visited[i][j] = true;
        while (!que.isEmpty()) {
            int[] tmp = que.poll();
            int y = tmp[0];
            int x = tmp[1];
            for (int k = 0; k < 4; k++) {
                int ny = y + dy[k];
                int nx = x + dx[k];
                if (ny >= 0 && ny < n && nx >= 0 && nx < m && !visited[ny][nx] && map[ny][nx] == 1) {
                    visited[ny][nx] = true;
                    map[ny][nx] = num;
                    que.add(new int[] { ny, nx });
                }
            }
        }
    }

    // mst find
    static int find(int a) {
        if (parents[a] != a)
            parents[a] = find(parents[a]);
        return parents[a];
    }

    // mst union
    static boolean union(int a, int b) {
        int pa = find(a);
        int pb = find(b);
        if (pa == pb)
            return false;
        parents[pa] = pb;
        return true;
    }

    static void build() {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                if (map[y][x] > 1) {
                    int from = map[y][x];
                    for (int d = 0; d < 4; d++) {
                        int ny = y;
                        int nx = x;
                        int len = 0;
                        while (true) {
                            ny += dy[d];
                            nx += dx[d];
                            if (ny < 0 || ny >= n || nx < 0 || nx >= m)
                                break;
                            if (map[ny][nx] == from)
                                break;
                            if (map[ny][nx] == 0) {
                                len++;
                                continue;
                            } else {// 다른 땅을 만난 경우
                                if (len >= 2) {
                                    int to = map[ny][nx];
                                    list.add(new bridge(from, to, len));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}