
import java.util.Scanner;

public class B14891 {
    
	static class gear {
		boolean[] teeth = new boolean[8];

		public gear(boolean[] teeth) {
			this.teeth = teeth;
		}

		public boolean[] rightSpin() {
			boolean tmp = teeth[7];
			for (int i = 7; i >= 1; i--) {
				teeth[i] = teeth[i - 1];
			}
			teeth[0] = tmp;
			return new boolean[] { teeth[2], teeth[6] };
		}

		public boolean[] leftSpin() {
			boolean tmp = teeth[0];
			for (int i = 0; i < 7; i++) {
				teeth[i] = teeth[i + 1];
			}
			teeth[7] = tmp;
			return new boolean[] { teeth[2], teeth[6] };
		}
	}

	static gear[] gears = new gear[4];

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		for (int i = 0; i < 4; i++) {
			String line = sc.nextLine();
			boolean[] arr = new boolean[8];
			for (int j = 0; j < 8; j++) {
				arr[j] = (line.charAt(j) == '1');
			}
			gears[i] = new gear(arr);
		}

		int k = sc.nextInt();

		for (int i = 0; i < k; i++) {
			int pos = sc.nextInt() - 1;
			int dir = sc.nextInt();
			rotate(pos, dir);
		}

		System.out.println(score());
	}

	private static void rotate(int pos, int dir) {
		int[] real = new int[4];
		real[pos] = dir;

		for (int i = pos; i > 0; i--) {
			if (gears[i].teeth[6] != gears[i - 1].teeth[2]) {
				real[i - 1] = -real[i];
			} else {
				break;
			}
		}

		for (int i = pos; i < 3; i++) {
			if (gears[i].teeth[2] != gears[i + 1].teeth[6]) {
				real[i + 1] = -real[i];
			} else {
				break;
			}
		}

		for (int i = 0; i < 4; i++) {
			if (real[i] == 1) {
				gears[i].rightSpin();
			} else if (real[i] == -1) {
				gears[i].leftSpin();
			}
		}
	}

	private static int score() {
		int score = 0;

		if (gears[0].teeth[0])
			score += 1;
		if (gears[1].teeth[0])
			score += 2;
		if (gears[2].teeth[0])
			score += 4;
		if (gears[3].teeth[0])
			score += 8;

		return score;
	}

}
