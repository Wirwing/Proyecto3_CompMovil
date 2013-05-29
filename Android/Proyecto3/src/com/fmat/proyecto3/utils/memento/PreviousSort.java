package com.fmat.proyecto3.utils.memento;

/**
 * Clase que contiene los valores de las operaciones anteriores
 * @author Irving
 *
 */
public class PreviousSort implements PreviousSortToCareTaker,
		PreviousSortToOriginator {

	private int from;
	private int to;

	public PreviousSort(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public int getFrom() {
		return from;
	}

	@Override
	public int getTo() {
		return to;
	}

}
