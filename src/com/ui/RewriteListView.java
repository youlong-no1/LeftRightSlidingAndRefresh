/*
 * Copyright (C) 2015 Belle8
 * 
 * Description of the purpose of this file.
 *
 * @author ZhangZhaohui
 * @version 1.0.0
 */
package com.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class RewriteListView extends ListView {

	public RewriteListView(Context context) {
		// TODO Auto-generated method stub
		super(context);
	}

	public RewriteListView(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		super(context, attrs);
	}

	public RewriteListView(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated method stub
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		try {
			int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}