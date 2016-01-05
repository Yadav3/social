package com.etisbew.eatz.android.orderfood;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableListItems {

	private String parent;
	private ArrayList<HashMap<String, String>> parentChildren;

	public ExpandableListItems() {
	}

	public ExpandableListItems(String parent,
			ArrayList<HashMap<String, String>> parentChildren) {
		this.parent = parent;
		this.parentChildren = parentChildren;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public ArrayList<HashMap<String, String>> getParentChildren() {
		return parentChildren;
	}

	public void setParentChildren(
			ArrayList<HashMap<String, String>> parentChildren) {
		this.parentChildren = parentChildren;
	}
}
