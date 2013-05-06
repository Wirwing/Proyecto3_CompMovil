package com.fmat.proyecto3.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fmat.proyecto3.R;

public class StatementAdapter extends ArrayAdapter<String> {

	private List<String> statements;
	private LayoutInflater inflater;
	
	public StatementAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		statements = objects;
	}

	@Override
	public int getCount() {
		if (statements != null)
			return statements.size();
		else
			return 0;
	}

	@Override
	public String getItem(int position) {
		return statements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void remove(String object) {
		// TODO Auto-generated method stub
		statements.remove(object);
		notifyDataSetChanged();
	}
	
	@Override
	public void insert(String object, int index) {
		statements.add(index, object);
		notifyDataSetChanged();
	}
	
	public List<String> getAll(){
		return statements;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_statement, null);
			holder = new ViewHolder();
			holder.statement = (TextView) convertView
					.findViewById(R.id.tv_statement);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.statement.setText(statements.get(position));

		return convertView;

	}

	public static class ViewHolder {
		public TextView statement;
	}

}