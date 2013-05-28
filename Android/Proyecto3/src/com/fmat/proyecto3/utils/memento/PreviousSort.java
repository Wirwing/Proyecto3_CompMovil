package com.fmat.proyecto3.utils.memento;

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
