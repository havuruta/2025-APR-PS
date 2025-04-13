package gyumin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

/*
 * 사무실은 1 * 1 크기의 정사각형으로 구성, N * M
 * CCTV는 총 5가지 종류
 * 1번: 한 쪽 방향만
 * 2번: 서로 반대가 되는 두 방향
 * 3번: 두 방향이 직각
 * 4번: 한 방향을 제외한 세방향
 * 5번: 네 방향
 * 
 * 6번은 벽을 의미
 * 
 * CCTV는 회전할 수 있으며 회전시켜서 사각지대를 최소화 하는 것이 목적
 * 
 * 사무실의 최대 크기는 8 * 8
 * 둘째 줄부터는 사무실 각 칸의 정보
 * 
 * 출력은 사각 지대의 최소 크기를 출력
 * 
 * 전략
 * CCTV를 하나씩 방향을 지정하고 다음 CCTV의 방향을 지정
 * 각각의 방향을 지정했다 해제하는 방식의 백트래킹
 */

public class B15683 {
    static int n; // 세로
    static int m; // 가로

    static int[][] room;

    static int[] oneDir;
    static int[][] twoDir;
    static int[][] threeDir;
    static int[][] fourDir;
    static int[] fiveDir;

    static int wall; // 나중에 이걸 빼서 최종 사각 지대 수를 파악
    static int cam; // 이건 반복 횟수 지정에 씀
    static boolean[][] canSee; // 볼 수 있는 영역을 체크
    static int[][] checker; // 그 위치를 특정 cctv가 점유중인지 확인하는 배열

    static int cantSee; // 벽 + 사각지대

    static List<int[]> cctvList = new ArrayList<>(); // CCTV 좌표를 저장할 리스트

    static {
        // 1: 북쪽, 2: 동쪽, 3: 남쪽, 4: 서쪽
        oneDir = new int[] { 1, 2, 3, 4 };
        twoDir = new int[][] { { 1, 3 }, { 2, 4 } };
        threeDir = new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 1 } };
        fourDir = new int[][] { { 4, 1, 2 }, { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 1 } };
        fiveDir = new int[] { 1, 2, 3, 4 };

        cantSee = 64; // 8 * 8 = 64
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        room = new int[n][m];
        canSee = new boolean[n][m];
        checker = new int[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                room[i][j] = Integer.parseInt(st.nextToken());
                if (room[i][j] == 6)
                    wall++;
                if (room[i][j] > 0 && room[i][j] < 5) {
                    cam++;
                    cctvList.add(new int[]{i, j, room[i][j]}); // CCTV 좌표와 종류 저장
                }
            }
        }

        for(int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                if (room[i][j] == 5) {
                    room[i][j] = -1;
                    set(i, j, fiveDir);
                }
            }
        } //  5번 cctv는 회전을 해도 그대로니까 미리 처리해두기

        run(0);

        int res = cantSee - wall;

        bw.write(res + "");
        bw.flush();

        br.close();
        bw.close();

    } // main

    private static void run(int count) {
        if (count == cam) {
            int temp = 0;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (!canSee[i][j])
                        temp++;
                }
            }

            cantSee = Math.min(cantSee, temp);

            return;
        }

        // CCTV 좌표를 사용하여 처리
        int[] cctv = cctvList.get(count);
        int r = cctv[0];
        int c = cctv[1];
        int type = cctv[2];

        switch (type) {
            case 1:
                room[r][c] = -1;
                for (int dIdx = 0; dIdx < oneDir.length; dIdx++) {
                    set(r, c, oneDir[dIdx]);
                    run(count + 1);
                    unset(r, c, oneDir[dIdx]);
                }
                room[r][c] = 1;
                break;
            case 2:
                room[r][c] = -1;
                for (int dIdx = 0; dIdx < twoDir.length; dIdx++) {
                    set(r, c, twoDir[dIdx]);
                    run(count + 1);
                    unset(r, c, twoDir[dIdx]);
                }
                room[r][c] = 2;
                break;
            case 3:
                room[r][c] = -1;
                for (int dIdx = 0; dIdx < threeDir.length; dIdx++) {
                    set(r, c, threeDir[dIdx]);
                    run(count + 1);
                    unset(r, c, threeDir[dIdx]);
                }
                room[r][c] = 3;
                break;
            case 4:
                room[r][c] = -1;
                for (int dIdx = 0; dIdx < fourDir.length; dIdx++) {
                    set(r, c, fourDir[dIdx]);
                    run(count + 1);
                    unset(r, c, fourDir[dIdx]);
                }
                room[r][c] = 4;
                break;
        }
    }

    private static void set(int r, int c, int dir) {
        switch (dir) {
            case 1:
                for (int i = r; i >= 0; i--) {
                    if (room[i][c] == 6)
                        break;
                    checker[i][c]++;
                    canSee[i][c] = true;
                }
                break;
            case 2:
                for (int i = c; i < m; i++) {
                    if (room[r][i] == 6)
                        break;
                    checker[r][i]++;
                    canSee[r][i] = true;
                }
                break;
            case 3:
                for (int i = r; i < n; i++) {
                    if (room[i][c] == 6)
                        break;
                    checker[i][c]++;
                    canSee[i][c] = true;
                }
                break;
            case 4:
                for (int i = c; i >= 0; i--) {
                    if (room[r][i] == 6)
                        break;
                    checker[r][i]++;
                    canSee[r][i] = true;
                }
                break;
        }
    } // set

    private static void set(int r, int c, int[] dirs) {
        for (int dir : dirs) {
            set(r, c, dir);
        }
    } // set

    private static void unset(int r, int c, int dir) {
        switch (dir) {
            case 1:
                for (int i = r; i >= 0; i--) {
                    if (room[i][c] == 6)
                        break;
                    checker[i][c]--;
                    if (checker[i][c] == 0)
                        canSee[i][c] = false;
                }
                break;
            case 2:
                for (int i = c; i < m; i++) {
                    if (room[r][i] == 6)
                        break;
                    checker[r][i]--;
                    if (checker[r][i] == 0)
                        canSee[r][i] = false;
                }
                break;
            case 3:
                for (int i = r; i < n; i++) {
                    if (room[i][c] == 6)
                        break;
                    checker[i][c]--;
                    if (checker[i][c] == 0)
                        canSee[i][c] = false;
                }
                break;
            case 4:
                for (int i = c; i >= 0; i--) {
                    if (room[r][i] == 6)
                        break;
                    checker[r][i]--;
                    if (checker[r][i] == 0)
                        canSee[r][i] = false;
                }
                break;
        }
    } // unset

    private static void unset(int r, int c, int[] dirs) {
        for (int dir : dirs) {
            unset(r, c, dir);
        }
    } // unset
}
