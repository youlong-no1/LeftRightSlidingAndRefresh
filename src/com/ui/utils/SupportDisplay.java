/*
 * Copyright (C) 2015 Belle8
 * 
 * Description of the purpose of this file.
 *
 * @author ZhangZhaohui
 * @version 1.0.0
 */
package com.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SupportDisplay {

	/**
	 * 基准屏横
	 */
	private static final float BASIC_SCREEN_WIDTH = 1080f;
	/**
	 * 基准屏竖
	 */
	private static final float BASIC_SCREEN_HEIGHT = 1920f;

	/**
	 * 基准屏dpi
	 */
	private static final float BASIC_DENSITY = 2.0f;

	/**
	 * 误差率
	 */
	private static final float COMPLEMENT_VALUE = 0.5f;

	/**
	 * 手机屏宽
	 */
	private static int mDisplayWidth;
	/**
	 * 手机屏高
	 */
	private static int mDisplayHeight;
	/**
	 * 水平比例
	 */
	private static float mLayoutScale;
	/**
	 * 垂直比例
	 */
	private static float mVerticalScale;

	private static float mDensityScale;

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	public static void initLayoutSetParams(Context context) {
		if (context == null) {
			return;
		}
		final WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		final Display disp = wm.getDefaultDisplay();
		final Point point = new Point();
		getSize(disp, point);
		mDisplayWidth = point.x;
		mDisplayHeight = point.y;
		mLayoutScale = mDisplayWidth / BASIC_SCREEN_WIDTH;
		mVerticalScale = mDisplayWidth / BASIC_SCREEN_HEIGHT;
		float density = context.getResources().getDisplayMetrics().density;
		mDensityScale = BASIC_DENSITY / density;
	}

	public static float getLayoutScale() {
		return mLayoutScale;
	}

	/**
	 * 
	 * @param display
	 *            System display.
	 * @param outSize
	 *            Target size.
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void getSize(Display display, Point outSize) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			display.getSize(outSize);
		} else {
			outSize.x = display.getWidth();
			outSize.y = display.getHeight();
		}
	}

	/**
	 * 
	 * @param sizedp
	 * @return
	 */
	public static int calculateActualControlerSize(float sizedp) {
		return (int) (sizedp * mLayoutScale + COMPLEMENT_VALUE);
	}

	/**
	 * 
	 * @param sizedp
	 *            Calculation of real width by basic width.
	 * @return The size after calculation.
	 */
	public static int calculateActualControlerSizeY(float sizedp) {
		return (int) (sizedp * mVerticalScale + COMPLEMENT_VALUE);
	}

	/**
	 * 对文字大小计算
	 * 
	 * @param textView
	 *            TextView
	 * @param textSizedp
	 */
	public static void resetContrlerTextSize(TextView textView, float textSizedp) {
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizedp
				* mLayoutScale * mDensityScale);

	}

	/**
	 * 对文字大小计算
	 * 
	 * @param textView
	 *            TextView
	 * @param textSizedp
	 */
	public static void resetContrlerTextSizeY(TextView textView,
			float textSizedp) {
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizedp
				* mVerticalScale * mDensityScale);

	}

	/**
	 * 对Button计算
	 * 
	 * @param button
	 *            Button
	 * @param textSizedp
	 */
	public static void resetContrlerTextSize(Button button, float textSizedp) {
		resetContrlerTextSize((TextView) button, textSizedp);

	}

	/**
	 * 得到屏宽
	 * 
	 * @return mDisplayWidth
	 */
	public static int getmDisplayWidth() {
		return mDisplayWidth;
	}

	/**
	 * 得到屏高
	 * 
	 * @return mDisplayHeight
	 */
	public static int getmDisplayHeight() {
		return mDisplayHeight;
	}

	/**
	 * 重新计算所有Layout
	 * 
	 * @param rootView
	 */
	public static void resetAllChildViewParam(ViewGroup rootView) {
		int childCount = rootView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = rootView.getChildAt(i);
			if (childView instanceof ViewGroup) {
				SupportDisplay.setChildViewParam((ViewGroup) childView);
			} else {
				SupportDisplay.setViewParam(childView);
			}
		}
	}

	// public static void resetAllChildViewParam(ViewGroup rootView) {
	// // resetAllChildViewParam(rootView, false);
	// setChildViewParam(rootView);
	// }

	/**
	 * 重新计算所有Layout
	 * 
	 * @param parentView
	 */
	public static void setChildViewParam(ViewGroup parentView) {
		if (parentView == null) {
			return;
		}
		setViewParam(parentView);
		int childCount = parentView.getChildCount();
		if (childCount == 0) {
			return;
		}
		for (int i = 0; i < childCount; i++) {
			View childView = parentView.getChildAt(i);
			if (childView instanceof ViewGroup) {
				setChildViewParam((ViewGroup) childView);
			} else {
				setViewParam(childView);
			}
		}
	}

	/**
	 * 重新计算所有Layout
	 * 
	 * @param view
	 */
	public static void setViewParam(View view) {
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		int height = lp.height;
		if (height != -1 && height != -2) {
			lp.height = SupportDisplay.calculateActualControlerSize(height);
		}
		int width = lp.width;
		if (width != -1 && width != -2) {
			lp.width = SupportDisplay.calculateActualControlerSize(width);
		}

		if (!(lp instanceof RelativeLayout.LayoutParams
				|| lp instanceof LinearLayout.LayoutParams || lp instanceof FrameLayout.LayoutParams)) {
			return;
		}
		if (lp instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view
					.getLayoutParams();
			int leftMargin = param.leftMargin;
			if (leftMargin != -1) {
				param.leftMargin = SupportDisplay
						.calculateActualControlerSize(leftMargin);
			}
			int rightMargin = param.rightMargin;
			if (rightMargin != -1) {
				param.rightMargin = SupportDisplay
						.calculateActualControlerSize(rightMargin);
			}
			int topMargin = param.topMargin;
			if (topMargin != -1) {
				param.topMargin = SupportDisplay
						.calculateActualControlerSize(topMargin);
			}
			int bottomMargin = param.bottomMargin;
			if (bottomMargin != -1) {
				param.bottomMargin = SupportDisplay
						.calculateActualControlerSize(bottomMargin);
			}
		} else if (lp instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) view
					.getLayoutParams();

			int leftMargin = param.leftMargin;
			if (leftMargin != -1) {
				param.leftMargin = SupportDisplay
						.calculateActualControlerSize(leftMargin);
			}
			int rightMargin = param.rightMargin;
			if (rightMargin != -1) {
				param.rightMargin = SupportDisplay
						.calculateActualControlerSize(rightMargin);
			}
			int topMargin = param.topMargin;
			if (topMargin != -1) {
				param.topMargin = SupportDisplay
						.calculateActualControlerSize(topMargin);
			}
			int bottomMargin = param.bottomMargin;
			if (bottomMargin != -1) {
				param.bottomMargin = SupportDisplay
						.calculateActualControlerSize(bottomMargin);
			}

		} else if (lp instanceof FrameLayout.LayoutParams) {

			FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) view
					.getLayoutParams();

			int leftMargin = param.leftMargin;
			if (leftMargin != -1) {
				param.leftMargin = SupportDisplay
						.calculateActualControlerSize(leftMargin);
			}
			int rightMargin = param.rightMargin;
			if (rightMargin != -1) {
				param.rightMargin = SupportDisplay
						.calculateActualControlerSize(rightMargin);
			}
			int topMargin = param.topMargin;
			if (topMargin != -1) {
				param.topMargin = SupportDisplay
						.calculateActualControlerSize(topMargin);
			}
			int bottomMargin = param.bottomMargin;
			if (bottomMargin != -1) {
				param.bottomMargin = SupportDisplay
						.calculateActualControlerSize(bottomMargin);
			}

		}
		int leftPadding = view.getPaddingLeft();
		if (leftPadding != -1) {
			leftPadding = SupportDisplay
					.calculateActualControlerSize(leftPadding);
		}
		int rightPadding = view.getPaddingRight();
		if (rightPadding != -1) {
			rightPadding = SupportDisplay
					.calculateActualControlerSize(rightPadding);

		}
		int topPadding = view.getPaddingTop();
		if (topPadding != -1) {
			topPadding = SupportDisplay
					.calculateActualControlerSize(topPadding);
		}
		int bottomPadding = view.getPaddingBottom();
		if (bottomPadding != -1) {
			bottomPadding = SupportDisplay
					.calculateActualControlerSize(bottomPadding);
		}
		view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
		if (view instanceof EditText || view instanceof TextView) {
			TextView tv = (TextView) view;
			float textSize = tv.getTextSize();
			SupportDisplay.resetContrlerTextSize(tv, textSize);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if (view instanceof GridView) {
				GridView gridView = (GridView) view;
				int horizontalSpacing = gridView.getHorizontalSpacing();
				int verticalSpacing = gridView.getVerticalSpacing();
				gridView.setHorizontalSpacing(calculateActualControlerSize(horizontalSpacing));
				gridView.setVerticalSpacing(calculateActualControlerSize(verticalSpacing));
			}
		}

	}
}
