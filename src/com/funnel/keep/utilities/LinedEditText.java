package com.funnel.keep.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class LinedEditText extends EditText {
	private Rect mRect;
	private Paint mPaint;
	public int defaultNumberOfLine = 10;

	// we need this constructor for LayoutInflater
	public LinedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);

		mPaint.setColor(0x800000FF);
		setMinLines(defaultNumberOfLine);
	}

	@Override
	protected void onScrollChanged(int horiz, int vert, int oldHoriz,
			int oldVert) {
		// TODO Auto-generated method stub
		super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int height = getHeight();
		int line_height = getLineHeight();

		int count = height / line_height;
		if (getLineCount() > count) {
			count = getLineCount();
		}
		Rect r = mRect;
		Paint paint = mPaint;
		int baseline = getLineBounds(0, r);

		for (int i = 0; i < count; i++) {

			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
			baseline += getLineHeight();// next line
		}

		// Finishes up by calling the parent method
		super.onDraw(canvas);
	}
}
