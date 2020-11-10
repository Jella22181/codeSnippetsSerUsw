package codeSnippets;

import java.util.function.Consumer;

// execute around method pattern
// never ever trust a feature with management in its name
// venkat patterns for j8 https://www.youtube.com/watch?v=e4MT_OguDKg
public class TryWithResourcesSample {
	// j7 ARM Autom res mgmnt
	public static void main(String[] args) {

		TryWithResources.use(resource -> resource.op1().op2());

	}
}

class TryWithResources/* implements AutoCloseable */ {

	private TryWithResources() {

	}

	public TryWithResources op1() {
		return this;
	}

	public TryWithResources op2() {
		return this;
	}

	private void close() {

	}
// Consumer accepts a single input argument and returns no result.
	public static void use(Consumer<TryWithResources> c) {
		TryWithResources resource = new TryWithResources();
		try {//Performs this operation on the given argument
			c.accept(resource);
		} finally {
			try {
				resource.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
