package codeSnippets;

import java.util.function.Predicate;

import codeSnippets.PredicateExample;

public class PredicateExampleLogic {
	public static Predicate<PredicateExample> isCategoryX(String category) {
		return pp -> pp.isCatX(category);
	}

	public static Predicate<PredicateExample> isCategoryArzt() {
		return pp -> pp.isArzt();
	}

}
