package com.example.android.greenspadestest.adapters;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.greenspadestest.R;
import org.json.JSONArray;
import org.json.JSONException;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {

	private int[] colors;
	private JSONArray kpisArray;

	public GridAdapter(int[] colors, JSONArray kpisArray) {
		this.colors = colors;
		this.kpisArray = kpisArray;
	}

	@Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v =
			LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
		return new MyViewHolder(v);
	}

	@Override public void onBindViewHolder(MyViewHolder holder, int position) {
		GradientDrawable border = new GradientDrawable();
		border.setColor(0xFFFFFFFF); //white background
		border.setStroke(2, colors[position % 4]);
		holder.itemView.setBackground(border);//black border with full opacity
		holder.headingTextView.setTextColor(colors[position % 4]);
		holder.valueTextView.setTextColor(colors[position % 4]);
		holder.border.setBackgroundColor(colors[position % 4]);

		try {
			holder.headingTextView.setText(kpisArray.getJSONObject(position).getString("text1"));
			holder.valueTextView.setText(kpisArray.getJSONObject(position).getString("value"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override public int getItemCount() {
		return kpisArray.length();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView headingTextView, valueTextView;
		View border;

		MyViewHolder(View itemView) {
			super(itemView);
			headingTextView = itemView.findViewById(R.id.heading_text_view);
			valueTextView = itemView.findViewById(R.id.value_text_view);
			border = itemView.findViewById(R.id.bottom_border);
		}
	}
}
