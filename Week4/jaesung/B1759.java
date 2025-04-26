package jaesung;

/*
 문제 : 1759(암호 만들기)
 시간 : 180ms
 풀이 : 알파벳 순서대로 조합이 가능하도록 정렬 후, 모든 조합 경우의 수를 백트리킹을 통해 구성. 자음, 모음 조건을 만족하는 경우 출력.
 
 */

import java.util.Scanner;
import java.util.Arrays;
import java.lang.StringBuilder;

public class B1759 {
    static int L, C;
    static char[] letters;
    static boolean[] visited;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        L = sc.nextInt();
        C = sc.nextInt();
        letters = new char[C];
        for (int i = 0; i < C; i++) {
            letters[i] = sc.next().charAt(0);
        }
        Arrays.sort(letters);
        visited = new boolean[C];
        dfs(0, 0);
    }

    static void dfs(int start, int depth) {
        if (depth == L) {
            StringBuilder sb = new StringBuilder();
            int vowel = 0;
            int consonant = 0;
            for (int i = 0; i < C; i++) {
                if (visited[i]) {
                    sb.append(letters[i]);
                    char ch = letters[i];
                    if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                        vowel++;
                    } else {
                        consonant++;
                    }
                }
            }
            if (vowel >= 1 && consonant >= 2) {
                System.out.println(sb.toString());
            }
            return;
        }

        for (int i = start; i < C; i++) {
            visited[i] = true;
            dfs(i + 1, depth + 1);
            visited[i] = false;
        }
    }
}
