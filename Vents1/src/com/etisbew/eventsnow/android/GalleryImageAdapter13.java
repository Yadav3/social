package com.etisbew.eventsnow.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ParseException;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class GalleryImageAdapter13 extends BaseAdapter {

	private Activity context;

	private static ImageView imageView;
	private static TextView txt;
	String todate = "";
	private ArrayList<String> events_dates;
	private ArrayList<String> default_dates;
	Spanned ddd;
	private static ViewHolder holder; 
	private int row;  

	public GalleryImageAdapter13(Activity context, int resource,
			ArrayList<String> events_dates,ArrayList<String> default_dates) {
		
		this.context = context;
		this.events_dates = events_dates; 
		this.default_dates = default_dates; 
		this.row = resource;
		System.out.println("events_dates"+events_dates+default_dates);
	}

	@Override
	public int getCount() {
		return default_dates.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("position"+position);

		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);
			holder = new ViewHolder();   
			view.setTag(holder);  
		} else { 
			holder = (ViewHolder) view.getTag(); 
		} 
		// holder.imageView.setImageDrawable(events_dates.get(position));

		// holder.textview.setText(""+events_dates.get(position));
		holder.textview = (TextView) view.findViewById(R.id.tv);

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Date date = new Date();
			todate = dateFormat.format(date); 
			
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String ss=default_dates.get(position);
			
			Date dt1 = format1.parse(ss); 
			DateFormat format2 = new SimpleDateFormat("EEE");
			String finalDay = format2.format(dt1).toUpperCase();

			String[] parts = ss.split("-");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
			Date date1 = sdf.parse(todate);
			Date date2 = sdf.parse(ss);

			System.out.println("date1"+sdf.format(date1));
			System.out.println("date2"+sdf.format(date2));
			//date2.compareTo(date1) > 0 || 
			System.out.println("dddd"+(date2.compareTo(date1))+date2+date1);
			//if (date2.compareTo(date1) > 0 ||date2.compareTo(date1)==0) {
			if(events_dates.contains(ss)){
				System.out.println("Date1 is after Date2");
				
				Spannable word = new SpannableString(finalDay + "\n");
				word.setSpan(new ForegroundColorSpan(Color.RED), 0,
						word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.textview.setText(word);
  
				Spannable wordTwo = new SpannableString(parts[2] + "\n");
				wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0,
						wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.textview.append(wordTwo);
				 

				Spannable wordTwo0 = new SpannableString(
						getMonthForInt(Integer.parseInt(parts[1])));
				wordTwo0.setSpan(new ForegroundColorSpan(Color.RED), 0,
						wordTwo0.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.textview.append(wordTwo0);
				/*holder.textview.setEnabled(false);

				holder.textview.setTextColor(context.getResources().getColor(R.color.hidden));*/

		//	} else if (date2.compareTo(date1) < 0) { 
			}else{
				System.out.println("Date1 is before Date2");
				
  
				Spannable word = new SpannableString(finalDay + "\n");
				word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hidden)), 0,
						word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.textview.setText(word);

				Spannable wordTwo = new SpannableString(parts[2] + "\n");
				wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hidden)), 0,
						wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.textview.append(wordTwo);

				Spannable wordTwo0 = new SpannableString(
						getMonthForInt(Integer.parseInt(parts[1])));
				wordTwo0.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hidden)), 0,
						wordTwo0.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.textview.append(wordTwo0);

			
			}
			holder.textview.setLayoutParams(new Gallery.LayoutParams(81, 95));
			//holder.textview.setTextSize(17); 

		} catch (ParseException ex) { 
			ex.printStackTrace(); 
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// holder.textview.setText(""+ddd);
		// holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		// holder.imageView.setLayoutParams(new Gallery.LayoutParams(150, 90));

		return holder.textview;
	}

	private static class ViewHolder {
		ImageView imageView;
		TextView textview;
	}

	String getMonthForInt(int num) {
		String monthString = "wrong";
		/*
		 * DateFormatSymbols dfs = new DateFormatSymbols(); String[] months =
		 * dfs.getMonths(); if (num >= 0 && num <= 11 ) { month = months[num]; }
		 */

		// int month = 8;
		// String monthString;
		switch (num) {
		case 1:
			monthString = "Jan";
			break;
		case 2:
			monthString = "Feb";
			break;
		case 3:
			monthString = "Mar";
			break;
		case 4:
			monthString = "Apr";
			break;
		case 5:
			monthString = "May";
			break;
		case 6:
			monthString = "Jun";
			break;
		case 7:
			monthString = "Jul";
			break;
		case 8:
			monthString = "Aug";
			break;
		case 9:
			monthString = "Sep";
			break;
		case 10:
			monthString = "Oct";
			break;
		case 11:
			monthString = "Nov";
			break;
		case 12:
			monthString = "Dec";
			break;
		default:
			monthString = "Invalid month";
			break;
		}
		// System.out.println(monthString);
		return monthString;
	}
}
