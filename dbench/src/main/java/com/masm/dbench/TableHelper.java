package com.masm.dbench;

import java.util.Arrays;
import java.util.stream.IntStream;

import de.vandermeer.skb.asciitable.AsciiTable;

public abstract class TableHelper {

	private static Object times[][];

	private static int numColumns;
	private static int numRows;
	private static int iterations = 1;
	private static int position = 0;

	public static void setPosition(int position_) {
		position = position_;
	}

	public static void setupTable(int numColumns_, int numRows_, int iterations_) {

		numColumns = numColumns_;
		numRows = numRows_;

		iterations = iterations_;

		times = new Object[numRows][numColumns];

		IntStream.range(0, times.length).forEach(x -> Arrays.setAll(times[x], y -> 0.0));
	}

	public static void drawTable(String[] columns) {

		Integer[] columnsLen = new Integer[columns.length];

		IntStream.range(0, columnsLen.length).forEach(i -> columnsLen[i] = 10);

		AsciiTable at = AsciiTable.newTable(columnsLen);

		at.addRow(columns);

		for (Object[] row : times) {
			IntStream.range(0, row.length).forEach(i -> {
				if (row[i] instanceof Double) {
					row[i] = Math.round((Double) row[i]);
				}
			});
		}

		IntStream.range(0, times.length).forEach(i -> at.addRow(times[i]));

		System.out.println(at.render());
	}

	public static void addValue2Table(Object value) {

		int x = Math.floorDiv(position, numColumns);
		int y = position % numColumns;

		times[x][y] = value;
		position++;
	}

	public static void addIterationValue2Table(Double elapsedTime) {

		int x = Math.floorDiv(position, numColumns);
		int y = position % numColumns;

		times[x][y] = (Double) times[x][y] + Double.valueOf(elapsedTime / iterations);

		position++;
	}

}
