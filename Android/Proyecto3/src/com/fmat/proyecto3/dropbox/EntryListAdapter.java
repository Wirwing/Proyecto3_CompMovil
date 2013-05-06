package com.fmat.proyecto3.dropbox;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI.Entry;
import com.fmat.proyecto3.R;

/**
 * Adaptador para listas de objetos Entry de Dropbox
 * @author Fabián Castillo
 * 
 */
public class EntryListAdapter extends ArrayAdapter<Entry> {
	private Activity mActivity;
	private List<Entry> mEntries;
	private Entry mParent;

	/**
	 * Constructor de la clase
	 * @param activity La activida del listview de este adaptador
	 * @param entries Lista de objetos Entry que se mostrarán en el list view
	 * @param parent El objeto entry que reprsenta el directorio padre de los Entry de este
	 * adaptador. Un valor nulls significará que no se mostrará un elemento padre.
	 */
	public EntryListAdapter(Activity activity, List<Entry> entries, Entry parent) {
		super(activity, android.R.layout.simple_list_item_1, entries);
		this.mActivity = activity;
		if (parent != null) {
			entries.add(0, parent);
		}
		this.mEntries = entries;
		this.mParent = parent;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View itemView = prepareViewHolder(convertView, parent);
		ViewHolder holder = (ViewHolder) itemView.getTag();
		
		Entry entry = mEntries.get(position);
		if (entry != null) {
			if(mParent != entry){
				holder.textView.setText(entry.path.equals("/") ? "/" : entry.fileName());
				holder.imageView.setImageResource(R.drawable.folder);
				holder.button.setVisibility(View.VISIBLE);
				holder.button.setTag(position);
				
				// Se prepara el listener para el presionado del botón 'usar'
				// Si se presiona el botón se guarda en preferencias el directorio indicado
				// Y se finaliza la aplicación
				holder.button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
						Entry entry = mEntries.get(position);
						SharedPreferences.Editor editor = preferences.edit();
						String prefKey = mActivity.getString(R.string.pref_dropbox_dir);
						editor.putString(prefKey, entry.path);
						editor.commit();
						mActivity.finish();
					}
				});
				
			} else {
				holder.textView.setText("A " + entry.fileName());
				holder.imageView.setImageResource(R.drawable.arrow_up);
				holder.button.setVisibility(View.GONE);
			}

		}
		return itemView;
	}
	
	/**
	 * Prepara el view holder para un objeto view, de ser necesario 
	 * @param convertView El objeto view antiguo para reusar, si es posible.
	 * @param parent El padre al que esta vista estará asociada
	 * @return vista preparada
	 */
	private View prepareViewHolder(View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_file, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.button = (Button) convertView.findViewById(R.id.button);
			
			convertView.setTag(viewHolder);
			
		}
		return convertView;
	}
	
	/**
	 * Clase contenedor de elementos view para una fila del listview.
	 * Siguiendo el patrón ViewHolder
	 */
	static class ViewHolder{
		TextView textView;
		ImageView imageView;
		Button button;
	}

}