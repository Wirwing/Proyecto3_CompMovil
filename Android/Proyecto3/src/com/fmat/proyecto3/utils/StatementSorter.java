package com.fmat.proyecto3.utils;

public abstract class StatementSorter {
	
	public static String[] rearrangeStatementsByKeys(String[] statements,
			int[] sortKeys) {

		if (statements == null || sortKeys == null
				|| statements.length - sortKeys.length != 0) {
			throw new IllegalArgumentException(
					"Arrays must have the same length");
		}

		String[] arrangedStaments = new String[statements.length];

		for (int i = 0; i < arrangedStaments.length; i++)
			arrangedStaments[i] = statements[sortKeys[i]];

		return arrangedStaments;

	}
	
	
}
