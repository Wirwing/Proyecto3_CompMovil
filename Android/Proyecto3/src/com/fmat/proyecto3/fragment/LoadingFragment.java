package com.fmat.proyecto3.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmat.proyecto3.R;

/**
 * Fragmento que muestra un mensaje mientras se realiza una operacion asincrona
 * larga.
 */
public class LoadingFragment extends Fragment {
	
	private static final String MESSAGE_PARAM = "MESSAGE_PARAM";

	private String message;

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento usandos los parametros
	 * proveidos
	 * 
	 * @param message	Mensaje indicativo
	 * @return
	 */
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

	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			message = getArguments().getString(MESSAGE_PARAM);
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
