package com.example.android.greenspadestest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.android.greenspadestest.R;
import com.example.android.greenspadestest.adapters.GridAdapter;
import com.example.android.greenspadestest.adapters.ListAdapter;
import com.roger.catloadinglibrary.CatLoadingView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

	RecyclerView recyclerView;
	RecyclerView listRecyclerView;
	CatLoadingView mView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mView = new CatLoadingView();
		mView.show(getSupportFragmentManager(), "");

		recyclerView = findViewById(R.id.rv);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		recyclerView.setHasFixedSize(true);

		listRecyclerView = findViewById(R.id.history_rv);
		listRecyclerView.setNestedScrollingEnabled(false);
		listRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		makeGetRequest();
	}

	private void makeGetRequest() {

		int[] colors = getResources().getIntArray(R.array.gridColors);

		OkHttpClient client = new OkHttpClient();
		String url = "http://35.154.241.44:5555/api/v1/greenspades/data";
		Request request = new Request.Builder().url(url).build();

		client.newCall(request).enqueue(new Callback() {
			@Override public void onFailure(Request request, IOException e) {
				e.printStackTrace();
			}

			@Override public void onResponse(Response response) throws IOException {
				if (response.isSuccessful()) {
					final JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(response.body().string());
						runOnUiThread(() -> {
							try {
								GridAdapter adapter =
									new GridAdapter(colors, jsonObject.getJSONArray("kpis"));
								ScaleInAnimationAdapter alphaAdapter =
									new ScaleInAnimationAdapter(adapter);
								alphaAdapter.setDuration(1000);
								recyclerView.setAdapter(alphaAdapter);

								listRecyclerView.setAdapter(
									new ListAdapter(jsonObject.getJSONArray("transactions")));
								mView.dismiss();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
