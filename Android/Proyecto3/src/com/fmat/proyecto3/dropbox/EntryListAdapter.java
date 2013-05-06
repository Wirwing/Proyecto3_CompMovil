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
 * List Adapter for Dropbox's Entry objects.
 * 
 * @author Fabián Castillo
 * 
 */
public class EntryListAdapter extends ArrayAdapter<Entry> {
	private Activity mActivity;
	private List<Entry> mEntries;
	private Entry mParent;

	/**
	 * Class constructor
	 * 
	 * @param context
	 *            The current context
	 * @param entries
	 *            List of entries to represent in the ListView
	 * @param parent
	 *            The entry that represents the parent folder for the entries of
	 *            this adapter. Null means no parent will be displayed.
	 */
	public EntryListAdapter(Activity activity, List<Entry> entries, Entry parent) {
		super(activity, android.R.layout.simple_list_item_1, entries);
		this.mActivity = activity;
		if (parent != null) {
			entries.add(0, parent);
			//super.insert(parent, 0);
		}
		this.mEntries = entries;
		this.mParent = parent;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View itemView = prepareView(convertView, parent);
		ViewHolder holder = (ViewHolder) itemView.getTag();
		
		Entry entry = mEntries.get(position);
		if (entry != null) {
			if(mParent != entry){
				holder.textView.setText(entry.path.equals("/") ? "/" : entry.fileName());
				holder.imageView.setImageResource(R.drawable.folder);
				holder.button.setVisibility(View.VISIBLE);
				holder.button.setTag(position);
				
				holder.button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//SharedPreferences preferences = mActivity.getSharedPreferences("preferences", Context.MODE_PRIVATE);
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
	
	private View prepareView(View convertView, ViewGroup parent){
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
	
	static class ViewHolder{
		TextView textView;
		ImageView imageView;
		Button button;
	}

}