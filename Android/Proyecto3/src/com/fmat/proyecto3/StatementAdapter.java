package com.fmat.proyecto3;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StatementAdapter extends BaseAdapter {

	private HashMap<Integer, String> statements;
	private LayoutInflater inflater;

	public StatementAdapter(Context ctx) {
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (statements != null)
			return statements.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return statements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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
