package codeSnippets;

import java.util.Arrays;
import java.util.List;

import javax.mail.search.IntegerComparisonTerm;

public class CodilityFish {
	static int counter0 = 0;
	static int counter1 = 0;
	Integer a[] = { 4, 3, 2, 1, 5 };
	Integer a11[] = { 46, 31, 2, 14, 5 };
	Integer a12[] = { 46, 315, 2, 14, 5 };
	Integer a13[] = { 46, 388 };
	Integer a14[] = { 388 };
	Integer a1[] = { 1, 233, 1388, 90 };
	Integer b[] = { 0, 1, 0, 0, 0 };

	public static void main(String[] args) {

		CodilityFish cf = new CodilityFish();
		cf.process();
		System.out.println("+++++++++++++++ cnt: " + counter1 + "---" + counter0);
		
		

	}

	public int process() {
		int res = 0;
		int idxA = 0;
		int idxB = 0;
		proc(idxA, idxB);
		res = counter1 + counter0;

		return res;
	}

	public void proc(int idxA, int direction) {
		

		if (direction == 0 && idxA == 0) {
			counter0++;
			proc (++idxA, a[idxA]);
		}
		// up
		if (direction == 0 && idxA < a.length) {
			int j = idxA;

			for (int i = idxA; i > 0 && j >= 0; i--) {
				j--;
				if (b[j] == 1) {

					if (a[idxA] < a[j]) {
						a[idxA] = -199;
						direction = b[idxA];
						proc(j, direction);
					} else {
						a[j] = -199;
						direction = b[j];
						proc(j, direction);
					}
				} else {
					proc(idxA, b[idxA]);
				}
			}
		}
		// up
		else if (direction == 1 && idxA < a.length) {
			int j = idxA;
j++;
			for (int i = idxA; i < a.length - 1 && j < a.length; i++) {
				if (b[j] == 1) {

					if (a[idxA] < a[j]) {
						a[idxA] = -199;
						direction = b[idxA];
						proc(j, direction);
					} else {
						a[j] = -199;
						direction = b[j];
						proc(j, direction);
					}
				} else {
					proc(j, b[j]);
				}
			}
		}
	}

	// falsche Logik, muß kontinuierlich ablaufen - nach u nach
	public int process2() {
		int res = 0;

		int[] maxArr = max(a);
		proc2(maxArr);
		res = counter1 + counter0;

		return res;
	}

	public int[] proc2(int[] maxArr) {

		if (maxArr[0] < 0) {
			return maxArr;
		} else if (counter0 + counter1 >= a.length) {
			return maxArr;
		}

		int max = maxArr[0];
		int posMax = maxArr[1];
		int directionMax = b[posMax];

		// TODO posMax == 0
		// upstream
		if (directionMax == 0 && posMax > 0) {
			int posMaxStart = posMax - 1;

			for (int i = posMaxStart; i >= 0; i--) {

				int direction = b[posMaxStart];
				if (direction == 1) {
					a[posMaxStart] = -199;
					b[posMaxStart] = -199;

				} else {
					counter0++;
				}
			}

			a[maxArr[1]] = a[maxArr[1]] * -1;

			int[] newMaxArr = max(a);

		}

		// down
		else if (directionMax == 1 && posMax < a.length - 1) {
			int posMaxStart = posMax + 1;

			for (int i = posMaxStart; i < a.length; i++) {

				int direction = b[posMaxStart];
				if (direction == 0) {
					a[posMaxStart] = -990;
					b[posMaxStart] = -990;

				} else {
					counter1++;
				}
			}

			a[maxArr[1]] = a[maxArr[1]] * -1;

			int[] newMaxArr = max(a);

		}

		return maxArr;

	}

	public static int[] max(Integer a[]) {
		int max[] = { 0, 0 };

		int tmp = 0;
		int maxT = a[0];
		int maxTIdx = 0;
		int j = 1;
		for (int i = 0; i < a.length - 1 && j < a.length; i++) {

			System.out.println(tmp = a[i]);
			j = i + 1;
			if (maxT < a[j]) {
				maxT = a[j];
				System.out.println(maxT);

				maxTIdx = j;
			} else {
				System.out.println("else " + maxT);

			}

		}
		System.out.println("#####" + maxT);
		System.out.println("#####" + maxTIdx);
		max[0] = maxT;
		max[1] = maxTIdx;
		return max;
	}
}
