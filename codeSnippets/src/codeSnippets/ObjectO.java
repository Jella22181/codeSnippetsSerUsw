package codeSnippets;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;



public class ObjectO {

	private final int value1;
	private final int value2;
	private final boolean bool;

	public ObjectO(int value1, int value2, boolean bool) {
		this.value1 = value1;
		this.value2 = value2;
		this.bool = bool;
	}

	public int getvalue1() {
		return value1;
	}

	public int getvalue2() {
		return value2;
	}

	public boolean isBool() {// getAlive() {
		return bool;
	}

	public static Predicate<ObjectO> getPredicateIsBool() {// isAlive()
		return e -> e.isBool();
	}
// Predicate This is a functional interface whose functional method is test
	public static Predicate<ObjectO> getPredicateNotBool() {// isDead()
		return getPredicateIsBool().negate();
	}

	public static Function<ObjectO, ObjectO> ifPredicateThenChangeStatusToNotBool(Predicate<ObjectO> p) {// toDeadField(Predicate<Field>
																										// isFieldKillable)
		return o -> Optional.of(p).map(changeStatusToNotBool(o)).get();
	}

	public static Function<ObjectO, ObjectO> ifPredicateThenChangeStatusToBool(Predicate<ObjectO> p) {
		return o -> Optional.of(p).map(changeStatusToBool(o)).get();
	}

	public static Function<Predicate<ObjectO>, ObjectO> changeStatusToNotBool(ObjectO o) {
		return p -> p.test(o) ? getChangedObjectToNotBool(o) : o;
	}

	public static Function<Predicate<ObjectO>, ObjectO> changeStatusToBool(ObjectO o) {
		return f -> f.test(o) ? getChangedObjectToBool(o) : o;
	}

	public static ObjectO getChangedObjectToBool(ObjectO o) {
		return new ObjectO(o.getvalue1(), o.getvalue2(), true);
	}

	public static ObjectO getChangedObjectToNotBool(ObjectO o) {
		return new ObjectO(o.getvalue1(), o.getvalue2(), false);
	}
	public static Function<ObjectO, String> strategyForStatusBool() {//	public static Function<Field, String> mapToSign() {
		return o -> getPredicateIsBool().test(o) ? "bazulakatula" : "nee, is nix";
	}	
	
	public static  Function<ObjectO, String> mapToSign() {
		return null;//(field) -> (isAlive().test(field)) ? "#" : "-";
	}
	public boolean getAlive() {
		return true;
	}

	public static Predicate<ObjectO> isAlive() {
		return (field) -> field.getAlive();
	}
//	public static Function<Field, String> mapToSign() {
//		return (field) -> (isAlive().test(field)) ? "#" : "-";
//	}


}
