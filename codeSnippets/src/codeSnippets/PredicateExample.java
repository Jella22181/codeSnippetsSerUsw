package codeSnippets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PredicateExample {

	public String getPriAusgabenArztWithStream(String str) {
		String name;
		try {
			List<String> ll = new ArrayList();
			ll.add("a");
			ll.add("b");
			ll.add("c");

			ll = ll.stream().filter(e -> e != null).collect(Collectors.toList());
			String pp = ll.get(0);
		} catch (Exception ex) {
			return "EXC: " + ex.toString();
		}
		return "SUCCESS: ";
	}

	public boolean isCatX(String category) {
		System.out.println(this.getKategorie());
		if (this.getKategorie().contains(category)) {
			System.out.println("isCatX true: " + this.getKategorie());
			return true;
		}
		return false;
	}

	public String getKategorie() {
		return "x";
	}
	public boolean isArzt() {
		if (this.getKategorie().contains("Arzt")) {
			return true;
		}
		return false;
	}
}
