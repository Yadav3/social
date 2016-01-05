package com.etisbew.eatz.android;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.objects.Item;
import com.squareup.picasso.Picasso;

public class RowAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;
	Bitmap bimg;
	ViewHolder holder;
	String strQty = "0";

	public RowAdapter(Activity act, int resource, List<Item> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);
			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.txtLocation = (TextView) view.findViewById(R.id.txtLocation1);
		if (holder.txtLocation != null
				&& !TextUtils.isEmpty(objBean.getLocation())) {
			// holder.txtLocation.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtLocation.setText(Html.fromHtml(objBean.getLocation()));

			// if (MainScreen.selectedItem == position) {
			// holder.txtLocation.setBackgroundColor(Color
			// .parseColor("#ffdc64"));
			// } else {
			// holder.txtLocation.setBackgroundColor(Color
			// .parseColor("#ffffff"));
			// }
		}

		// holder.txtLocation.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// holder.txtLocation.setBackgroundColor(Color
		// .parseColor("#ffdc64"));
		// }
		// });

		holder.txtRestaurantName = (TextView) view
				.findViewById(R.id.txtRestaurantName);

		if (holder.txtRestaurantName != null
				&& !TextUtils.isEmpty(objBean.getRestaurantName())) {
			// holder.txtRestaurantName.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtRestaurantName.setText(Html
					.fromHtml("<font color=\"#2583c4\">"
							+ objBean.getRestaurantName() + "</font>"
							+ "<font color=\"#434242\">" + " , "
							+ objBean.getLocation() + "</font>"));
		}

		holder.txtRestaurantAddress = (TextView) view
				.findViewById(R.id.txtRestaurantAddress);

		if (holder.txtRestaurantAddress != null
				&& !TextUtils.isEmpty(objBean.getRestaurantAddress())) {
			// holder.txtRestaurantAddress.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtRestaurantAddress.setText(Html.fromHtml(objBean
					.getRestaurantAddress()));
		}

		holder.txtCuisine = (TextView) view.findViewById(R.id.txtCuisine);

		if (holder.txtCuisine != null
				&& !TextUtils.isEmpty(objBean.getCuisine())) {
			// holder.txtCuisine.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtCuisine.setText(Html.fromHtml(objBean.getCuisine()));
		}

		holder.txtReviews = (TextView) view.findViewById(R.id.txtReviews);

		if (holder.txtReviews != null
				&& !TextUtils.isEmpty(objBean.getReviewCount())) {
			// holder.txtReviews.setTypeface(Localsecrets.Titillium_Bold);
			if (Integer.parseInt(objBean.getReviewCount()) == 1) {
				holder.txtReviews
						.setText("( " + Html.fromHtml(objBean.getReviewCount())
								+ " ) Review");
			} else {
				holder.txtReviews.setText("( "
						+ Html.fromHtml(objBean.getReviewCount())
						+ " ) Reviews");
			}

		}

		holder.txtReviewUser = (TextView) view.findViewById(R.id.txtReviewUser);

		if (holder.txtReviewUser != null
				&& !TextUtils.isEmpty(objBean.getReviewUser())) {
			holder.txtReviewUser
					.setText(Html.fromHtml(objBean.getReviewUser()));
		}

		holder.txtReviewDate = (TextView) view.findViewById(R.id.txtReviewDate);

		if (holder.txtReviewDate != null
				&& !TextUtils.isEmpty(objBean.getReviewDate())) {
			holder.txtReviewDate
					.setText(Html.fromHtml(objBean.getReviewDate()));
		}

		holder.txtReviewTitle = (TextView) view
				.findViewById(R.id.txtReviewTitle2);

		if (holder.txtReviewTitle != null
				&& !TextUtils.isEmpty(objBean.getReviewTitle())) {
			holder.txtReviewTitle.setText(Html.fromHtml(objBean
					.getReviewTitle()));
		}
		holder.txtOrderDateId1 = (TextView) view
				.findViewById(R.id.txtOrderDateId1);
		if (holder.txtOrderDateId1 != null) {
			holder.txtOrderDateId1.setText("LSO"
					+ Html.fromHtml(objBean.getRestaurantId()));
		}
		holder.txtOrderDatePlaced1 = (TextView) view
				.findViewById(R.id.txtOrderDatePlaced1);

		if (holder.txtOrderDatePlaced1 != null) {
			holder.txtOrderDatePlaced1.setText(Html.fromHtml(objBean
					.getBookedDate()));// getOrderedDate()));
		}

		holder.txtReviewDesc = (TextView) view.findViewById(R.id.txtReviewDesc);

		if (holder.txtReviewDesc != null
				&& !TextUtils.isEmpty(objBean.getReviewDesc())) {
			holder.txtReviewDesc
					.setText(Html.fromHtml(objBean.getReviewDesc()));
		}

		holder.imgProfile = (ImageView) view.findViewById(R.id.imgReviewPerson);
		if (holder.imgProfile != null
				&& !TextUtils.isEmpty(objBean.getProfileImgUrl())) {

			Picasso.with(activity)
					.load(objBean.getProfileImgUrl().replace(" ", "%20"))
					// .resize(100, 150).into(imgUser);
					.resize(100, 100).into(holder.imgProfile);
		}

		holder.txtReservationDate1 = (TextView) view
				.findViewById(R.id.txtReservationDate1);

		if (holder.txtReservationDate1 != null) {
			holder.txtReservationDate1.setText(Html.fromHtml(objBean
					.getBookedDate()));
		}

		holder.txtReservationVenue1 = (TextView) view
				.findViewById(R.id.txtReservationVenue1);

		if (holder.txtReservationVenue1 != null
				&& !TextUtils.isEmpty(objBean.getVenue())) {
			holder.txtReservationVenue1.setText(Html.fromHtml(objBean
					.getVenue()));
		}

		holder.txtReservationSession1 = (TextView) view
				.findViewById(R.id.txtReservationSession1);

		if (holder.txtReservationSession1 != null
				&& !TextUtils.isEmpty(objBean.getSession())) {
			holder.txtReservationSession1.setText(Html.fromHtml(objBean
					.getSession()));
		}

		holder.txtReservationPoints1 = (TextView) view
				.findViewById(R.id.txtReservationPoints1);

		if (holder.txtReservationPoints1 != null
				&& !TextUtils.isEmpty(objBean.getPoints())) {
			holder.txtReservationPoints1.setText(Html.fromHtml(objBean
					.getPoints()));
		}

		holder.txtReservationStatus1 = (TextView) view
				.findViewById(R.id.txtReservationStatus1);

		if (holder.txtReservationStatus1 != null
				&& !TextUtils.isEmpty(objBean.getStatus())) {
			holder.txtReservationStatus1.setText(Html.fromHtml(objBean
					.getStatus()));
		}

		holder.txtDeliveryDate1 = (TextView) view
				.findViewById(R.id.txtDeliveryDate1);

		if (holder.txtDeliveryDate1 != null) {
			holder.txtDeliveryDate1.setText(Html.fromHtml(objBean
					.getOrderedDate()));// getBookedDate()));
		}

		holder.txtOrderVenue1 = (TextView) view
				.findViewById(R.id.txtOrderVenue1);

		if (holder.txtOrderVenue1 != null) {
			holder.txtOrderVenue1.setText(Html.fromHtml(objBean
					.getRestaurantName()));
		}

		holder.txtOrderType2 = (TextView) view.findViewById(R.id.txtOrderType2);

		if (holder.txtOrderType2 != null
				&& !TextUtils.isEmpty(objBean.getOrderType())) {
			holder.txtOrderType2.setText(Html.fromHtml(objBean.getOrderType()));
		}

		holder.txtOrderAmount1 = (TextView) view
				.findViewById(R.id.txtOrderAmount1);

		if (holder.txtOrderAmount1 != null
				&& !TextUtils.isEmpty(objBean.getVoucherValue())) {
			// holder.txtOrderAmount1.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtOrderAmount1.setText("Rs."
					+ Html.fromHtml(objBean.getVoucherValue()));
		}

		holder.txtOrderStatus1 = (TextView) view
				.findViewById(R.id.txtOrderStatus1);

		if (holder.txtOrderStatus1 != null
				&& !TextUtils.isEmpty(objBean.getStatus())) {
			// holder.txtOrderStatus1.setTypeface(Localsecrets.Titillium_Semibold);
			int pos = MyOrders.status_id.indexOf(objBean.getStatus());
			holder.txtOrderStatus1.setText(Html
					.fromHtml(MyOrders.status_names[pos]));
		}

		holder.txtOrderPoints1 = (TextView) view
				.findViewById(R.id.txtOrderPoints1);

		if (holder.txtOrderPoints1 != null
				&& !TextUtils.isEmpty(objBean.getPoints())) {
			holder.txtOrderPoints1.setText(Html.fromHtml(objBean.getPoints()));
		}

		holder.txtReviewRestaurant = (TextView) view
				.findViewById(R.id.txtReviewRestaurant);

		if (holder.txtReviewRestaurant != null
				&& !TextUtils.isEmpty(objBean.getRestaurantName())) {
			holder.txtReviewRestaurant.setText(Html
					.fromHtml("<font color=\"#d42a2b\">"
							+ objBean.getRestaurantName() + "</font>"
							+ "<font color=\"#434242\">" + " , "
							+ objBean.getLocation() + "</font>"));
		}

		holder.txtMadeon1 = (TextView) view.findViewById(R.id.txtMadeon1);

		if (holder.txtMadeon1 != null
				&& !TextUtils.isEmpty(objBean.getMadeon())) {
			holder.txtMadeon1.setText(Html.fromHtml(objBean.getMadeon()));
		}

		holder.txtReviewDate1 = (TextView) view
				.findViewById(R.id.txtReviewDate1);

		if (holder.txtReviewDate1 != null
				&& !TextUtils.isEmpty(objBean.getReviewDate())) {
			holder.txtReviewDate1
					.setText(Html.fromHtml(objBean.getReviewDate()));
		}

		holder.txtDistance = (TextView) view.findViewById(R.id.txtDistance);

		if (holder.txtDistance != null
				&& !TextUtils.isEmpty(objBean.getDistance())) {
			if (Float.parseFloat(objBean.getDistance()) == 0) {
				holder.txtDistance.setText("Near by");
			} else {
				holder.txtDistance.setText(Html.fromHtml(objBean.getDistance()
						+ " km"));
			}
		}

		holder.txtMyReviewComments = (TextView) view
				.findViewById(R.id.txtMyReviewComments);

		if (holder.txtMyReviewComments != null
				&& !TextUtils.isEmpty(objBean.getReviewDesc())) {
			holder.txtMyReviewComments.setText(Html.fromHtml(objBean
					.getReviewDesc()));
		}

		holder.txtRedemptionDate1 = (TextView) view
				.findViewById(R.id.txtRedemptionDate1);

		if (holder.txtRedemptionDate1 != null
				&& !TextUtils.isEmpty(objBean.getRedeemDate())) {
			holder.txtRedemptionDate1.setText(Html.fromHtml(objBean
					.getRedeemDate()));
		}

		holder.txtRedemptionRedeem1 = (TextView) view
				.findViewById(R.id.txtRedemptionRedeem1);

		if (holder.txtRedemptionRedeem1 != null
				&& !TextUtils.isEmpty(objBean.getRedeemedPoints())) {
			holder.txtRedemptionRedeem1.setText(Html.fromHtml(objBean
					.getRedeemedPoints()));
		}

		holder.txtRedemptionVoucher1 = (TextView) view
				.findViewById(R.id.txtRedemptionVoucher1);

		if (holder.txtRedemptionVoucher1 != null
				&& !TextUtils.isEmpty(objBean.getVoucherValue())) {
			holder.txtRedemptionVoucher1.setText(Html.fromHtml(objBean
					.getVoucherValue()));
		}

		holder.txtRedemptionVoucherNo1 = (TextView) view
				.findViewById(R.id.txtRedemptionVoucherNo1);

		if (holder.txtRedemptionVoucherNo1 != null
				&& !TextUtils.isEmpty(objBean.getVouchernumber())) {
			holder.txtRedemptionVoucherNo1.setText(Html.fromHtml(objBean
					.getVouchernumber()));
		}

		holder.txtRedemptionStatus1 = (TextView) view
				.findViewById(R.id.txtRedemptionStatus1);

		if (holder.txtRedemptionStatus1 != null
				&& !TextUtils.isEmpty(objBean.getStatus())) {
			holder.txtRedemptionStatus1.setText(Html.fromHtml(objBean
					.getStatus()));
		}

		holder.txtMenu = (TextView) view.findViewById(R.id.menuText);

		if (holder.txtMenu != null && !TextUtils.isEmpty(objBean.getMenuItem())) {
			holder.txtMenu.setText(objBean.getMenuItem());
		}

		holder.txtMenuName = (TextView) view.findViewById(R.id.txtItemName);
		holder.imgVeg = (ImageView) view.findViewById(R.id.imgVeg);
		holder.imgSpice = (ImageView) view.findViewById(R.id.imgSpice);

		if (holder.txtMenuName != null
				&& !TextUtils.isEmpty(objBean.getMenuItemName())) {

			if (!TextUtils.isEmpty(objBean.getMenuItemName())) {
				holder.txtMenuName.setText(objBean.getMenuItemName());

				if (Integer.parseInt(objBean.getSpicy()) == 2) {
					holder.imgSpice.setVisibility(View.VISIBLE);
					holder.imgSpice.setImageResource(R.drawable.spicy1);
				} else if (Integer.parseInt(objBean.getSpicy()) == 1) {
					holder.imgSpice.setVisibility(View.VISIBLE);
					holder.imgSpice.setImageResource(R.drawable.spice);
				} else {
					holder.imgSpice.setVisibility(View.GONE);
				}

				if (Integer.parseInt(objBean.getVegType()) == 1) {
					holder.imgVeg.setImageResource(R.drawable.nonveg);
				} else {
					holder.imgVeg.setImageResource(R.drawable.green);
				}

				// holder.txtMenuName.setCompoundDrawablesWithIntrinsicBounds(
				// R.drawable.spice, 0, 0, 0);

			}

		}

		holder.txtMenuPrice = (TextView) view.findViewById(R.id.txtPrice);

		if (holder.txtMenuPrice != null
				&& !TextUtils.isEmpty(objBean.getMenuItemPrice())) {
			// holder.txtMenuPrice.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtMenuPrice.setText(objBean.getMenuItemPrice());
		}

		holder.ratingBarRestaurant = (RatingBar) view
				.findViewById(R.id.ratingbarRestaurant1);
		if (holder.ratingBarRestaurant != null
				&& !TextUtils.isEmpty(objBean.getReviews())) {
			// Float f1 = Float.valueOf(objBean.getReviews()).floatValue();
			// Float f = new Float(objBean.getReviews());
			// holder.ratingBarRestaurant.setRating(f);

			// holder.ratingBarRestaurant.setRating(3.0f);

			if (objBean.getReviews().equalsIgnoreCase("null")
					|| objBean.getReviews().equalsIgnoreCase("0")) {
				holder.ratingBarRestaurant.setRating(0);
				holder.ratingBarRestaurant.setVisibility(View.GONE);
				holder.txtReviews.setVisibility(View.GONE);
				if (holder.txtBetheFirstReview1 != null) {
					holder.txtBetheFirstReview1.setVisibility(View.VISIBLE);
				}
			} else {

				holder.ratingBarRestaurant.setRating(Float.parseFloat(objBean
						.getReviews()));
				holder.txtReviews.setVisibility(View.VISIBLE);
				holder.ratingBarRestaurant.setVisibility(View.VISIBLE);
				if (holder.txtBetheFirstReview1 != null) {
					holder.txtBetheFirstReview1.setVisibility(View.GONE);
				}
			}

		}

		holder.txtBetheFirstReview1 = (TextView) view
				.findViewById(R.id.txtBetheFirstReview1);
		if (holder.txtBetheFirstReview1 != null) {
			holder.txtBetheFirstReview1
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								Launcher.strVenueId = Launcher.jsonArrayRestaurants
										.getJSONObject(position).getString(
												"Venueid");
							} catch (Exception e) {
								// TODO: handle exception
							}
							// new SearchDetails().getWriteReview(activity);
						}
					});
		}

		holder.ratingbarReview = (RatingBar) view
				.findViewById(R.id.ratingbarReview);
		if (holder.ratingbarReview != null
				&& !TextUtils.isEmpty(objBean.getRating())) {

			// Float f1 = Float.valueOf(objBean.getReviews()).floatValue();
			// Float f = new Float(objBean.getReviews());
			// holder.ratingBarRestaurant.setRating(f);
			if (objBean.getRating().equalsIgnoreCase("null")
					|| objBean.getRating().equalsIgnoreCase("0")) {
				holder.ratingbarReview.setRating(0);

			} else {

				holder.ratingbarReview.setRating(Float.parseFloat(objBean
						.getRating()));
			}
		}

		holder.ratingbarMyReview = (RatingBar) view
				.findViewById(R.id.ratingbarMyReview);
		if (holder.ratingbarMyReview != null
				&& !TextUtils.isEmpty(objBean.getRating())) {

			// Float f1 = Float.valueOf(objBean.getReviews()).floatValue();
			// Float f = new Float(objBean.getReviews());
			// holder.ratingBarRestaurant.setRating(f);
			if (objBean.getRating().equalsIgnoreCase("null")) {
				holder.ratingbarMyReview.setRating(0);
			} else {
				holder.ratingbarMyReview.setRating(Float.parseFloat(objBean
						.getRating()));
			}
		}

		holder.txtVenue = (TextView) view.findViewById(R.id.txtVenue);

		if (holder.txtVenue != null
				&& !TextUtils.isEmpty(objBean.getRestaurantName())) {
			// holder.txtVenue.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtVenue.setText(objBean.getRestaurantName());
		}

		holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);

		if (holder.txtTitle != null
				&& !TextUtils.isEmpty(objBean.getDealTitle())) {
			// holder.txtTitle.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtTitle.setText(objBean.getDealTitle());
		}

		holder.txtVoucher = (TextView) view.findViewById(R.id.txtVoucher);

		if (holder.txtVoucher != null) {
			// holder.txtVoucher.setTypeface(Localsecrets.Titillium_Semibold);
			if (!TextUtils.isEmpty(objBean.getProductPrice())) {
				holder.txtVoucher.setText(objBean.getProductPrice());
			}
		}

		holder.txtDeal = (TextView) view.findViewById(R.id.txtVoucher);

		if (holder.txtDeal != null) {
			// holder.txtDeal.setTypeface(Localsecrets.Titillium_Semibold);
			if (!TextUtils.isEmpty(objBean.getLSPrice())) {
				holder.txtDeal.setText("Pay Now Rs " + objBean.getLSPrice());
			}
		}

		holder.imgDeal = (ImageView) view.findViewById(R.id.imgDeal);
		if (holder.imgDeal != null
				&& !TextUtils.isEmpty(objBean.getDealImage())) {

			// URL url;
			// try {
			// url = new URL(objBean.getDealImage());
			// Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
			// .getInputStream());
			// holder.imgDeal.setImageBitmap(bmp);
			// } catch (MalformedURLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			BitmapDownloaderTask task = new BitmapDownloaderTask(holder.imgDeal);
			task.execute(objBean.getDealImage().replace(" ", "%20"));

		}

		holder.txtOrderItem1 = (TextView) view.findViewById(R.id.txtOrderItem1);
		if (holder.txtOrderItem1 != null
				&& !TextUtils.isEmpty(objBean.getProduct())) {
			// holder.txtOrderItem1.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtOrderItem1.setText(Html.fromHtml(objBean.getProduct()));
		}

		holder.txtOrderQty1 = (TextView) view.findViewById(R.id.txtOrderQty1);

		if (holder.txtOrderQty1 != null
				&& !TextUtils.isEmpty(objBean.getProductQty())) {
			// holder.txtOrderQty1.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtOrderQty1.setText(Html.fromHtml(objBean.getProductQty()));
		}

		holder.txtOrderPrice1 = (TextView) view
				.findViewById(R.id.txtOrderPrice1);
		if (holder.txtOrderPrice1 != null
				&& !TextUtils.isEmpty(objBean.getProductPrice())) {
			// holder.txtOrderPrice1.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtOrderPrice1.setText(Html.fromHtml(objBean
					.getProductPrice()));
		}

		holder.txtOrderTotal1 = (TextView) view
				.findViewById(R.id.txtOrderTotal1);
		if (holder.txtOrderTotal1 != null
				&& !TextUtils.isEmpty(objBean.getTotal())) {
			// holder.txtOrderTotal1.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtOrderTotal1.setText(Html.fromHtml(objBean.getTotal()));
		}

		holder.txtEventName = (TextView) view.findViewById(R.id.txtEvent);
		if (holder.txtEventName != null
				&& !TextUtils.isEmpty(objBean.getEventTitle())) {
			// holder.txtEventName.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtEventName.setText(Html.fromHtml(objBean.getEventTitle()));
		}

		holder.txtEvntDesc = (TextView) view.findViewById(R.id.txtEvntDesc);
		if (holder.txtEvntDesc != null
				&& !TextUtils.isEmpty(objBean.getEventDesc())) {
			// holder.txtEvntDesc.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtEvntDesc.setText(Html.fromHtml(objBean.getEventDesc()));
		}

		holder.txtEvntLoc = (TextView) view.findViewById(R.id.txtEvntLoc);
		if (holder.txtEvntLoc != null
				&& !TextUtils.isEmpty(objBean.getEventAddress())) {
			// holder.txtEvntLoc.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtEvntLoc.setText(Html.fromHtml(objBean.getEventAddress()));
		}

		holder.txtEvntDate = (TextView) view.findViewById(R.id.txtEvntDate);
		if (holder.txtEvntDate != null
				&& !TextUtils.isEmpty(objBean.getEventDate())) {
			// holder.txtEvntDate.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtEvntDate.setText(Html.fromHtml(objBean.getEventDate()));
		}

		holder.txtEvntTime = (TextView) view.findViewById(R.id.txtEvntTime);
		if (holder.txtEvntTime != null
				&& !TextUtils.isEmpty(objBean.getEventTime())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtEvntTime.setText(Html.fromHtml(objBean.getEventTime()));
		}

		holder.imgEvent = (ImageView) view.findViewById(R.id.imgEvent);
		if (holder.imgEvent != null
				&& !TextUtils.isEmpty(objBean.getEventImage())) {

			// BitmapDownloaderTask task = new BitmapDownloaderTask(
			// holder.imgEvent);
			// task.execute(objBean.getEventImage().replace(" ", "%20"));
			Picasso.with(activity)
					.load(objBean.getEventImage().replace(" ", "%20"))
					// .resize(100, 150).into(imgUser);
					.into(holder.imgEvent);
		}

		holder.txtItemName1 = (TextView) view.findViewById(R.id.txtItemName1);
		if (holder.txtItemName1 != null
				&& !TextUtils.isEmpty(objBean.getItemName())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtItemName1.setText(Html.fromHtml(objBean.getItemName()));
		}
		holder.txtVouchers1 = (TextView) view.findViewById(R.id.txtVouchers1);
		if (holder.txtVouchers1 != null
				&& !TextUtils.isEmpty(objBean.getVouchers())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtVouchers1.setText(Html.fromHtml(objBean.getVouchers()));
		}
		holder.txtVoucherType1 = (TextView) view
				.findViewById(R.id.txtVoucherType1);
		if (holder.txtVoucherType1 != null
				&& !TextUtils.isEmpty(objBean.getVoucherType())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtVoucherType1.setText(Html.fromHtml(objBean
					.getVoucherType()));
		}
		holder.txtQuantity1 = (TextView) view.findViewById(R.id.txtQuantity1);
		if (holder.txtQuantity1 != null
				&& !TextUtils.isEmpty(objBean.getQuantity())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtQuantity1.setText(Html.fromHtml(objBean.getQuantity()));
		}

		holder.txtUnitPrice1 = (TextView) view.findViewById(R.id.txtUnitPrice1);
		if (holder.txtUnitPrice1 != null
				&& !TextUtils.isEmpty(objBean.getUnitPrice())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtUnitPrice1.setText(Html.fromHtml(objBean.getUnitPrice()));
		}

		holder.txtPrice1 = (TextView) view.findViewById(R.id.txtPrice1);
		if (holder.txtPrice1 != null && !TextUtils.isEmpty(objBean.getPrice())) {
			// holder.txtEvntTime.setTypeface(Localsecrets.Titillium_Semibold);
			holder.txtPrice1.setText(Html.fromHtml(objBean.getPrice()));
		}
		
		holder.txtPointsDate1 = (TextView) view.findViewById(R.id.txtPointsDate1);
		if (holder.txtPointsDate1 != null && !TextUtils.isEmpty(objBean.getPointsDate())) {
			holder.txtPointsDate1.setText(Html.fromHtml(objBean.getPointsDate()));
		}
		
		holder.txtPointsVenue1 = (TextView) view.findViewById(R.id.txtPointsVenue1);
		if (holder.txtPointsVenue1 != null && !TextUtils.isEmpty(objBean.getPointsVenue())) {
			holder.txtPointsVenue1.setText(Html.fromHtml(objBean.getPointsVenue()));
		}
		
		holder.txtPointsType1 = (TextView) view.findViewById(R.id.txtPointsType1);
		if (holder.txtPointsType1 != null && !TextUtils.isEmpty(objBean.getPointsType())) {
			holder.txtPointsType1.setText(Html.fromHtml(objBean.getPointsType()));
		}
		
		holder.txtPointsEarned1 = (TextView) view.findViewById(R.id.txtPointsEarned1);
		if (holder.txtPointsEarned1 != null && !TextUtils.isEmpty(objBean.getPointsEarned())) {
			holder.txtPointsEarned1.setText(Html.fromHtml(objBean.getPointsEarned()));
		}
		

		return view;
	}

	static class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		// Actual download method, run in the task thread
		protected Bitmap doInBackground(String... params) {
			// params comes from the execute() call: params[0] is the url.
			return downloadBitmap(params[0]);
		}

		@Override
		// Once the image is downloaded, associates it to the imageView
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	public class ViewHolder {
		public TextView txtRestaurantName, txtRestaurantAddress, txtCuisine,
				txtReviews, txtLocation, txtReviewUser, txtReviewDate,
				txtReviewDesc, txtReservationDate, txtReservationDate1,
				txtReservationVenue, txtReservationVenue1,
				txtReservationSession, txtReservationSession1,
				txtReservationStatus, txtReservationStatus1,
				txtReservationPoints, txtReservationPoints1, txtOrderDate,
				txtDeliveryDate1, txtOrderVenue, txtOrderVenue1, txtOrderType1,
				txtOrderType2, txtOrderAmount, txtOrderAmount1, txtOrderStatus,
				txtOrderStatus1, txtOrderPoints, txtOrderPoints1,
				txtReviewRestaurant, txtVotedAs, txtReviewDate1,
				txtReviewTitle, txtMyReviewComments, txtRedemptionDate,
				txtRedemptionDate1, txtRedemptionRedeem, txtRedemptionRedeem1,
				txtRedemptionVoucher, txtRedemptionVoucher1,
				txtRedemptionVoucherNo, txtRedemptionVoucherNo1,
				txtRedemptionStatus, txtRedemptionStatus1, txtMadeon,
				txtMadeon1, txtMenu, txtMenuName, txtMenuPrice, txtVenue,
				txtTitle, txtVoucher, txtDeal, txtOrderItem1, txtOrderQty1,
				txtOrderPrice1, txtOrderTotal1, txtEventName, txtEvntDesc,
				txtEvntLoc, txtEvntDate, txtEvntTime, txtQty, btnMinus,
				btnPlus, txtBetheFirstReview1, txtDistance, txtItemName1,
				txtVouchers1, txtVoucherType1, txtUnitPrice1, txtQuantity1,
				txtPrice1, txtOrderDateId1, txtOrderDatePlaced1,
				txtPointsDate1, txtPointsVenue1, txtPointsType1,
				txtPointsEarned1;
		RatingBar ratingBarRestaurant, ratingbarReview, ratingbarMyReview;
		ImageView imgProfile, imgDeal, imgEvent, imgVeg, imgSpice;

	}
}