package com.fmat.proyecto3.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;
import com.fmat.proyecto3.utils.memento.ISorter;
import com.fmat.proyecto3.utils.memento.PreviousSortToCareTaker;
import com.fmat.proyecto3.utils.memento.Sorter;
import com.mobeta.android.dslv.DragSortListView;

/**
 * Fragmento que muestra el ejercicio per se: La lista de sentencias a ordenar,
 * el tiempo que ha transcurrido desde que el fragmento se muestra en pantalla y
 * un boton para finalizar el ejercicio.
 * 
 */
public class ExerciseFragment extends SherlockFragment implements
		SensorEventListener {

	private static final String TAG = ExerciseFragment.class.getName();

	/**
	 * Las setencias del ejercicio como argumentos del Bundle
	 */
	public static final String STATEMENTS_PARAM = "STATEMENTS_PARAM";

	private final float UNDO_TOTAL_NOISE = (float) 25.0;
	private final float UNDO_NOISE = (float) 10.0;

	private float mLastX, mLastY;
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	private ISorter sorter;

	private boolean isDialogShown;

	private LinkedList<PreviousSortToCareTaker> steps;

	private ArrayAdapter<String> adapter;

	private ListView listView;
	private Chronometer chronometer;

	private OnExerciseListener listener;
	private String[] statements;

	/**
	 * Esta interfaz debe ser implementada por las actividades que llaman a este
	 * fragmento, para poder comunicarse con la actividad.
	 */
	public interface OnExerciseListener {

		/**
		 * Callback cuando un ejercicio se desea terminar de resolver
		 * 
		 * @param Keys
		 *            El orden de la respuesta de las sentencias
		 * @param time
		 *            El tiempo en milisegundos que tomo resolver el ejercicio
		 * 
		 * @return nothing
		 */
		public void onFinishExcercise(int[] keys, long time);
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {

		/**
		 * Hace el intercambio de sentencias y genera un estado con el orden de
		 * las sentencias en este momento.
		 */
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				steps.add(sorter.swap(from, to));
			}
		}
	};

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento
	 * usandos los parametros proveidos
	 * 
	 * @param statements
	 *            Sentencias a ordenar
	 * @return Una nueva instancia del fragmento
	 */
	public static ExerciseFragment newInstance(String[] statements) {

		ExerciseFragment fragment = new ExerciseFragment();

		Bundle args = new Bundle();
		args.putStringArray(STATEMENTS_PARAM, statements);
		fragment.setArguments(args);
		return fragment;

	}

	/**
	 * Al crear, checa si han sido pasados argumentos al fragmento.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

			statements = getArguments().getStringArray(STATEMENTS_PARAM);
			isDialogShown = false;

		}

		mSensorManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener(this);
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

		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.list_item_statement, R.id.tv_statement, arrayList);

		sorter = new Sorter(adapter);
		steps = new LinkedList<PreviousSortToCareTaker>();

		listView.setAdapter(adapter);
		((DragSortListView) listView).setDropListener(onDrop);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	/**
	 * Obtener el callback a la actividad al adherirla a ella.
	 */
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

	/**
	 * Al quitar el fragmento, quitar el callback a la actividad.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	/**
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor,
	 *      int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	/*
	 * @see
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware
	 * .SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {

		float x = event.values[0];
		float y = event.values[1];

		if (!mInitialized) {

			mLastX = x;
			mLastY = y;
			mInitialized = true;

		} else {

			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			if (deltaX < UNDO_TOTAL_NOISE)
				deltaX = (float) 0.0;
			if (deltaY < UNDO_TOTAL_NOISE)
				deltaY = (float) 0.0;
			mLastX = x;
			mLastY = y;

			//Gesto para deshacer un paso
			if (x > UNDO_NOISE && deltaY == 0) {
				undoStep();
				return;
			}

			//Gesto para deshacer todo
			if (deltaY > 0)
				undoAll();

		}

	}

	/**
	 * Restablece a un paso anterior la ordenacion de sentencias, si es que ha habido movimiento.
	 */
	private void undoStep() {

		if (!steps.isEmpty()) {

			if (isDialogShown)
				return;

			isDialogShown = true;

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("¿Deseas deshacer movimiento?")
					.setCancelable(false)
					.setPositiveButton("Si",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									sorter.restorePreviousSorting(steps
											.removeLast());

									isDialogShown = false;

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									isDialogShown = false;

									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		} else {
			Toast.makeText(getActivity(), "No hay movimientos para deshacer",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Reestablece la vista a la ordenacion original del ejercicio
	 */
	private void undoAll() {

		if (!isDialogShown) {

			isDialogShown = true;

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("¿Deseas reiniciar ejercicio?")
					.setCancelable(false)
					.setPositiveButton("Si",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									isDialogShown = false;
									ArrayList<String> arrayList = new ArrayList<String>(
											Arrays.asList(statements));

									adapter = new ArrayAdapter<String>(
											getActivity(),
											R.layout.list_item_statement,
											R.id.tv_statement, arrayList);
									listView.setAdapter(adapter);

									sorter = new Sorter(adapter);
									steps.clear();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									isDialogShown = false;

									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	/**
	 * Clase callback para notificar a la actividad que contiene a este 
	 * fragmento cuando un ejercicio ha sido completado.
	 */
	private class OnFinishExerciseListener implements OnClickListener {

		/*
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {

			chronometer.stop();

			long elapsedMillis = SystemClock.elapsedRealtime()
					- chronometer.getBase();

			int[] keys = sorter.getKeys();

			listener.onFinishExcercise(keys, elapsedMillis);
		}

	}

}
