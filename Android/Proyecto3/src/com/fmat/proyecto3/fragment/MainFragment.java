package com.fmat.proyecto3.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MainFragment.OnExerciseSelectedListener} interface to handle
 * interaction events. Use the {@link MainFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class MainFragment extends SherlockFragment implements OnClickListener {

	private static final String STUDENT_NUMBER_PARAM = "NUMBER_PARAM";

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String STUDENT_NAME_PARAM = "NAME_PARAM";
	private static final String DEGREE_PARAM = "DEGREE_PARAM";

	private String number;
	private String name;
	private String degree;

	private EditText et_exercise_number;

	private OnExerciseSelectedListener listener;

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
	public static MainFragment newInstance(String number, String name,
			String degree) {

		MainFragment fragment = new MainFragment();

		Bundle args = new Bundle();

		args.putString(STUDENT_NUMBER_PARAM, number);
		args.putString(STUDENT_NAME_PARAM, name);
		args.putString(DEGREE_PARAM, degree);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			name = getArguments().getString(STUDENT_NAME_PARAM);
			number = getArguments().getString(STUDENT_NUMBER_PARAM);
			degree = getArguments().getString(DEGREE_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		initView(rootView);
		
		return rootView;
	}

	private void initView(View rootView) {

		((TextView) rootView.findViewById(R.id.tv_student_number))
				.setText(number);
		((TextView) rootView.findViewById(R.id.tv_name)).setText(name);
		((TextView) rootView.findViewById(R.id.tv_degree)).setText(degree);

		((Button)rootView.findViewById(R.id.btn_play)).setOnClickListener(this);
		
		et_exercise_number = (EditText)rootView.findViewById(R.id.et_excercise_number);
		
	}

	// TODO: Rename method, update argument and hook method into UI event
	// public void onButtonPressed(Uri uri) {
	// if (listener != null) {
	// listener.onExerciseSelected(uri);
	// }
	// }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnExerciseSelectedListener) activity;
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

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnExerciseSelectedListener {
		// TODO: Update argument type and name
		public void onExerciseSelected(String number);
	}

	@Override
	public void onClick(View v) {
		
		String number = et_exercise_number.getText().toString();
		listener.onExerciseSelected(number);
		
	}

}
