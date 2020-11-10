package codeSnippets;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BetragFormat {
	public static void main(String... args) {
		BetragFormat b = new BetragFormat();
		String s = b.format("4.551,99");
	}

	private String format(String s) {
		s = s.replace('.', ' ');
		s = s.replace(',', '.');
		s = s.replaceAll("\"", "");

		// Double d = Double.valueOf(s); exc wg space

		// format US
		double doubleValue = 1400.0;
		String pattern = "0.00";
		DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
		format.applyPattern(pattern);
		// will be 1400,00 and not 1.400,00
		String formattedValue = format.format(doubleValue);
		System.out.println(formattedValue);

		// format D
		doubleValue = 1_400.0d;
		pattern = "0.00";
		format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
		format.applyPattern(pattern);
		// will be 1400,00 and not 1_400.0
		formattedValue = format.format(doubleValue);
		System.out.println(formattedValue);

		/***
		 * applyPattern public void applyPattern(String pattern)
		 * 
		 * Apply the given pattern to this Format object. A pattern is a short-hand
		 * specification for the various formatting properties. These properties can
		 * also be changed individually through the various setter methods. There is no
		 * limit to integer digits set by this routine, since that is the typical
		 * end-user desire; use setMaximumInteger if you want to set a real value. For
		 * negative numbers, use a second pattern, separated by a semicolon
		 * 
		 * Example "#,#00.0#" -> 1,234.56
		 * 
		 * This means a minimum of 2 integer digits, 1 fraction digit, and a maximum of
		 * 2 fraction digits.
		 * 
		 * Example: "#,#00.0#;(#,#00.0#)" for negatives in parentheses.
		 * 
		 * In negative patterns, the minimum and maximum counts are ignored; these are
		 * presumed to be set in the positive pattern.
		 */

		// format D mit n,nnn.nn
		doubleValue = 1_400.0d;
		pattern = "#,#00.0#";
		format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
		format.applyPattern(pattern);
		// will be 1400,00 and not 1_400.0
		formattedValue = format.format(doubleValue);
		System.out.println(formattedValue);

		// format D mit Eingabe String "4.888,9"
	    s="\"4.888,9\"";
		String patternBackslash = "\"";
		s=s.replaceAll(patternBackslash, "");
		String patternDot= "[.]";
		s=s.replaceAll(patternDot, "");
		String patternKomma= "[,]";
		s=s.replaceAll(patternKomma, ".");

		doubleValue = Double.valueOf(s);
		pattern = "#00.0#";
		format = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
		format.applyPattern(pattern);
		// will be 1400,00 and not 1_400.0
		formattedValue = format.format(doubleValue);
		System.out.println(formattedValue);

		// Number parse(String source)
		// Parses text from the beginning of the given string to produce a number.
		// abstract Number parse(String source, ParsePosition parsePosition)
		// Returns a Long if possible (e.g., within the range [Long.MIN_VALUE,
		// Long.MAX_VALUE] and with no decimals), otherwise a Double.
		// Object parseObject(String source, ParsePosition pos)
		// Parses text from a string to produce a Number.

		// To format a number for a different Locale, specify it in the call to
		// getInstance.
		// You can also use a NumberFormat to parse numbers:
		// NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
		// s = "\"4.888,9\"";
		// StringBuilder sbb = new StringBuilder("\"4.888,9\"");
		// sbb.deleteCharAt(0);
		// sbb.deleteCharAt(sb.length()-1);


		return s;
	}
}
