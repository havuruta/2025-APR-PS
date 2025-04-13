package gyumin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/*
 * 총 8개의 톱니를 가지고 있는 톱니바퀴 4개
 * 톱니바퀴를 k번 회전
 * 회전은 한 칸을 기준, 시계 방향/반시계 방향
 * 서로 맞닿은 극에 따라 회전시킬 수도 있고, 회전 시키지 않을 수 잇음
 * 맞닿은 톱니의 극이 다르면 서로 반대방향으로 회전
 * 맞닿은 톱니의 극이 같으면 회전 x
 * 맞닿은 톱니바퀴가 회전 안하면 회전 x
 * 
 * 입력
 * 1번 톱니 상태
 * 2번 톱니 상태
 * 3번 톱니 상태
 * 4번 톱니 상태
 * (각 톱니 상태는 12시 방향부터 시계방향, 0: N극, 1: S극)
 * k
 * 회전할 톱니, 회전 방향(양수: 시계, 음수: 반시계)
 * 
 * 출력
 * 12시 방향(0번 인덱스) 값을 규칙에 맞춰 모두 더한 결과
 */
public class B14891 {
    static int[][] toolbox; // 톱니 바퀴를 모두 담을 공간
    static boolean[] preTurn; // 이전 톱니가 돌아야하는지 검사
    static boolean[] nextTurn; // 다음 톱니가 돌아야하는지 검사

    static {
        toolbox = new int[5][]; // 1-base
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        for (int i = 1; i <= 4; i++) {
            toolbox[i] = Arrays.stream(br.readLine().split("")).mapToInt(Integer::parseInt).toArray();
        }

        int k = Integer.parseInt(br.readLine());

        for (int i = 0; i < k; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            preTurn = new boolean[5];
            nextTurn = new boolean[5];

            int target = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());

            // 앞뒤로 맞닿는 애들 다 확인
            preCheck(target);
            nextCheck(target);

            // 일단 당사자 돌리고
            turn(target, dir);

            // 뒤에 있는 애들 돌리고
            int preDir = -dir;

            for (int j = target - 1; j >= 1; j--) {
                if (!preTurn[j])
                    break;
                turn(j, preDir);
                preDir = -preDir;
            }

            // 앞에 있는 애들 돌리기

            int nextDir = -dir;

            for (int j = target + 1; j <= 4; j++) {
                if (!nextTurn[j])
                    break;
                turn(j, nextDir);
                nextDir = -nextDir;
            }
        } // 회전 끝

        int res = 0;

        for (int i = 1; i <= 4; i++) {
            switch (i) {
                case 1:
                    res += toolbox[i][0];
                    break;
                case 2:
                    res += toolbox[i][0] * 2;
                    break;
                case 3:
                    res += toolbox[i][0] * 4;
                    break;
                case 4:
                    res += toolbox[i][0] * 8;
                    break;
            }
        }

        bw.write(res + "");
        bw.flush();

        br.close();
        bw.close();
    } // main

    private static void preCheck(int target) {
        for (int i = target; i > 1; i--) {
            if (toolbox[i][6] != toolbox[i - 1][2])
                preTurn[i - 1] = true;
        }
    } // preCheck

    private static void nextCheck(int target) {
        for (int i = target; i < 4; i++) {
            if (toolbox[i][2] != toolbox[i + 1][6])
                nextTurn[i + 1] = true;
        }
    } // nextCheck

    private static void turn(int target, int dir) {
        int[] cog = new int[8];

        switch (dir) {
            case 1:
                for (int i = 0; i < 8; i++) {
                    cog[i] = toolbox[target][(i - 1 + 8) % 8];
                }
                toolbox[target] = cog;
                break;
            case -1:
                for (int i = 0; i < 8; i++) {
                    cog[i] = toolbox[target][(i + 1) % 8];
                }
                toolbox[target] = cog;
                break;
        }
    } // turn
}
