package com.fmat.proyecto3.utils.memento;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class Sorter implements ISorter {

	private ArrayAdapter<String> adapter;
	private ArrayList<Integer> keys;

	public Sorter(ArrayAdapter<String> adapter) {

		this.adapter = adapter;

		keys = new ArrayList<Integer>();
		for (int i = 0; i < adapter.getCount(); i++) {
			keys.add(i);
		}

	}

	@Override
	public void restorePreviousSorting(PreviousSortToCareTaker memento) {

		int oldFrom = ((PreviousSortToOriginator) memento).getFrom();
		int oldTo = ((PreviousSortToOriginator) memento).getTo();

		if (oldFrom != oldTo) {
			swap(oldFrom, oldTo);
		}

	}

	@Override
	public PreviousSortToCareTaker swap(int from, int to) {

		Integer key = keys.get(from);
		keys.remove(key);
		keys.add(to, key);

		String item = adapter.getItem(from);
		adapter.remove(item);
		adapter.insert(item, to);

		return new PreviousSort(from, to);

	}

	@Override
	public int[] getKeys() {

		int[] ret = new int[keys.size()];
		int i = 0;
		for (Integer e : keys)
			ret[i++] = e.intValue();

		return ret;

	}

}
