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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;

/**
 * Un fragmento que muestra la pantalla principal de la aplicacion.
 */
public class MainFragment extends SherlockFragment implements OnClickListener {

	private static final String STUDENT_NUMBER_PARAM = "STUDENT_NUMBER_PARAM";

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String STUDENT_NAME_PARAM = "STUDENT_NAME_PARAM";
	private static final String DEGREE_PARAM = "DEGREE_PARAM";

	private String number;
	private String name;
	private String degree;

	private EditText et_exercise_number;
	private OnExerciseSelectedListener listener;

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento usandos los parametros
	 * proveidos
	 * 
	 * @param number	Matricula del estudiante
	 * @param name		Nombre del estudiante
	 * @param degree	Licenciatura del estudiante
	 * 
	 * @return Una nueva instancia del fragmento
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

	/**
	 * Constructor
	 */
	public MainFragment() {
		// Required empty public constructor
	}

	
	/**
	 * Al crear, checa si han sido pasados argumentos al fragmento
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			name = getArguments().getString(STUDENT_NAME_PARAM);
			number = getArguments().getString(STUDENT_NUMBER_PARAM);
			degree = getArguments().getString(DEGREE_PARAM);
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

		((Button) rootView.findViewById(R.id.btn_play))
				.setOnClickListener(this);

		et_exercise_number = (EditText) rootView
				.findViewById(R.id.et_excercise_number);

	}

	/**
	 * Obtener el callback a la actividad al adherirla a ella.
	 */
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

	/**
	 * Al quitar el fragmento, quitar el callback a la actividad.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	/**
	 * Esta interfaz debe ser implementada por las actividades que llaman a este
	 * fragmento, para poder comunicarse con la actividad.
	 */
	public interface OnExerciseSelectedListener {
		/**
		 * Callback cuando se elige un ejercicio
		 * 
		 * @param number
		 *            Id del ejercicio
		 * 
		 * @return nothing
		 */
		public void onExerciseSelected(String number);
	}

	/**
	 * Al dar click, iniciar la descarga del ejercicio por la actividad.
	 */
	@Override
	public void onClick(View v) {

		String number = et_exercise_number.getText().toString();

		if (number.isEmpty()) {
			Toast.makeText(getActivity(), "Selecciona un ejercicio primero",
					Toast.LENGTH_SHORT).show();
		} else
			listener.onExerciseSelected(number);

	}

}
