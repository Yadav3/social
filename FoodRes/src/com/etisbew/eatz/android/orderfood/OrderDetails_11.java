package com.etisbew.eatz.android.orderfood;

import java.io.Serializable;

public class OrderDetails_11 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String title1,count1,itemId1;
	
	public OrderDetails_11(String itemId1, String title1, String count1 ) {
		this.itemId1=itemId1; 
		this.title1 = title1;
		this.count1 = count1;
	}

}
