package com.etisbew.eatz.android.orderfood;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 6507849080931639582L;
	/**
		 * 
		 */

	private ArrayList<OrderDetails_1> orders;

	public DataWrapper(ArrayList<OrderDetails_1> data) {
		this.orders = data;
	}

	public ArrayList<OrderDetails_1> getOrders() {
		return this.orders;
	}

}
