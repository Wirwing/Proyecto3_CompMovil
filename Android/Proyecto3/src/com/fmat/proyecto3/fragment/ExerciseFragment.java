package com.fmat.proyecto3.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.fmat.proyecto3.R;
import com.mobeta.android.dslv.DragSortListView;

/**
 * Fragmento que muestra el ejercicio per se: La lista de sentencias a ordenar, el tiempo que ha transcurrido desde que el
 * fragmento se muestra en pantalla y un boton para finalizar el ejercicio.
 * 
 */
public class ExerciseFragment extends SherlockFragment {

	/**
	 * Las setencias del ejercicio como argumentos del Bundle
	 */
	public static final String STATEMENTS_PARAM = "STATEMENTS_PARAM";

	private ArrayAdapter<String> adapter;
	private ListView listView;
	private Chronometer chronometer;
	private ArrayList<Integer> keys;
	private OnExerciseListener listener;
	private String[] statements;

	/**
	 * Esta interfaz debe ser implementada por las actividades que llaman a este fragmento,
	 * para poder comunicarse con la actividad.
	 */
	public interface OnExerciseListener {
		
		/**
		 * Callback cuando un ejercicio se decide finalizar de resolver
		 * 
		 * @param Keys  El orden de la respuesta de las setencias
		 * @param time El tiempo en milisegundos que tomo resolver el ejercicio
		 *            
		 * @return nothing
		 */
		public void onFinishExcercise(int[] keys, long time);
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				
				Integer key = keys.get(from);
				keys.remove(key);
				keys.add(to, key);
				
				String item = adapter.getItem(from);
				adapter.remove(item);
				adapter.insert(item, to);
			}
		}
	};

	/**
	 * 
	 * Usa este metodo factory para crear una nueva instancia de este fragmento usandos los parametros
	 * proveidos
	 * 
	 * @param statements	Sentencias a ordenar
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
	 * Al crear, checa si han sido pasados argumentos al fragmento
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

			statements = getArguments().getStringArray(STATEMENTS_PARAM);
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

		keys = new ArrayList<Integer>();
		for (int i = 0; i < arrayList.size(); i++) {
			keys.add(i);
		}

		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.list_item_statement, R.id.tv_statement, arrayList);

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

	private class OnFinishExerciseListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			chronometer.stop();

			long elapsedMillis = SystemClock.elapsedRealtime()
					- chronometer.getBase();

			int[] ret = new int[keys.size()];
			int i = 0;
			for (Integer e : keys)
				ret[i++] = e.intValue();

			listener.onFinishExcercise(ret, elapsedMillis);
		}

	}

}
