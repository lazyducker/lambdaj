package ch.lambdaj.mock;


public class Text {
	private String string;

	public Text() {}

	public Text(String string) {
		this.string = string;
	}

	public int length() {
		return string.length();
	}

	public Text subString(int begin, int end) {
		return new Text(string.substring(begin, end));
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Text ? string.equals(((Text) obj).string) : false;
	}

	@Override
	public int hashCode() {
		return string.hashCode();
	}

	@Override
	public String toString() {
		return string;
	}
}
