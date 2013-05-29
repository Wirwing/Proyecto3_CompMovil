package com.fmat.proyecto3.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.R;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.utils.animation.MarkerPulseAnimation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Un fragmento que muestra la pantalla principal de la aplicacion.
 */
public class MainFragment extends SherlockFragment implements OnClickListener {

	private static final String TAG = MainFragment.class.getName();

	private static final String STUDENT_NUMBER_PARAM = "STUDENT_NUMBER_PARAM";
	private static final String STUDENT_NAME_PARAM = "STUDENT_NAME_PARAM";
	private static final String DEGREE_PARAM = "DEGREE_PARAM";
	private static final String EXERCISES_PARAM = "EXERCISES_PARAM";

	private String number;
	private String name;
	private String degree;
	private ArrayList<Exercise> exercises;

	private TextView tv_exercise_date;

	private OnExerciseSelectedListener listener;
	private Spinner sp_exercises;

	private Exercise selected;

	private LatLng currentExercisePoint;
	private double radius;

	private float[] result;

	private Button playButton;

	private boolean dateExpirated;

	private GoogleMap map;

	private MarkerPulseAnimation animator;

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento
	 * usandos los parametros proveidos
	 * 
	 * @param number
	 *            Matricula del estudiante
	 * @param name
	 *            Nombre del estudiante
	 * @param degree
	 *            Licenciatura del estudiante
	 * 
	 * @return Una nueva instancia del fragmento
	 */
	// TODO: Rename and change types and number of parameters
	public static MainFragment newInstance(String number, String name,
			String degree, ArrayList<Exercise> exercises) {

		MainFragment fragment = new MainFragment();

		Bundle args = new Bundle();

		args.putString(STUDENT_NUMBER_PARAM, number);
		args.putString(STUDENT_NAME_PARAM, name);
		args.putString(DEGREE_PARAM, degree);
		args.putParcelableArrayList(EXERCISES_PARAM, exercises);
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
			exercises = getArguments().getParcelableArrayList(EXERCISES_PARAM);

			result = new float[1];

			dateExpirated = true;

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

		playButton = ((Button) rootView.findViewById(R.id.btn_play));
		playButton.setOnClickListener(this);

		tv_exercise_date = (TextView) rootView
				.findViewById(R.id.tv_exercise_date);

		sp_exercises = (Spinner) rootView.findViewById(R.id.sp_excercise);

		ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(
				getActivity(), android.R.layout.simple_spinner_item, exercises);

		sp_exercises.setAdapter(adapter);

		FragmentManager manager = ((SherlockFragmentActivity) getActivity())
				.getSupportFragmentManager();

		map = ((SupportMapFragment) manager.findFragmentById(R.id.exercise_map))
				.getMap();

		map.setMyLocationEnabled(true);

		animator = new MarkerPulseAnimation(map);

		sp_exercises.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				animator.cancel();

				selected = exercises.get(position);

				boolean enabled = true;

				long exerciseDate = selected.getDate() * 1000;

				String dateString = null;
				if (exerciseDate > 0) {

					dateString = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy")
							.format(new Date(exerciseDate));

					tv_exercise_date.setText(dateString);

					if (exerciseDate < new Date().getTime()) {

						tv_exercise_date.setTextColor(Color.RED);

						enabled = true;

						dateExpirated = true;

					} else {

						dateExpirated = false;

					}

				} else {

					dateString = "Hora no establecida.";
					tv_exercise_date.setText(dateString);

					dateExpirated = false;

				}

				if (selected.getPlace() != null) {

					enabled = false;

					currentExercisePoint = new LatLng(selected.getPlace()[0],
							selected.getPlace()[1]);

					radius = selected.getPlace()[2];

					animator.animate(currentExercisePoint, radius);

				} else {

					currentExercisePoint = null;

				}

				playButton.setEnabled(enabled);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

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
		public void onExerciseSelected(Exercise exercise);
	}

	/**
	 * Al dar click, iniciar la descarga del ejercicio por la actividad.
	 */
	@Override
	public void onClick(View v) {

		Exercise selected = (Exercise) sp_exercises.getSelectedItem();

		if (selected == null) {
			Toast.makeText(getActivity(), "Selecciona un ejercicio primero",
					Toast.LENGTH_SHORT).show();
		} else {
			listener.onExerciseSelected(selected);
		}
	}

	public void onLocationChanged(Location location) {

		if (currentExercisePoint != null && radius > 0) {

			Location.distanceBetween(location.getLatitude(),
					location.getLongitude(), currentExercisePoint.latitude,
					currentExercisePoint.longitude, result);

			int color = Color.RED;
			boolean enabled = false;

			if (radius > result[0]) {
				color = Color.BLUE;
				enabled = true;
			}

			animator.setColor(color);
			setPlayEnabled(enabled);

		}

		// Log.i(TAG,
		// "New location: " + location.getLatitude() + ", "
		// + location.getLongitude());
	}

	private void setPlayEnabled(boolean enabled) {

		if (playButton.isEnabled() != enabled && !dateExpirated) {
			playButton.setEnabled(enabled);
		}

	}

}
