package com.fmat.proyecto3.utils.memento;


public interface ISorter {

	// set Memento
	public void restorePreviousSorting(PreviousSortToCareTaker memento);

	// Actual services
	public PreviousSortToCareTaker swap(int from, int to);

	public int[] getKeys();

	// public ArrayAdapter<String> getAdapter();

}
