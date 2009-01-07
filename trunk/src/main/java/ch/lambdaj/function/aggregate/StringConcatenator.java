package ch.lambdaj.function.aggregate;


public class StringConcatenator implements Aggregator<Object> {
	
	private static final String DEFAULT_SEPARATOR = ", ";
	
	private String separator;
	
	public StringConcatenator() {
		this(DEFAULT_SEPARATOR);
	}
	
	public StringConcatenator(String separator) {
		this.separator = separator;
	}

	public Object aggregate(Object first, Object second) {
		return first.toString() + (first.toString().length() == 0 ? "" : separator) + second.toString();
	}

	public String emptyItem() {
		return "";
	}
}
