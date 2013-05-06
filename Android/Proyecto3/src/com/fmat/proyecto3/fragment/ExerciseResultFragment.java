package com.fmat.proyecto3.fragment;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;

public class ExerciseResultFragment extends SherlockFragment implements
		OnClickListener {

	private static final String EXERCISE_ID_PARAM = "EXERCISE_NUMBER_PARAM";
	private static final String EXERCISE_ELLAPSED_PARAM = "EXERCISE_ELLAPSED_PARAM";
	private static final String EXERCISE_STATEMENTS_PARAM = "EXERCISE_STATEMENTS_PARAM";

	private String id;
	private String ellapsedTime;
	private String[] statements; 
	
	private OnResultListener listener;

	private EditText et_comments;
	private String comments;
	
	public interface OnResultListener {
		// TODO: Update argument type and name
		public void onSendAnswer(String comments);
	}

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment MainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ExerciseResultFragment newInstance(String id, int elapsedTimeInSeconds, String[] statements) {

		long millis = TimeUnit.SECONDS.toMillis(elapsedTimeInSeconds);
		
		String elapsedTime = String.format(
				"Tiempo: %d minutos, %d segundos",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		
		ExerciseResultFragment fragment = new ExerciseResultFragment();
		Bundle args = new Bundle();
		args.putString(EXERCISE_ID_PARAM, "Ejercicio " + id);
		args.putString(EXERCISE_ELLAPSED_PARAM, elapsedTime);
		args.putStringArray(EXERCISE_STATEMENTS_PARAM, statements);
		fragment.setArguments(args);
		return fragment;

	}

	public ExerciseResultFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			id = getArguments().getString(EXERCISE_ID_PARAM);
			ellapsedTime = getArguments().getString(EXERCISE_ELLAPSED_PARAM);
			statements = getArguments().getStringArray(EXERCISE_STATEMENTS_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_exercise_result,
				container, false);

		((TextView) rootView.findViewById(R.id.tv_exercise)).setText(id);
		((TextView) rootView.findViewById(R.id.tv_elapsed_time)).setText(ellapsedTime);
		((Button)rootView.findViewById(R.id.btn_send_exercise)).setOnClickListener(this);
		
		((ListView)rootView.findViewById(R.id.lv_statements)).setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1, statements));
		
		et_comments = (EditText)rootView.findViewById(R.id.et_answer_comments);
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnResultListener) activity;
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

	@Override
	public void onClick(View v) {
		
		comments = et_comments.getText().toString();
		listener.onSendAnswer(comments);
		
	}

}
