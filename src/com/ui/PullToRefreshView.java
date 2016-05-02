/*
 * Copyright (C) 2015 Belle8
 * 
 * Description of the purpose of this file.
 *
 * @author ZhangZhaohui
 * @version 1.0.0
 */
package com.ui;

import com.example.androiddemo.R;
import com.ui.utils.SupportDisplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @description 鑷畾涔夌晫闈㈠埛鏂板鍣�
 * 
 */
public class PullToRefreshView extends LinearLayout {
	// 鍒锋柊鐘舵��
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// 鎷夊姩鐘舵��
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	private boolean enablePullTorefresh = true;
	private boolean enablePullLoadMoreDataStatus = true;
	private int mLastMotionY;
	private boolean mLock;

	protected float mLayoutScale;
	/**
	 * 澶撮儴view
	 */
	private View mHeaderView;
	/**
	 * 搴曢儴view
	 */
	private View mFooterView;
	private AdapterView<?> mAdapterView;
	private ScrollView mScrollView;
	/**
	 * 澶撮儴view鐨勯珮
	 */
	private int mHeaderViewHeight;
	/**
	 * 搴曢儴view鐨勯珮
	 */
	private int mFooterViewHeight;
	/**
	 * 澶撮儴绠ご
	 */
	private ImageView mHeaderImageView;
	/**
	 * 搴曢儴绠ご
	 */
	private ImageView mFooterImageView;
	/**
	 * 澶撮儴鏂囧瓧
	 */
	private TextView mHeaderTextView;
	/**
	 * 搴曢儴鏂囧瓧
	 */
	private TextView mFooterTextView;
	/**
	 * 澶撮儴鍒锋柊鏃堕棿
	 */
	// private TextView mHeaderUpdateTextView;
	/**
	 * 搴曢儴鍒锋柊鏃堕棿
	 */
	// private TextView mFooterUpdateTextView;
	/**
	 * 澶撮儴杩涘害鏉�
	 */
	private ProgressBar mHeaderProgressBar;
	/**
	 * 搴曢儴杩涘害鏉�
	 */
	private ProgressBar mFooterProgressBar;
	/**
	 * layout inflater
	 */
	private LayoutInflater mInflater;
	/**
	 * 澶撮儴view鐨勫綋鍓嶇姸鎬�
	 */
	private int mHeaderState;
	/**
	 * 搴曢儴view鐨勫綋鍓嶇姸鎬�
	 */
	private int mFooterState;
	/**
	 * 鎷夊姩鐘舵��
	 */
	private int mPullState;
	/**
	 * 鍙樹负鍚戜笅鐨勭澶�,鏀瑰彉绠ご鏂瑰悜
	 */
	private RotateAnimation mFlipAnimation;
	/**
	 * 鍙樹负閫嗗悜鐨勭澶�,鏃嬭浆
	 */
	private RotateAnimation mReverseFlipAnimation;
	/**
	 * 搴曢儴view鐨勫埛鏂扮洃鍚�
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	/**
	 * 澶撮儴view鐨勫埛鏂扮洃鍚�
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	/**
	 * 鏈�鍚庢洿鏂版椂闂�
	 */
	// private String mLastUpdateTime;

	public boolean isLoadMore = true;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mLayoutScale = SupportDisplay.getLayoutScale();
		// Load all of the animations we need in code rather than through XML
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = LayoutInflater.from(getContext());
		// header view 鍦ㄦ娣诲姞,淇濊瘉鏄涓�涓坊鍔犲埌linearlayout鐨勬渶涓婄
		addHeaderView();
	}

	private void addHeaderView() {

		mHeaderView = mInflater.inflate(R.layout.item_refresh_header, this,
				false);
		RelativeLayout background = (RelativeLayout) mHeaderView
				.findViewById(R.id.pull_to_refresh_header);
		SupportDisplay.resetAllChildViewParam(background);
		mHeaderImageView = (ImageView) mHeaderView
				.findViewById(R.id.pull_to_refresh_image);
		mHeaderTextView = (TextView) mHeaderView
				.findViewById(R.id.pull_to_refresh_text);
		// mHeaderUpdateTextView = (TextView) mHeaderView
		// .findViewById(R.id.pull_to_refresh_updated_at);
		mHeaderProgressBar = (ProgressBar) mHeaderView
				.findViewById(R.id.pull_to_refresh_progress);

		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderViewHeight);
		params.topMargin = -mHeaderViewHeight;
		addView(mHeaderView, params);

	}

	private void addFooterView() {

		mFooterView = mInflater.inflate(R.layout.item_refresh_footer, this,
				false);
		RelativeLayout background = (RelativeLayout) mFooterView
				.findViewById(R.id.pull_to_refresh_footer);
		SupportDisplay.resetAllChildViewParam(background);
		mFooterImageView = (ImageView) mFooterView
				.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pull_to_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.pull_to_load_progress);

		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mFooterViewHeight);
		params.bottomMargin = -mFooterViewHeight;
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		addFooterView();
		initContentAdapterView();
	}

	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
		}

	}

	public void refreshViewBottom() {
		mScrollView.post(new Runnable() {
			public void run() {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	//按下时x轴坐标
	int mLastMotionX;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int x=(int) e.getRawX();
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = y;
			//获取X轴坐标位置
			mLastMotionX=x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			//滑动时获取X轴滑动距离绝对值
			int deltaX=Math.abs(x-mLastMotionX);
			//当deltaX>10时，return false
			if(deltaX>10){
				return false;
			}
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE) {
				headerPrepareToRefresh(deltaY);
			} else if (mPullState == PULL_UP_STATE) {
				footerPrepareToRefresh(deltaY);
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE) {
				if (topMargin >= 0) {
					headerRefreshing();
				} else {
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (mPullState == PULL_UP_STATE) {
				if (Math.abs(topMargin) >= mHeaderViewHeight
						+ mFooterViewHeight) {
					footerRefreshing();
				} else {
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		if (mAdapterView != null) {
			if (deltaY > 10) {
				if (!enablePullTorefresh) {
					return false;
				}
				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// 濡傛灉mAdapterView涓病鏈夋暟鎹�,涓嶆嫤鎴�
					return false;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 11) {// 杩欓噷涔嬪墠鐢�3鍙互鍒ゆ柇,浣嗙幇鍦ㄤ笉琛�,杩樻病鎵惧埌鍘熷洜
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < -10) {
				// 鍒ゆ柇鏄惁绂佺敤涓婃媺鍔犺浇鏇村鎿嶄綔
				if (!enablePullLoadMoreDataStatus) {
					return false;
				}
				View lastChild = mAdapterView.getChildAt(mAdapterView
						.getChildCount() - 1);
				if (lastChild == null) {
					// 濡傛灉mAdapterView涓病鏈夋暟鎹�,涓嶆嫤鎴�
					return false;
				}
				// 鏈�鍚庝竴涓瓙view鐨凚ottom灏忎簬鐖禫iew鐨勯珮搴﹁鏄巑AdapterView鐨勬暟鎹病鏈夊～婊＄埗view,
				// 绛変簬鐖禫iew鐨勯珮搴﹁鏄巑AdapterView宸茬粡婊戝姩鍒版渶鍚�
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView
								.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 瀵逛簬ScrollView
		if (mScrollView != null) {
			// 瀛恠croll view婊戝姩鍒版渶椤剁
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& child.getMeasuredHeight() <= getHeight()
							+ mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header 鍑嗗鍒锋柊,鎵嬫寚绉诲姩杩囩▼,杩樻病鏈夐噴鏀�
	 * 
	 * @param deltaY
	 *            ,鎵嬫寚婊戝姩鐨勮窛绂�
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 褰揾eader view鐨則opMargin>=0鏃讹紝璇存槑宸茬粡瀹屽叏鏄剧ず鍑烘潵浜�,淇敼header view 鐨勬彁绀虹姸鎬�
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderTextView
					.setText(R.string.common_pull_to_refresh_release_label);
			// mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 鎷栧姩鏃舵病鏈夐噴鏀�
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			// mHeaderImageView.
			mHeaderTextView.setText(R.string.common_pull_to_refresh_pull_label);
			mHeaderState = PULL_TO_REFRESH;
		}
	}

	/**
	 * footer 鍑嗗鍒锋柊,鎵嬫寚绉诲姩杩囩▼,杩樻病鏈夐噴鏀� 绉诲姩footer view楂樺害鍚屾牱鍜岀Щ鍔╤eader view
	 * 楂樺害鏄竴鏍凤紝閮芥槸閫氳繃淇敼header view鐨則opmargin鐨勫�兼潵杈惧埌
	 * 
	 * @param deltaY
	 *            ,鎵嬫寚婊戝姩鐨勮窛绂�
	 */
	private void footerPrepareToRefresh(int deltaY) {
		if (isLoadMore) {
			int newTopMargin = changingHeaderViewTopMargin(deltaY);
			// 濡傛灉header view topMargin 鐨勭粷瀵瑰�煎ぇ浜庢垨绛変簬header + footer 鐨勯珮搴�
			// 璇存槑footer view 瀹屽叏鏄剧ず鍑烘潵浜嗭紝淇敼footer view 鐨勬彁绀虹姸鎬�
			if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
					&& mFooterState != RELEASE_TO_REFRESH) {
				mFooterTextView
						.setText(R.string.pull_to_refresh_footer_release_label);
				mFooterImageView.clearAnimation();
				mFooterImageView.startAnimation(mFlipAnimation);
				mFooterState = RELEASE_TO_REFRESH;
			} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
				mFooterImageView.clearAnimation();
				mFooterImageView.startAnimation(mFlipAnimation);
				mFooterTextView
						.setText(R.string.pull_to_refresh_footer_pull_label);
				mFooterState = PULL_TO_REFRESH;
			}
		}
	}

	public void setFooterViewHide(){
		if (null != mFooterView) {
			mFooterView.setVisibility(View.GONE);
		}
	}
	public void setFooterViewShow(){
		if (null != mFooterView) {
			mFooterView.setVisibility(View.VISIBLE);
		}
	}
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		// 杩欓噷瀵逛笂鎷夊仛涓�涓嬮檺鍒�,鍥犱负褰撳墠涓婃媺鍚庣劧鍚庝笉閲婃斁鎵嬫寚鐩存帴涓嬫媺,浼氭妸涓嬫媺鍒锋柊缁欒Е鍙戜簡,鎰熻阿缃戝弸yufengzungzhe鐨勬寚鍑�
		// 琛ㄧず濡傛灉鏄湪涓婃媺鍚庝竴娈佃窛绂�,鐒跺悗鐩存帴涓嬫媺
		if (deltaY > 0 && mPullState == PULL_UP_STATE
				&& Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// 鍚屾牱鍦�,瀵逛笅鎷夊仛涓�涓嬮檺鍒�,閬垮厤鍑虹幇璺熶笂鎷夋搷浣滄椂涓�鏍风殑bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE
				&& Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	public void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		mHeaderProgressBar.setVisibility(View.VISIBLE);
		mHeaderTextView
				.setText(R.string.common_pull_to_refresh_refreshing_label);
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView
				.setText(R.string.pull_to_refresh_footer_refreshing_label);
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * 璁剧疆header view 鐨則opMargin鐨勫��
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view 瀹屾垚鏇存柊鍚庢仮澶嶅垵濮嬬姸鎬�
	 * 
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
		mHeaderTextView.setText(R.string.common_pull_to_refresh_pull_label);
		mHeaderProgressBar.setVisibility(View.GONE);
		mHeaderState = PULL_TO_REFRESH;
		// setLastUpdated("鏈�杩戞洿鏂�:" + new Date().toLocaleString());
	}

	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		// setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 瀹屾垚鏇存柊鍚庢仮澶嶅垵濮嬬姸鎬�
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
		mFooterProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * footer view 瀹屾垚鏇存柊鍚庢仮澶嶅垵濮嬬姸鎬�
	 */
	public void onFooterRefreshComplete(int size) {
		if (size > 0) {
			mFooterView.setVisibility(View.VISIBLE);
		} else {
			mFooterView.setVisibility(View.GONE);
		}
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
		mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
		mFooterProgressBar.setVisibility(View.GONE);
		// mHeaderUpdateTextView.setText("");
		mFooterState = PULL_TO_REFRESH;
	}

	// public void setLastUpdated(CharSequence lastUpdated) {
	// if (lastUpdated != null) {
	// mHeaderUpdateTextView.setVisibility(View.VISIBLE);
	// mHeaderUpdateTextView.setText(lastUpdated);
	// } else {
	// mHeaderUpdateTextView.setVisibility(View.GONE);
	// }
	// }

	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	/**
	 * @description
	 * @param headerRefreshListener
	 */
	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}

	public boolean isEnablePullTorefresh() {
		return enablePullTorefresh;
	}

	public void setEnablePullTorefresh(boolean enablePullTorefresh) {
		this.enablePullTorefresh = enablePullTorefresh;
	}

	public boolean isEnablePullLoadMoreDataStatus() {
		return enablePullLoadMoreDataStatus;
	}

	public void setEnablePullLoadMoreDataStatus(
			boolean enablePullLoadMoreDataStatus) {
		this.enablePullLoadMoreDataStatus = enablePullLoadMoreDataStatus;
	}
}
