package com.example.androiddemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ui.FullyLinearLayoutManager;
import com.ui.GalleryAdapter;
import com.ui.utils.SupportDisplay;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity {
	private RecyclerView mRVAdList;
	private List<Integer> mDatas;
	private GalleryAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SupportDisplay.initLayoutSetParams(this);
		mRVAdList = (RecyclerView) findViewById(R.id.rv_forum_home_ad_list);
		mRVAdList.setFocusable(true);
		mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3,
				R.drawable.banner4, R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4));
		FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		mRVAdList.setLayoutManager(linearLayoutManager);
		mAdapter = new GalleryAdapter(this, mDatas);
		mRVAdList.setAdapter(mAdapter);
	}

}
