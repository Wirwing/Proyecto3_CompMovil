package com.fmat.proyecto3;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobeta.android.dslv.DragSortListView;

public class ExerciseActivity extends Activity {

	private ArrayAdapter<String> adapter;

	private int[] keys;

	private ListView listView;

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {

				int temp = keys[from];
				keys[from] = keys[to];
				keys[to] = temp;

				String item = adapter.getItem(from);
				adapter.remove(item);
				adapter.insert(item, to);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);

		listView = (ListView) findViewById(R.id.list_statements);

		setAdapterAndKeys();

	}

	private void setAdapterAndKeys() {

		String[] array = getResources()
				.getStringArray(R.array.dummy_statements);
		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(array));

		keys = new int[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			keys[i] = i;
		}

		adapter = new ArrayAdapter<String>(this, R.layout.list_item_statement,
				R.id.tv_statement, arrayList);
		listView.setAdapter(adapter);
		((DragSortListView) listView).setDropListener(onDrop);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this
	 * adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.exercise, menu); return true; }
	 */
	public void showKeys(View v) {

		for (int i : keys)
			Log.i("Exercise", "Key: " + String.valueOf(i));

	}

}
