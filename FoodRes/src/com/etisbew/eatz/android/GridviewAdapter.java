package com.etisbew.eatz.android;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class GridviewAdapter extends BaseAdapter {
	private Activity activity;
	List<String> list, pointsList;
	// used to keep selected position in ListView
	private int selectedPos = -1; // init value for not-selected

	public GridviewAdapter(Activity activity, List<String> list,
			List<String> slotPoints) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.list = list;
		pointsList = slotPoints;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSelectedPosition(int pos) {
		selectedPos = pos;
		// inform the view of this change
		notifyDataSetChanged();
	}

	public int getSelectedPosition() {
		return selectedPos;
	}

	public static class ViewHolder {

		Button txtSlot;
	}

	int pos;

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		LayoutInflater inflator = activity.getLayoutInflater();
		final String slot = list.get(position);
		pos = position;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.mylist_row, null);

			holder.txtSlot = (Button) convertView.findViewById(R.id.txt_slot);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtSlot.setText(slot);

		if (pointsList.get(pos).equalsIgnoreCase("1000")) {
			holder.txtSlot.setBackgroundResource(R.drawable.tho_box_u);
		} else if (pointsList.get(pos).equalsIgnoreCase("500")) {
			holder.txtSlot.setBackgroundResource(R.drawable.five_box_u);
		} else if (pointsList.get(pos).equalsIgnoreCase("200")) {
			holder.txtSlot.setBackgroundResource(R.drawable.two_box_u);
		} else {
			holder.txtSlot.setBackgroundResource(R.drawable.one_box_u);
		}

		if (selectedPos == pos) {
			if (pointsList.get(pos).equalsIgnoreCase("1000")) {
				holder.txtSlot.setBackgroundResource(R.drawable.tho_box_s);
			} else if (pointsList.get(pos).equalsIgnoreCase("500")) {
				holder.txtSlot.setBackgroundResource(R.drawable.five_box_s);
			} else if (pointsList.get(pos).equalsIgnoreCase("200")) {
				holder.txtSlot.setBackgroundResource(R.drawable.two_box_s);
			} else {
				holder.txtSlot.setBackgroundResource(R.drawable.one_box_s);
			}
		} else {
			if (pointsList.get(pos).equalsIgnoreCase("1000")) {
				holder.txtSlot.setBackgroundResource(R.drawable.tho_box_u);
			} else if (pointsList.get(pos).equalsIgnoreCase("500")) {
				holder.txtSlot.setBackgroundResource(R.drawable.five_box_u);
			} else if (pointsList.get(pos).equalsIgnoreCase("200")) {
				holder.txtSlot.setBackgroundResource(R.drawable.two_box_u);
			} else {
				holder.txtSlot.setBackgroundResource(R.drawable.one_box_u);
			}
		}

		return convertView;
	}
}
