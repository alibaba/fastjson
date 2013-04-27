package data;

public class ReprUtil
{
	public static String repr(String s)
	{
		if (s == null) return "null";
		return '"' + s + '"';
	}

	public static String repr(Iterable<String> it)
	{
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		String sep = "";
		for (String s : it) {
			buf.append(sep); sep = ", ";
			buf.append(repr(s));
		}
		buf.append(']');
		return buf.toString();
	}
}
