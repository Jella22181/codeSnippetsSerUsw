package codeSnippets;

import java.util.Arrays;
import java.util.List;

public class Stream201802 {

	public static void main(String[] args) {

		Stream201802 s = new Stream201802();
		List<Integer> num = Arrays.asList(1, 2, 3);

		int res = 0;
		for (int e : num) {
			if (e % 2 == 0) {
				res += e * 2;
			}
		}
		
		res = num.stream().filter(e -> e% 2== 0).mapToInt(e-> e*2).sum();
		System.out.println(res);
		//num.forEach(System.out::println);
	}

}
