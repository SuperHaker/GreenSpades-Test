package com.example.android.greenspadestest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import com.example.android.greenspadestest.R;
import com.example.android.greenspadestest.flipanimation.FlipVerticalToAnimation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

	private JSONArray mJSONArray;

	public ListAdapter(JSONArray JSONArray) {
		mJSONArray = JSONArray;
	}

	@Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v =
			LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
		return new MyViewHolder(v);
	}

	@Override public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.detailView.setVisibility(View.GONE);
		try {
			holder.amount.setText(mJSONArray.getJSONObject(position).getString("amountPaid"));
			holder.name.setText(
				mJSONArray.getJSONObject(position).getJSONObject("customer").getString("name"));
			holder.phoneNumber.setText(mJSONArray.getJSONObject(position)
				.getJSONObject("customer")
				.getString("mobileNumber"));

			holder.date.setText(
				parseDate(mJSONArray.getJSONObject(position).getString("transactionDate")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String parseDate(String transactionDate) {
		SimpleDateFormat format =
			new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.getDefault());
		try {
			Date newDate = format.parse(transactionDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(newDate);
			int dateNum = cal.get(Calendar.DATE);

			format = new SimpleDateFormat("MMM dd'" + getDayNumberSuffix(dateNum) + "' yy",
				Locale.getDefault());
			return format.format(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override public int getItemCount() {
		return mJSONArray.length();
	}

	private String getDayNumberSuffix(int day) {
		if (day >= 11 && day <= 13) {
			return "th";
		}
		switch (day % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView name, date, phoneNumber, amount;
		View detailView;

		MyViewHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			date = itemView.findViewById(R.id.date);
			phoneNumber = itemView.findViewById(R.id.phone_number);
			amount = itemView.findViewById(R.id.amount);
			detailView = itemView.findViewById(R.id.detail_view);
			itemView.setOnClickListener(this);
		}

		@Override public void onClick(View view) {
			new FlipVerticalToAnimation(view).setFlipToView(view)
				.setInterpolator(new LinearInterpolator())
				.animate();
		}
	}
}
