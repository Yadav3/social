package com.etisbew.eatz.android.orderfood;

import java.io.Serializable;


public class OrderDetails_1  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8144746786276899372L;
	String title,count,itemId,image,category_id;
	

	public OrderDetails_1(String itemId, String title, String count, String image,String category_id) {
		this.itemId=itemId; 
		this.category_id=category_id;
		this.title = title;
		this.count = count;
		this.image=image;
		
		
	}
}