package com.etisbew.eatz.android.orderfood;

import java.io.Serializable;
import java.util.ArrayList;

public class DatWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809294747987488740L;

	private ArrayList<OrderDetails_11> orders1;

	public DatWrapper(ArrayList<OrderDetails_11> data1) {
		this.orders1 = data1;
	}

	public ArrayList<OrderDetails_11> getOrders1() {
		return this.orders1;
	}

}
