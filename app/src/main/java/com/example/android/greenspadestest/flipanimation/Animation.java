package com.example.android.greenspadestest.flipanimation;

import android.view.View;

public abstract class Animation {

	static final int DIRECTION_UP = 3;
	static final int DURATION_LONG = 500;

	View view;

	public abstract void animate();
}