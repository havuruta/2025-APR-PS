package Baekjook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

/*
 * N개의 세로선, M개의 가로선
 * 두 가로선이 연속하면 안됨
 * 가로선을 추가해서 게임 결과 조작
 * i번 세로선에서 출발하면 i번에 도착
 * 
 * 입력
 * N M H(가로선을 놓을 수 있는 위치의 수)
 * a b(b번과 b + 1번 연결)
 * 
 * 출력
 * 추가해야 하는 가로선 개수의 최솟값
 * 정답이 3보다 크거나 불가능하면 -1
 */

public class B15684 {
    static int n; // 세로선 개수
    static int m; // 가로선 개수
    static int h; // 가로선을 놓을 수 있는 위치의 수
    static boolean[][] ladder; // 사다리 상태 저장 배열
    static int answer = -1; // 결과값, 불가능한 경우 -1

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());

        ladder = new boolean[h + 1][n + 1]; // 1-based 인덱싱

        // 초기 사다리 상태 설정
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            ladder[a][b] = true; // (a, b)와 (a, b+1) 연결
        }

        // 가로선 0개부터 3개까지 추가해보기
        for (int i = 0; i <= 3; i++) {
            dfs(0, 0, i);
            if (answer != -1) {
                break; // 이미 답을 찾았으면 종료
            }
        }

        bw.write(answer + "");
        bw.flush();
        br.close();
        bw.close();
    }

    // count: 현재까지 추가한 가로선 개수, depth: 현재 탐색 깊이, maxCount: 최대 추가할 가로선 개수
    private static void dfs(int count, int start, int maxCount) {
        if (answer != -1) return; // 이미 답을 찾았으면 종료
        
        if (count == maxCount) {
            // 가로선을 maxCount개 추가했을 때 조건을 만족하는지 확인
            if (checkLadder()) {
                answer = maxCount;
            }
            return;
        }
        
        // 가로선 추가 시도
        for (int i = start; i < h * n; i++) {
            int row = i / n + 1;
            int col = i % n + 1;
            
            // 마지막 세로선은 가로선을 놓을 수 없음
            if (col == n) continue;
            
            // 현재 위치에 가로선 놓기 가능한지 확인
            if (!ladder[row][col] && !ladder[row][col+1] && (col == 1 || !ladder[row][col-1])) {
                ladder[row][col] = true;
                dfs(count + 1, i + 1, maxCount);
                ladder[row][col] = false; // 백트래킹
            }
        }
    }

    // 사다리 게임 시뮬레이션 - 모든 출발점이 자기 번호로 도착하는지 확인
    private static boolean checkLadder() {
        for (int start = 1; start <= n; start++) {
            int pos = start; // 현재 위치
            
            for (int row = 1; row <= h; row++) {
                // 오른쪽으로 이동
                if (pos < n && ladder[row][pos]) {
                    pos++;
                }
                // 왼쪽으로 이동
                else if (pos > 1 && ladder[row][pos-1]) {
                    pos--;
                }
            }
            
            // i번 출발점이 i번에 도착하지 않으면 실패
            if (pos != start) {
                return false;
            }
        }
        
        return true; // 모든 출발점이 자기 번호로 도착
    }
}