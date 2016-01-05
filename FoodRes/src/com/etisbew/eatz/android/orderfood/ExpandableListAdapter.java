package com.etisbew.eatz.android.orderfood;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.etisbew.eatz.android.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements
		Filterable {

	public static String TAG_ITEM_QNTY = "";
	public ExpandableListView expandableList;
	private LayoutInflater inflater;
	OrderFood activity = null;
	int flag=1;
	public ArrayList<ExpandableListItems> items;

	// private int parentPosition;
	// private int childPosition;
	//
	// String
	// Strprice=items.get(parentPosition).getParentChildren().get(childPosition).get("price");
	// String
	// Strque=activity.itemWithQuantity.get(items.get(parentPosition).getParentChildren().get(childPosition).get("id"));

	public ArrayList<ExpandableListItems> getitems() {
		return items;
	}

	public ExpandableListAdapter(OrderFood context,
			ArrayList<ExpandableListItems> parentList) {
		this.activity = context;
		this.items = parentList;
		this.inflater = LayoutInflater.from(context);

	}

	// counts the number of group/parent items so the list knows how many
	// times calls getGroupView() method
	@Override
	public int getGroupCount() {
		return items.size();
	}

	// counts the number of children items so the list knows how many times
	// calls getChildView() method
	@Override
	public int getChildrenCount(int parentPosition) {
		int size = 0;
		if (items.get(parentPosition).getParentChildren() != null) {
			size = items.get(parentPosition).getParentChildren().size();
		}
		return size;
	}

	// gets the title of each parent/group
	@Override
	public String getGroup(int i) {

		return items.get(i).getParent();
	}

	// gets the name of each item
	@Override
	public Object getChild(int parentPosition, int childPosition) {
		return items.get(parentPosition).getParentChildren().get(childPosition);
	}

	@Override
	public long getGroupId(int parentPosition) {
		return parentPosition;
	}

	@Override
	public long getChildId(int i, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	} 

	// in this method you must set the text to see the parent/group on the list

	@Override
	public View getGroupView(int parentPosition, boolean b, View view,
			ViewGroup viewGroup) {
		if (view == null) {
			view = inflater.inflate(R.layout.group, viewGroup, false);
		}

		((TextView) view.findViewById(R.id.parentText)).setText(items.get(
				parentPosition).getParent());

		if (b) {
			view.setBackgroundResource(R.drawable.expend);
		} else {
			view.setBackgroundResource(R.drawable.normal);
		} 

		((ImageView) view.findViewById(R.id.iv_menubar))
				.setImageResource(b ? R.drawable.up_accessory
						: R.drawable.down_accessory);

		return view; 

	} 

	// in this method you must set the text to see the children on the list

	@Override
	public View getChildView(int parentPosition, int childPosition, boolean b,
			View view, ViewGroup viewGroup) {
		if (view == null)
			view = inflater.inflate(R.layout.row, viewGroup, false);
		((TextView) view.findViewById(R.id.tv_label)).setText(items
				.get(parentPosition).getParentChildren().get(childPosition)
				.get("title"));
		((TextView) view.findViewById(R.id.tv_rate)).setText(activity
				.getString(R.string.oneRupee)
				+ " "
				+ items.get(parentPosition).getParentChildren()
						.get(childPosition).get("price"));

		ImageView vegtype = (ImageView) view.findViewById(R.id.iv_image);
		// vegtype.setTag(items.get(parentPosition).getParentChildren().get(childPosition).get("veg_type"));

		/*
		 * if
		 * ((items.get(parentPosition).getParentChildren().get(childPosition).
		 * get("veg_type").equalsIgnoreCase("0"))) {
		 * vegtype.setVisibility(View.VISIBLE);
		 * vegtype.setBackgroundResource(R.drawable.green); } else {
		 * vegtype.setImageResource(R.drawable.ic_non_veg);
		 * vegtype.setVisibility(View.VISIBLE); }
		 */
		if ((items.get(parentPosition).getParentChildren().get(childPosition)
				.get("veg_type")).equalsIgnoreCase("1")) {
			// vegtype.setVisibility(View.VISIBLE);
			vegtype.setImageResource(R.drawable.ic_non_veg);
		} else {
			vegtype.setImageResource(R.drawable.green);
			// vegtype.setVisibility(View.VISIBLE);
		}

		final TextView qty = ((TextView) view.findViewById(R.id.itemQty));

		qty.setTag(items.get(parentPosition).getParentChildren()
				.get(childPosition).get("id"));
		TAG_ITEM_QNTY = activity.itemWithQuantity.get(items.get(parentPosition)
				.getParentChildren().get(childPosition).get("id"));
		if (activity.itemWithQuantity.containsKey(items.get(parentPosition)
				.getParentChildren().get(childPosition).get("id"))) {
			qty.setText(activity.itemWithQuantity.get(items.get(parentPosition)
					.getParentChildren().get(childPosition).get("id")));
		} else {
			qty.setText("0");

		}

		TextView btnMinus = ((TextView) view.findViewById(R.id.btnMinus));
		TextView btnPlus = ((TextView) view.findViewById(R.id.btnPlus));
		
		btnMinus.setTag(items.get(parentPosition).getParentChildren()
				.get(childPosition).get("id"));
		btnPlus.setTag(items.get(parentPosition).getParentChildren()
				.get(childPosition).get("id"));
		btnPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String prevStrQty = activity.itemWithQuantity.get(arg0.getTag()
						.toString());
				if (prevStrQty == null)
					prevStrQty = "0";
				int prevQty = Integer.parseInt(prevStrQty);

				int newQty = prevQty + 1;
				qty.setText(String.valueOf(newQty));
				if (newQty != 0) {
					activity.itemWithQuantity.put(arg0.getTag().toString(),
							newQty + "");
					activity.updateQtyTextView();

				}
			}
		});

		btnMinus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) { 
				// TODO Auto-generated method stub
				
				String prevStrQty = activity.itemWithQuantity.get(arg0.getTag()
						.toString());
				int prevQty = Integer.parseInt((prevStrQty == null) ? "0"
						: prevStrQty);

				if (prevQty > 0) {
					int newQty = prevQty - 1;
					activity.itemWithQuantity.put(arg0.getTag().toString(),
							newQty + "");
					activity.updateQtyTextView();
					if (newQty == 0) {
						activity.itemWithQuantity.remove(arg0.getTag()
								.toString());
					}
					qty.setText(String.valueOf(newQty));

				}
			}
		});

		// return the entire view
		return view;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);

	}

	@Override
	public void onGroupExpanded(int groupPosition) {

		super.onGroupExpanded(groupPosition);
		if (items.get(groupPosition).getParentChildren().size() <= 0)
			activity.getGroupChildItems(groupPosition);
		/*if(flag==1){
			flag=2;
		activity.itemWithQuantity.put(""+19870,
				3 + ""); 
		activity.updateQtyTextView();
		}*/

		// if(groupPosition != lastExpandedGroupPosition){
		// expandableList.collapseGroup(lastExpandedGroupPosition);
		// }
		//
		// super.onGroupExpanded(groupPosition);
		// lastExpandedGroupPosition = groupPosition;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

}
