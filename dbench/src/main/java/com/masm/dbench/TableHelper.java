package com.masm.dbench;

import java.util.Arrays;
import java.util.stream.IntStream;

import de.vandermeer.skb.asciitable.AsciiTable;

public abstract class TableHelper {

	private static Object times[][];
	private static long iterations[][];

	private static int numColumns;
	private static int numRows;

	private static int position = 0;

	public static void setPosition(int position_) {
		position = position_;
	}

	public static void setupTable(int numColumns_, int numRows_) {

		numColumns = numColumns_;
		numRows = numRows_;

		times = new Object[numRows][numColumns];
		iterations = new long[numRows][numColumns];

		IntStream.range(0, times.length).forEach(x -> Arrays.setAll(times[x], y -> 0L));
		IntStream.range(0, iterations.length).forEach(x -> Arrays.setAll(iterations[x], y -> 0L));
	}

	public static void drawTable(String[] columns) {

		Integer[] columnsLen = new Integer[columns.length];

		IntStream.range(0, columnsLen.length).forEach(i -> columnsLen[i] = 10);

		AsciiTable at = AsciiTable.newTable(columnsLen);

		at.addRow(columns);

		IntStream.range(0, times.length).forEach(i -> at.addRow(times[i]));

		System.out.println(at.render());
	}

	public static void addValue2Table(Object value) {

		int x = Math.floorDiv(position, numColumns);
		int y = position % numColumns;

		times[x][y] = value;
		position++;
	}

	public static void addIterationValue2Table(Long elapsedTime) {

		int x = Math.floorDiv(position, numColumns);
		int y = position % numColumns;

		long iteration = ++iterations[x][y];

		times[x][y] = (Long) times[x][y] + Long.valueOf(elapsedTime / iteration);

		position++;
	}

}
