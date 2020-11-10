package codeSnippets;

import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionPredicateSnippet {

	public static void main(String[] args) {
		FunctionPredicateSnippet f = new FunctionPredicateSnippet();
		String s = "k;kk;k;k;kxy;k";
		// String res
		Function<String, String> func = getFunctionX(s);

	}

	/**
	 * 
	 * java.util.function.Function<String, String>
	 * 
	 * 
	 * @FunctionalInterface
	 * 
	 * 
	 * 
	 * 						Represents a function that accepts one argument and
	 *                      produces a result. This is a functional interface whose
	 *                      functional method is apply(Object).
	 * 
	 * Function interface has been defined with the generic types T & R, where T is the type of the input and R is the output type
	 * 
	 * 
	 */
	public static Function<String, String> getFunctionX(String ss) {
		return f -> isX(ss).test(f) ? f : "";
	}

	/**
	 * PREDICATE: prüft nur eine Bedingung auf true/false
	 * java.util.function.Predicate<String>
	 * 
	 * 
	 * @FunctionalInterface
	 * 
	 * 
	 * 
	 * 						Represents a predicate (boolean-valued function) of one
	 *                      argument. This is a functional interface whose
	 *                      functional method is test(Object).
	 * 
	 * 
	 *                      Typ des Predicates muß Method haben, die überprüft wird
	 */
	// alle lines mit 5 ; holen
	private static Predicate<String> isX(String ss) {
		if (ss.contains("xy")) {
			return pp -> (pp.split(";").length == 5);
		}
		return null;
	}

}
