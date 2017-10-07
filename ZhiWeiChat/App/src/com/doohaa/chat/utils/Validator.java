package com.doohaa.chat.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Validator {

	/**
	 * Is empty?
	 * @param o
	 * @return boolean
	 */
	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}

		if (o instanceof String) {
			return StringUtils.isBlank((String)o);
		}

		if (o instanceof Collection<?>) {
			return ((Collection<?>)o).isEmpty();
		}

		if (o instanceof Map<?, ?>) {
			return ((Map<?, ?>)o).isEmpty();
		}
		if (o instanceof Integer) {
			return (Integer)o == 0;
		}

		if (o.getClass().isArray()) {
			PrimitiveType type = PrimitiveType.getInstance(o.getClass().getComponentType().getName());
			switch (type) {
				case BOOLEAN:
					return ((boolean[])o).length == 0;
				case BYTE:
					return ((byte[])o).length == 0;
				case CHAR:
					return ((char[])o).length == 0;
				case DOUBLE:
					return ((double[])o).length == 0;
				case FLOAT:
					return ((float[])o).length == 0;
				case INT:
					return ((int[])o).length == 0;
				case LONG:
					return ((long[])o).length == 0;
				case SHORT:
					return ((short[])o).length == 0;
				case OBJECT:
				default:
					return ((Object[])o).length == 0;
			}
		}

		return false;
	}

	/**
	 * Is not empty?
	 * @param o
	 * @return boolean
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * Is empty?<p>
	 * if null or empty string, return true.
	 * @param s
	 * @return boolean
	 */
	public static boolean isEmpty(String s) {
		return StringUtils.isBlank(s);
	}

	/**
	 * Is not empty?
	 * @param s
	 * @return boolean
	 */
	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(boolean[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(boolean[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(byte[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(byte[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(char[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(char[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(double[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(double[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(float[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(float[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(int[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(int[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(long[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(long[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(short[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(short[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or length is 0, return true.
	 * @param a
	 * @return boolean
	 */
	public static boolean isEmpty(Object[] a) {
		return (a == null) || (a.length == 0);
	}

	/**
	 * Is not empty?
	 * @param a
	 * @return boolean
	 */
	public static boolean isNotEmpty(Object[] a) {
		return !isEmpty(a);
	}

	/**
	 * Is empty?<p>
	 * if null or empty, return true.
	 * @param c
	 * @return boolean
	 */
	public static boolean isEmpty(Collection<?> c) {
		return (c == null) || c.isEmpty();
	}

	/**
	 * Is not empty?
	 * @param c
	 * @return boolean
	 */
	public static boolean isNotEmpty(Collection<?> c) {
		return !isEmpty(c);
	}

	/**
	 * Is empty?<p>
	 * if null or empty, return true.
	 * @param m
	 * @return boolean
	 */
	public static boolean isEmpty(Map<?, ?> m) {
		return (m == null) || m.isEmpty();
	}

	/**
	 * Is not empty?
	 * @param m
	 * @return boolean
	 */
	public static boolean isNotEmpty(Map<?, ?> m) {
		return !isEmpty(m);
	}

	protected enum PrimitiveType {

		BOOLEAN,
		BYTE,
		CHAR,
		DOUBLE,
		FLOAT,
		INT,
		LONG,
		SHORT,
		OBJECT;

		protected static final Map<String, PrimitiveType> enumNameMap = lowerNameMap(values());

		protected static PrimitiveType getInstance(String className) {
			PrimitiveType instance = enumNameMap.get(className);
			return (instance != null) ? instance : OBJECT;
		}

		private static <E extends Enum<?>> Map<String, E> lowerNameMap(E[] enums) {
			Map<String, E> map = new HashMap<String, E>();
			for (E e : enums) {
				map.put(e.name().toLowerCase(), e);
			}
			return Collections.unmodifiableMap(map);
		}

	}

}
