package com.example.android.greenspadestest.flipanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.example.android.greenspadestest.R;

public class FlipVerticalToAnimation extends Animation {

	private static final int PIVOT_CENTER = 0, PIVOT_TOP = 1, PIVOT_BOTTOM = 2;

	private View flipToView;
	private int pivot, direction;
	private TimeInterpolator interpolator;
	private long duration;
	private AnimationListener listener;

	public FlipVerticalToAnimation(View view) {
		this.view = view;
		flipToView = null;
		pivot = PIVOT_CENTER;
		direction = DIRECTION_UP;
		interpolator = new AccelerateDecelerateInterpolator();
		duration = DURATION_LONG;
		listener = null;
	}

	@Override public void animate() {
		ViewGroup parentView = (ViewGroup) view.getParent(), rootView =
			(ViewGroup) view.getRootView();

		float pivotX, pivotY, flipAngle = 270f, viewWidth = view.getWidth(), viewHeight =
			view.getHeight();
		final float originalRotationX = view.getRotationX();
		switch (pivot) {
			case PIVOT_TOP:
				pivotX = viewWidth / 2;
				pivotY = 0f;
				break;
			case PIVOT_BOTTOM:
				pivotX = viewWidth / 2;
				pivotY = viewHeight;
				break;
			default:
				pivotX = viewWidth / 2;
				pivotY = viewHeight / 2;
				flipAngle = 90f;
				break;
		}
		view.setPivotX(pivotX);
		view.setPivotY(pivotY);
		flipToView.setLayoutParams(view.getLayoutParams());
		flipToView.setLeft(view.getLeft());
		flipToView.setTop(view.getTop());
		flipToView.setPivotX(pivotX);
		flipToView.setPivotY(pivotY);
		flipToView.setVisibility(View.VISIBLE);

		while (parentView != rootView) {
			parentView.setClipChildren(false);
			parentView = (ViewGroup) parentView.getParent();
		}
		rootView.setClipChildren(false);

		AnimatorSet flipToAnim = new AnimatorSet();
		if (direction == DIRECTION_UP) {
			flipToView.setRotationX(270f);
			flipToAnim.playSequentially(
				ObjectAnimator.ofFloat(view, View.ROTATION_X, 0f, flipAngle),
				ObjectAnimator.ofFloat(flipToView, View.ROTATION_X, 270f, 360f));
		} else if (direction == DIRECTION_UP) {
			flipToView.setRotationX(-270f);
			flipToAnim.playSequentially(
				ObjectAnimator.ofFloat(view, View.ROTATION_X, 0f, -flipAngle),
				ObjectAnimator.ofFloat(flipToView, View.ROTATION_X, -270f, -360f));
		}
		flipToAnim.setInterpolator(interpolator);
		flipToAnim.setDuration(duration / 2);
		flipToAnim.addListener(new AnimatorListenerAdapter() {

			@Override public void onAnimationEnd(Animator animation) {
				//view.setVisibility(View.INVISIBLE);
				view.setRotationX(originalRotationX);
				if (getListener() != null) {
					getListener().onAnimationEnd(FlipVerticalToAnimation.this);
				}
			}
		});
		flipToAnim.start();
	}

	public FlipVerticalToAnimation setFlipToView(View flipToView) {
		this.flipToView = flipToView;
		if (flipToView.findViewById(R.id.detail_view).getVisibility() == View.GONE) {
			flipToView.findViewById(R.id.detail_view).setVisibility(View.VISIBLE);
		} else {
			flipToView.findViewById(R.id.detail_view).setVisibility(View.GONE);
		}
		return this;
	}

	public FlipVerticalToAnimation setInterpolator(TimeInterpolator interpolator) {
		this.interpolator = interpolator;
		return this;
	}

	AnimationListener getListener() {
		return listener;
	}
}