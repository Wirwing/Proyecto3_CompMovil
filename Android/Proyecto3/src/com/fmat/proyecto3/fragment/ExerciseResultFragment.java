package com.fmat.proyecto3.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.R.raw;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragmento que muestra el resultado del ejercicio, es decir, el orden de las
 * sentencias como el usuario las dispuso.
 * 
 * Resuelve los comentarios del ejercicio y se los pasa a al actividad para
 * enviar todo al WS.
 * 
 * @author Irving
 * 
 */
public class ExerciseResultFragment extends SherlockFragment implements
		OnClickListener {

	private static final String EXERCISE_ID_PARAM = "EXERCISE_NUMBER_PARAM";
	private static final String EXERCISE_ELLAPSED_PARAM = "EXERCISE_ELLAPSED_PARAM";
	private static final String EXERCISE_STATEMENTS_PARAM = "EXERCISE_STATEMENTS_PARAM";

	private static final String EXERCISE_SOLVED_DATE_PARAM = "EXERCISE_SOLVED_DATE_PARAM";

	private static final String EXERCISE_SOLVED_LOCATION_PARAM = "EXERCISE_SOLVED_LOCATION_PARAM";
	private static final String EXERCISE_EXERCISE_LOCATION_PARAM = "EXERCISE_EXERCISE_LOCATION_PARAM";
	private static final String EXERCISE_EXERCISE_RADIUS_PARAM = "EXERCISE_EXERCISE_RADIUS_PARAM";

	private String id;
	private String ellapsedTime;
	private String[] statements;

	private OnResultListener listener;

	private EditText et_comments;
	private String comments;

	private GoogleMap map;

	private LatLng solvedLocation, exerciseLocation;

	private long date;

	private double radius;

	private CheckBox chx_dropbox;

	/**
	 * Esta interfaz debe ser implementada por las actividades que llaman a este
	 * fragmento, para poder comunicarse con la actividad.
	 */
	public interface OnResultListener {

		/**
		 * Enviar la respuesta con los comentarios
		 * 
		 * @param comments
		 * @param sendToDropbox
		 */
		public void onSendAnswer(String comments, boolean sendToDropbox);
	}

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento
	 * usandos los parametros proveidos
	 * 
	 * 
	 * @param id
	 *            Id del ejercio
	 * @param elapsedTimeInSeconds
	 *            Tiempo transcurrido
	 * @param statements
	 *            Sentencias ya ordenadas.
	 * @return
	 */
	public static ExerciseResultFragment newInstance(String id,
			int elapsedTimeInSeconds, String[] statements, long rawDate,
			LatLng solvedLocation, LatLng exerciseLocation, double radius) {

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

		args.putLong(EXERCISE_SOLVED_DATE_PARAM, rawDate);
		args.putParcelable(EXERCISE_SOLVED_LOCATION_PARAM, solvedLocation);

		if (exerciseLocation != null && radius != 0) {
			args.putDouble(EXERCISE_EXERCISE_RADIUS_PARAM, radius);
			args.putParcelable(EXERCISE_EXERCISE_LOCATION_PARAM,
					exerciseLocation);
		}

		fragment.setArguments(args);
		return fragment;

	}

	public ExerciseResultFragment() {
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			id = getArguments().getString(EXERCISE_ID_PARAM);
			ellapsedTime = getArguments().getString(EXERCISE_ELLAPSED_PARAM);
			statements = getArguments().getStringArray(
					EXERCISE_STATEMENTS_PARAM);
			solvedLocation = getArguments().getParcelable(
					EXERCISE_SOLVED_LOCATION_PARAM);

			date = getArguments().getLong(EXERCISE_SOLVED_DATE_PARAM);

			if (getArguments().containsKey(EXERCISE_EXERCISE_LOCATION_PARAM)) {
				exerciseLocation = getArguments().getParcelable(
						EXERCISE_EXERCISE_LOCATION_PARAM);
				radius = getArguments().getDouble(
						EXERCISE_EXERCISE_RADIUS_PARAM);
			}
		}
	}

	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_exercise_result,
				container, false);

		((TextView) rootView.findViewById(R.id.tv_exercise)).setText(id);

		((TextView) rootView.findViewById(R.id.tv_elapsed_time))
				.setText(ellapsedTime);

		String dateString = "";
		if (date > 0) {
			dateString = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy")
					.format(new Date(date));
		} else {
			dateString = "Hora no establecida.";
		}

		((TextView) rootView.findViewById(R.id.tv_solved_date))
				.setText(dateString);

		((Button) rootView.findViewById(R.id.btn_send_exercise))
				.setOnClickListener(this);

		((ListView) rootView.findViewById(R.id.lv_statements))
				.setAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, statements));

		et_comments = (EditText) rootView.findViewById(R.id.et_answer_comments);

		chx_dropbox = (CheckBox) rootView.findViewById(R.id.chkbx_dropbox);

		FragmentManager manager = ((SherlockFragmentActivity) getActivity())
				.getSupportFragmentManager();

		map = ((SupportMapFragment) manager
				.findFragmentById(R.id.exercise_result_map)).getMap();


		map.addMarker(new MarkerOptions().position(solvedLocation).title(
				"Finalizado aquí"));

		if (exerciseLocation != null && radius > 0)
			map.addCircle(new CircleOptions().center(exerciseLocation)
					.radius(radius).strokeColor(Color.BLUE).strokeWidth(2));

		return rootView;
	}

	/**
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
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

	/**
	 * @see com.actionbarsherlock.app.SherlockFragment#onDetach()
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		comments = et_comments.getText().toString();
		listener.onSendAnswer(comments, chx_dropbox.isChecked());

	}

}
