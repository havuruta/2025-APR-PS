package Week1.chomingi;

import java.io.*;
import java.util.*;

public class B17472 {
    static class Point{
        int y;
        int x;

        Point(int y, int x){
            this.y = y;
            this.x = x;
        }
    }

    static class Node{
        int s;
        int e;
        int cost;

        Node(int s, int e, int cost){
            this.s = s;
            this.e = e;
            this.cost = cost;
        }
    }
    

    static int N,M;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        int[][] bridge = new int[N][M];

        for (int i = 0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j<M; j++){
                int now = Integer.parseInt(st.nextToken());
                if (now==1) bridge[i][j] = -1;
                else bridge[i][j] = 0;
            }
        }

        int[] dy = {0,0,-1,1};
        int[] dx = {-1,1,0,0};

        int island = 1;
        for (int i = 0; i<N; i++){
            for (int j = 0; j<M; j++){
                if (bridge[i][j] != -1) continue;
                Deque<Point> queue = new ArrayDeque<>();
                queue.add(new Point(i, j));
                bridge[i][j] = island;
                while (!queue.isEmpty()) {
                    Point now = queue.poll();

                    for (int d = 0; d<4; d++){
                        int ny = now.y + dy[d];
                        int nx = now.x + dx[d];

                        if (isValid(ny, nx) && bridge[ny][nx] == -1){
                            bridge[ny][nx] = island;
                            queue.add(new Point(ny, nx));
                        }
                    }
                }
                island++;
            }
        }
        int[][] graph = new int[island][island];

        for (int i = 0; i<island; i++) Arrays.fill(graph[i], Integer.MAX_VALUE);

        for (int i = 0; i<N; i++){
            int flag = -1;
            for (int j = 0; j<M; j++) {
                if (bridge[i][j] == 0) continue;
                if (flag == -1) flag = j;
                else if (flag-1 == j) flag = j;
                else {
                    int start = bridge[i][flag];
                    int end = bridge[i][j];
                    int v = j-flag-1;
                    flag = j;
                    if (v<2) continue;
                    if (graph[start][end]>v) graph[start][end] = v;
                    if (graph[end][start]>v) graph[end][start] = v;
                }
            }
        }

        for (int i = 0; i<M; i++){
            int flag = -1;
            for (int j = 0; j<N; j++) {
                if (bridge[j][i] == 0) continue;
                if (flag == -1) flag = j;
                else if (flag-1 == j) flag = j;
                else {
                    int start = bridge[flag][i];
                    int end = bridge[j][i];
                    int v = j-flag-1;
                    flag = j;
                    if (v<2) continue;
                    
                    if (graph[start][end]>v) graph[start][end] = v;
                    if (graph[end][start]>v) graph[end][start] = v;
                }
            }
        }

        for (int i =0; i<island; i++) graph[i][i] = Integer.MAX_VALUE;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(o -> o.cost));

        for (int i = 2; i<island; i++){
            if (graph[1][i] != Integer.MAX_VALUE) pq.add(new Node(1, i, graph[1][i]));
        }
        boolean[] visited = new boolean[island];
        visited[1] = true;

        int res = 0;

        while (!pq.isEmpty()) {
            Node now = pq.poll();
            if (visited[now.e]) continue;
            res+=now.cost;
            visited[now.e] = true;
            
            for (int n = 1; n<island; n++){
                if (!visited[n] && graph[now.e][n] != Integer.MAX_VALUE) pq.add(new Node(now.e, n, graph[now.e][n]));
            }
        }

        boolean isConnected = true;
        for (int i = 1; i<island; i++){
            if (visited[i] == false) isConnected = false;
        }

        System.out.println(isConnected == true ? res : -1);
    }
    
    static boolean isValid(int y, int x){
        return y>=0 && x>=0 && y<N && x<M;
    }
}
