package com.fmat.proyecto3.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;

/**
 * Fragmento que muestra la descripcion del ejercicio a resolver.
 */
public class ExerciseDescriptionFragment extends SherlockFragment implements
		OnClickListener {

	private static final String EXERCISE_ID_PARAM = "EXERCISE_NUMBER_PARAM";
	private static final String EXERCISE_DESCRIPTION_PARAM = "EXERCISE_DESCRIPTION_PARAM";

	private String id;
	private String description;
	private OnDescriptionListener listener;

	/**
	 * Esta interfaz debe ser implementada por las actividades que llaman a este
	 * fragmento, para poder comunicarse con la actividad.
	 */
	public interface OnDescriptionListener {

		/**
		 * Callback cuando se decide iniciar el ejercicio
		 * 
		 * @return nothing
		 */
		public void onStartExcercise();

	}

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento
	 * usandos los parametros proveidos
	 * 
	 * @param id
	 *            Id del ejercicio
	 * @param description
	 *            Descripcion del ejercicio
	 * @return Una nueva instancia del fragmento
	 */
	public static ExerciseDescriptionFragment newInstance(String id,
			String description) {
		return ExerciseDescriptionFragment.newInstance(id, description, null);
	}

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento
	 * usandos los parametros proveidos
	 * 
	 * @param id
	 *            Id del ejercicio
	 * @param description
	 *            Descripcion del ejercicio
	 * @param date
	 *            Fecha del ejercicio
	 * @return Una nueva instancia del fragmento
	 */
	public static ExerciseDescriptionFragment newInstance(String id,
			String description, Long date) {

		if (date == null) {
			date = (long) -1;
		}

		ExerciseDescriptionFragment fragment = new ExerciseDescriptionFragment();
		Bundle args = new Bundle();
		args.putString(EXERCISE_ID_PARAM, id);
		args.putString(EXERCISE_DESCRIPTION_PARAM, description);
		fragment.setArguments(args);
		return fragment;

	}

	/**
	 * Constructor
	 */
	public ExerciseDescriptionFragment() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Al crear, checa si han sido pasados argumentos al fragmento
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			id = getArguments().getString(EXERCISE_ID_PARAM);
			description = getArguments().getString(EXERCISE_DESCRIPTION_PARAM);
		}
	}

	/**
	 * Infla la vista raiz, obtiene los elementos vista que son nodos hijos de
	 * esta raiz, les asigna los valores y listener a estos hijos.
	 * 
	 * Devuelve la vista inflada, que es establecida a este fragmente y
	 * desplegada en pantalla.
	 * 
	 * @return view vista inflada.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_description,
				container, false);

		((TextView) rootView.findViewById(R.id.tv_exercise_id)).setText(id);
		((TextView) rootView.findViewById(R.id.tv_exercise_description))
				.setText(description);

		((Button) rootView.findViewById(R.id.btn_start_exercise))
				.setOnClickListener(this);

		return rootView;
	}

	/**
	 * Obtener el callback a la actividad al adherirla a ella.
	 */
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

	/**
	 * Al quitar el fragmento, quitar el callback a la actividad.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	/**
	 * Llama al callback para iniciar el ejercicio
	 */
	@Override
	public void onClick(View v) {
		listener.onStartExcercise();

	}

}
