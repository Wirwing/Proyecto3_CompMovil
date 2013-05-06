package com.fmat.proyecto3.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;
import com.mobeta.android.dslv.DragSortListView;

public class ExerciseFragment extends SherlockFragment {

	public static final String STATEMENTS_PARAM = "STATEMENTS_PARAM";

	private ArrayAdapter<String> adapter;
	private ListView listView;
	private Chronometer chronometer;

	private ArrayList<Integer> keys;

	private OnExerciseListener listener;

	private String[] statements;

	public interface OnExerciseListener {
		public void onFinishExcercise(int[] keys, long time);
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				
				Integer key = keys.get(from);
				keys.remove(key);
				keys.add(to, key);
				
				String item = adapter.getItem(from);
				adapter.remove(item);
				adapter.insert(item, to);
			}
		}
	};

	public static ExerciseFragment newInstance(String[] statements) {

		ExerciseFragment fragment = new ExerciseFragment();

		Bundle args = new Bundle();
		args.putStringArray(STATEMENTS_PARAM, statements);
		fragment.setArguments(args);
		return fragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

			statements = getArguments().getStringArray(STATEMENTS_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_exercise, container,
				false);

		listView = (ListView) rootView.findViewById(R.id.list_statements);

		((Button) rootView.findViewById(R.id.btn_finish_exercise))
				.setOnClickListener(new OnFinishExerciseListener());

		chronometer = (Chronometer) rootView.findViewById(R.id.chronometer);

		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();

		setAdapterAndKeys();

		return rootView;
	}

	private void setAdapterAndKeys() {

		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(statements));

		keys = new ArrayList<Integer>();
		for (int i = 0; i < arrayList.size(); i++) {
			keys.add(i);
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.list_item_statement, R.id.tv_statement, arrayList);

		listView.setAdapter(adapter);
		((DragSortListView) listView).setDropListener(onDrop);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnExerciseListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	private class OnFinishExerciseListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			chronometer.stop();

			long elapsedMillis = SystemClock.elapsedRealtime()
					- chronometer.getBase();

			int[] ret = new int[keys.size()];
			int i = 0;
			for (Integer e : keys)
				ret[i++] = e.intValue();

			listener.onFinishExcercise(ret, elapsedMillis);
		}

	}

}
