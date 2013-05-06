package com.fmat.proyecto3.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;

public class ExerciseDescriptionFragment extends SherlockFragment implements
		OnClickListener {

	private static final String EXERCISE_ID_PARAM = "EXERCISE_NUMBER_PARAM";
	private static final String EXERCISE_DESCRIPTION_PARAM = "EXERCISE_DESCRIPTION_PARAM";

	private String id;
	private String description;

	private OnDescriptionListener listener;

	public interface OnDescriptionListener {
		// TODO: Update argument type and name
		public void onStartExcercise();
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
	public static ExerciseDescriptionFragment newInstance(String id,
			String description) {

		ExerciseDescriptionFragment fragment = new ExerciseDescriptionFragment();
		Bundle args = new Bundle();
		args.putString(EXERCISE_ID_PARAM, id);
		args.putString(EXERCISE_DESCRIPTION_PARAM, description);
		fragment.setArguments(args);
		return fragment;

	}

	public ExerciseDescriptionFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			id = getArguments().getString(EXERCISE_ID_PARAM);
			description = getArguments().getString(EXERCISE_DESCRIPTION_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_description,
				container, false);

		((TextView) rootView.findViewById(R.id.tv_exercise_id)).setText(id);
		((TextView) rootView.findViewById(R.id.tv_exercise_description))
				.setText(description);

		((Button)rootView.findViewById(R.id.btn_start_exercise)).setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnDescriptionListener) activity;
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
		listener.onStartExcercise();
	}

}
