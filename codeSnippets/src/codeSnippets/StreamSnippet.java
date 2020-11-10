package codeSnippets;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import static codeSnippets.ObjectO.mapToSign;
import static codeSnippets.ObjectO.strategyForStatusBool;

public class StreamSnippet {

	public static void main(String[] args) {

		List<ObjectO> listObj = new LinkedList<ObjectO>();
		boolean bool = false;
		int v1 = 0;
		int v2 = 9;

		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				bool = true;
			} else {
				bool = false;
			}
			listObj.add(new ObjectO(v1, v2, bool));
			v1++;
			v2++;
		}

		listObj.stream().collect(getCollectorTypeObjectMapIntListString(ObjectO::getvalue1, strategyForStatusBool()));
		// listObj.stream().collect(groupBy(Object::getvalue1, mapToSign()));

		// groupBy(ObjectO::getvalue1, ObjectO::getvalue2); ok

		// groupBy(ObjectO::getvalue1, mapToSign());

		// s.t(listObj);

	}

	/**
	 * <R,A> R collect(Collector<? super T,A,R> collector)
	 * 
	 * Performs a mutable reduction operation on the elements of this stream using a
	 * Collector. A Collector encapsulates the functions used as arguments to
	 * collect(Supplier, BiConsumer, BiConsumer), allowing for reuse of collection
	 * strategies and composition of collect operations such as multiple-level
	 * grouping or partitioning.
	 * 
	 * A Collector is specified by four functions that work together to accumulate
	 * entries into a mutable result container, and optionally perform a final
	 * transform on the result. They are: •creation of a new result container
	 * (supplier()) •incorporating a new data element into a result container
	 * (accumulator()) •combining two result containers into one (combiner())
	 * •performing an optional final transform on the container (finisher())
	 * 
	 * The following will accumulate strings into an ArrayList: List<String> asList
	 * = stringStream.collect(Collectors.toList());
	 *
	 *
	 * The following will classify Person objects by city:
	 * 
	 * Map<String, List<Person>> peopleByCity =
	 * personStream.collect(Collectors.groupingBy(Person::getCity));
	 * 
	 */

	// public void t(List<Object> listObj) {

	// listObj.stream().collect(getCollectorTypeObjectMapIntListString(Object::getvalue1,
	// strategyForStatusBool()));
	// .entrySet().stream().sorted(byYCoord())
	// .map(toSingleLine()).map(createTextLine()).peek(System.out::println).collect(Collectors.toList());
	// }

	// private static List<Object> iterateGameboard(List<Object> gameboard) {
	// // @formatter:off
	// return gameboard.stream()
	// .map(toDeadField(which(isAlive(), and(),
	// which(hasLessThanTwo(livingNeighboursIn(gameboard)), or(),
	// hasMoreThanThree(livingNeighboursIn(gameboard))))))
	// .map(toAliveField(which(isDead(), and(),
	// hasExactThree(livingNeighboursIn(gameboard)))))
	// .collect(Collectors.toList());
	// // @formatter:on
	// }

	private static Comparator<Entry<Integer, List<String>>> byYCoord() {
		return (entry1, entry2) -> Integer.compare(entry1.getKey(), entry2.getKey());
	}

	/**
	 * groupingBy public static <T,K,A,D> Collector<T,?,Map<K,D>>
	 * groupingBy(Function<? super T,? extends K> classifier, Collector<? super
	 * T,A,D> downstream)
	 * 
	 * Returns a Collector implementing a cascaded "group by" operation on input
	 * elements of type T, grouping elements according to a classification function,
	 * and then performing a reduction operation on the values associated with a
	 * given key using the specified downstream Collector. The classification
	 * function maps elements to some key type K. The downstream collector operates
	 * on elements of type T and produces a result of type D. The resulting
	 * collector produces a Map<K, D>.
	 * 
	 * There are no guarantees on the type, mutability, serializability, or
	 * thread-safety of the Map returned.
	 * 
	 * For example, to compute the set of last names of people in each city:
	 * 
	 * Map<City, Set<String>> namesByCity =
	 * people.stream().collect(groupingBy(Person::getCity,
	 * mapping(Person::getLastName, toSet())));
	 * 
	 * Implementation Note:The returned Collector is not concurrent. For parallel
	 * stream pipelines, the combiner function operates by merging the keys from one
	 * map into another, which can be an expensive operation. If preservation of the
	 * order in which elements are presented to the downstream collector is not
	 * required, using groupingByConcurrent(Function, Collector) may offer better
	 * parallel performance.
	 * 
	 * Type Parameters:
	 * 
	 * T - the type of the input elements
	 * 
	 * K - the type of the keys
	 * 
	 * A - the intermediate accumulation type of the downstream collector
	 * 
	 * D - the result type of the downstream reductionParameters:
	 * 
	 * classifier - a classifier function mapping input elements to keysdownstream -
	 * a Collector implementing the downstream reduction
	 */

	/**
	 * mapping public static <T,U,A,R> Collector<T,?,R> mapping(Function<? super T,?
	 * extends U> mapper, Collector<? super U,A,R> downstream)
	 * 
	 * Adapts a Collector accepting elements of type U to one accepting elements of
	 * type T by applying a mapping function to each input element before
	 * accumulation. API Note:The mapping() collectors are most useful when used in
	 * a multi-level reduction, such as downstream of a groupingBy or
	 * partitioningBy. For example, given a stream of Person, to accumulate the set
	 * of last names in each city: Map<City, Set<String>> lastNamesByCity =
	 * people.stream().collect(groupingBy(Person::getCity,
	 * mapping(Person::getLastName, toSet())));
	 * 
	 * Type Parameters:T - the type of the input elementsU - type of elements
	 * accepted by downstream collector
	 * 
	 * A - intermediate accumulation type of the downstream collector
	 * 
	 * R - result type of collectorParameters:mapper - a function to be applied to
	 * the input elementsdownstream - a collector which will accept mapped values
	 * 
	 * Returns:a collector which applies the mapping function to the input elements
	 * and provides the mapped results to the downstream collector
	 * 
	 * 
	 */
	private static Collector<ObjectO, ?, Map<Integer, List<String>>> getCollectorTypeObjectMapIntListString(
			Function<ObjectO, Integer> f, Function<ObjectO, String> f2) {
		// Collectors.toList() Returns a Collector that accumulates the input elements
		// into a new List.

		/**
		 * Returns a Collector implementing a cascaded "group by" operation on input
		 * elements of type T, grouping elements according to a classification function,
		 * and then performing a reduction operation on the values associated with a
		 * given key using the specified downstream Collector. The classification
		 * function maps elements to some key type K. The downstream collector operates
		 * on elements of type T and produces a result of type D. The resulting
		 * collector produces a Map<K, D>.
		 * 
		 * There are no guarantees on the type, mutability, serializability, or
		 * thread-safety of the Map returned.
		 * 
		 * For example, to compute the set of last names of people in each city:
		 * 
		 * Map<City, Set<String>> namesByCity =
		 * people.stream().collect(groupingBy(Person::getCity,
		 * mapping(Person::getLastName, toSet())));
		 */
		return Collectors.groupingBy(f, Collectors.mapping(f2, Collectors.toList()));

	}

	// private static Collector<ObjectO, ?, Map<Integer, List<String>>>
	// groupBy(Function<ObjectO, Integer> supplier,
	// Function<ObjectO, Integer> mapper) {
	// return null;//Collectors.groupingBy(supplier, Collectors.mapping(mapper,
	// Collectors.toList()));
	// }OK
	private static Collector<ObjectO, ?, Map<Integer, List<String>>> groupBy(Function<ObjectO, Integer> supplier,
			// Function<ObjectO, String>
			Function<ObjectO, String> mapper) {
		return null;
	}
}
