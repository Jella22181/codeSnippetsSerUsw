package codeSnippets;

import java.time.LocalDateTime;

public class DateTime1 {
	public static void main(String... args) {
		DateTime1 dt1 = new DateTime1();
		LocalDateTime ldt = LocalDateTime.now();
		dt1.s1(ldt.toString());
	}

	DateTime1() {
	}	
	
	public String s1(String st) {
		System.out.println(st); // 2018-10-28T20:33:16.138
		String s = st.toString().replace("T", "_");
		 System.out.println(s); // 2018-10-28T20:33:16.138
			 s = s.replace(":", "");
			 System.out.println(s); // 2018-10-28T20:33:16.138 
			return s;
	}	
}
