package com.fmat.proyecto3.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmat.proyecto3.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link LoadingFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link LoadingFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class LoadingFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String MESSAGE_PARAM = "MESSAGE_PARAM";

	// TODO: Rename and change types of parameters
	private String message;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment LoadingFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static LoadingFragment newInstance(String message) {
		LoadingFragment fragment = new LoadingFragment();
		Bundle args = new Bundle();
		args.putString(MESSAGE_PARAM, message);
		fragment.setArguments(args);
		return fragment;
	}

	public LoadingFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			message = getArguments().getString(MESSAGE_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_loading, container,
				false);

		((TextView) rootView.findViewById(R.id.tv_message)).setText(message);

		return rootView;
	}
}
