package com.fmat.proyecto3.utils;

/**
 * Ordenador de sentencias
 * @author Irving Caro
 *
 */
public abstract class StatementSorter {
	
	/**
	 * Reordena el conjunto de sentencias de acuerdo a las claves proporcionadas
	 * @param statements sentencias a ordenar
	 * @param sortKeys claves correspondientes
	 * @return sentencias ordenadas
	 */
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
