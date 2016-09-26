package com.masm.dbench;

import static com.masm.dbench.Constants.LIMIT_ALL;

public abstract class SQLHelper {

	public static enum Dialect {
		ORACLE, MYSQL, H2
	};

	public static String contructLimitQuery(String query, boolean oracle, int from, int to) {

		String result = "";

		if (from != LIMIT_ALL || to != LIMIT_ALL) {

			if (oracle) {
				result = "SELECT a.* FROM (SELECT b.*, ROWNUM b_rownum FROM (";
			}

			result += query;

			if (oracle) {
				result += ") b WHERE ROWNUM <= " + to + ") a WHERE b_rownum >= " + from;
			} else {
				result += " LIMIT " + from + ", " + to;
			}
		} else {
			result = query;
		}

		return result;

	}
}
