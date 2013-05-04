package com.fmat.proyecto3.dropbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private Context mContext;
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
	public EntryListAdapter(Context context, List<Entry> entries, Entry parent) {
		super(context, android.R.layout.simple_list_item_1, entries);
		this.mContext = context;
		if (parent != null) {
			entries.add(0, parent);
			super.insert(parent, 0);
		}
		this.mEntries = entries;
		this.mParent = parent;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		if (itemView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//itemView = inflater.inflate(R.layout.list_item_file, parent, false);
			itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}

		Entry entry = mEntries.get(position);
		if (entry != null) {
			/*TextView tvName = (TextView) itemView.findViewById(R.id.TextView01);
			TextView tvData = (TextView) itemView.findViewById(R.id.TextView02);
			TextView tvDate = (TextView) itemView
					.findViewById(R.id.TextViewDate);
			Button selectButton = (Button) itemView
					.findViewById(R.id.fc_select);*/
			TextView tv = (TextView) itemView.findViewById(android.R.id.text1);
			if (mParent != entry) {
				/*tvName.setText(entry.fileName());
				tvData.setText(entry.size);
				tvDate.setText(entry.modified);
				selectButton.setVisibility(View.VISIBLE);*/
				tv.setText(entry.fileName());
			} else {
				tv.setText("^UP");
				/*tvName.setText("..");
				tvData.setText(R.string.up);
				tvDate.setText("");
				selectButton.setVisibility(View.INVISIBLE);*/
			}

		}
		return itemView;
	}

}