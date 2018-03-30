package com.seikomi.janus.utils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities static methods. This methods extends the Java SDK and provide
 * advance function with high reusability.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public final class Utils {
	static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	private static final String WINDOWS_SEPARATOR = "\\";
	private static final String UNIX_SEPARATOR = "/";
	public static final String USER_HOME_PATH = System.getProperty("user.home");

	private Utils() {
		// Hide the public constructor.
	}

	/**
	 * Check if the string {@code s} represent an integer of radix 10.
	 * 
	 * @param string
	 *            the string to inspect
	 * @return {@code true} the string {@code s} represent an integer of radix 10,
	 *         {@code false} otherwise.
	 */
	public static boolean isInteger(String string) {
		return isInteger(string, 10);
	}

	/**
	 * Check if the string {@code s} represent an integer of radix {@code radix}.
	 * 
	 * @param string
	 *            the string to inspect
	 * @param radix
	 *            the radix to apply
	 * @return {@code true} the string {@code s} represent an integer of radix
	 *         {@code radix}, {@code false} otherwise.
	 */
	public static boolean isInteger(String string, int radix) {
		boolean isInteger = true;

		if (string.isEmpty()) {
			isInteger = false;
		}

		for (int i = 0; i < string.length(); i++) {
			if (i == 0 && string.charAt(i) == '-') {
				if (string.length() == 1) {
					isInteger = false;
				} else {
					continue;
				}
			}
			if (Character.digit(string.charAt(i), radix) < 0) {
				isInteger = false;
			}
		}

		return isInteger;
	}

	/**
	 * Converts a {@code byte[]} variable in the corresponding long.
	 * 
	 * @param b
	 *            the {@code byte[]} variable to convert.
	 * @return the corresponding long
	 */
	public static long bytesToLong(byte[] b) {
		ByteBuffer buffer = ByteBuffer.wrap(b);
		return buffer.getLong();
	}

	/**
	 * Converts a {@code long} variable in the corresponding array of bytes.
	 * 
	 * @param i
	 *            the {@code long} variable to convert.
	 * @return the corresponding array of bytes (8 bytes)
	 */
	public static byte[] longToBytes(long i) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(i);
		return buffer.array();
	}

	public static Path transformStringPath(String stringPath) {
		String transfromString = stringPath;

		if (File.separator.equals(UNIX_SEPARATOR)) {
			transfromString = transfromString.replaceAll(WINDOWS_SEPARATOR, File.separator);
		} else if (File.separator.equals(WINDOWS_SEPARATOR)) {
			transfromString = transfromString.replaceAll(UNIX_SEPARATOR, "\\" + File.separator);
		}

		if (transfromString.startsWith("~")) {
			transfromString = USER_HOME_PATH + transfromString.substring(1);
		}

		return Paths.get(transfromString);
	}

	public static Integer convertStringToInt(String string) {
		Integer stringInInteger = null;
		try {
			stringInInteger = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			LOGGER.error("The string {} cannot be convert in integer type", string, e);
		}
		return stringInInteger;
	}

	/**
	 * Binary type.
	 * 
	 * @param <L>
	 *            left assignment type
	 * @param <R>
	 *            right assignment type
	 */
	public static class Pair<L, R> {

		private final L left;
		private final R right;

		/**
		 * Construct a new Pair of two object one on the left, the other on the right.
		 * 
		 * @param left
		 *            the object on the left
		 * @param right
		 *            the object on the right
		 */
		public Pair(L left, R right) {
			this.left = left;
			this.right = right;
		}

		/**
		 * Gets the left object.
		 * 
		 * @return the left object
		 */
		public L getLeft() {
			return left;
		}

		/**
		 * Gets the right object.
		 * 
		 * @return the right object
		 */
		public R getRight() {
			return right;
		}

		@Override
		public int hashCode() {
			return left.hashCode() ^ right.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pair<?, ?>))
				return false;
			Pair<?, ?> pairo = (Pair<?, ?>) o;
			return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
		}

	}

}
