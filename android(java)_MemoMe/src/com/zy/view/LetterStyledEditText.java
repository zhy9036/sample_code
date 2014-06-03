package com.zy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

public class LetterStyledEditText extends EditText {
	private Rect mRect;
	private Paint mPaint;
	private Paint mPaint_red;
	private float fontHeight = 0;
	private float leading = 0;
	private int lineCount = 0;
	private int textSizeinDp;
	
	public LetterStyledEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}
	
	public LetterStyledEditText(Context context) {
		super(context);
		initPaint();
	}
	
	public LetterStyledEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}
	
	private void initPaint(){
		mRect = new Rect();
		mPaint = new Paint();
		mPaint_red = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint_red.setStyle(Paint.Style.STROKE);
		//mPaint.setColor(0x800000FF);
		mPaint.setColor(Color.GRAY);
		mPaint_red.setColor(Color.RED);
		mPaint_red.setStrokeWidth(2);
		int dpi=this.getContext().getResources().getDisplayMetrics().densityDpi;
		mPaint.setPathEffect(new DashPathEffect(new float[]{5,5,5,5},5));
		textSizeinDp = (int) this.getTextSize()*72/dpi;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		initEt();
	}
	
	private void initEt(){
		int lineHeight = this.getLineHeight();
		int viewHeight = this.getHeight();
		lineCount = viewHeight/lineHeight;
		this.setGravity(Gravity.TOP);
		float textSize = this.getTextSize();
		Paint p = new Paint();
		p.setTextSize(textSize);
		FontMetrics fontM = p.getFontMetrics();
		//fontHeight = fontM.descent - fontM.ascent;
		fontHeight = this.getLineHeight();
		//this.setLineSpacing(fontHeight, 1);
		leading = fontM.leading+textSizeinDp+2;
		//setBackgroundColor(Color.parseColor("#EEEE00"));
	}
	@SuppressLint("NewApi")
	@Override
	protected void onDraw(Canvas canvas){
		int count = this.getLineCount();
		//this.setLineSpacing(cur+8, 1);
		Rect r = mRect;
		Paint paint = mPaint;
		
		if(lineCount == 0) initEt();
		canvas.drawLine(15+20, 0, 15+20, getHeight(), mPaint_red);
		canvas.drawLine(25+20, 0, 25+20, getHeight(), mPaint_red);
		if(count<= lineCount){
			for(int i =0; i<lineCount; i++){
				if(i>0){
				
				 canvas.drawLine(0,
	                        fontHeight * i + leading,
	                        getWidth(),
	                        fontHeight * i + leading,
	                        mPaint);
				 
				}
			}
			
		}else{
			for(int i=0; i<count;i++){
	
				// Gets the baseline coordinates for the current line of text
				int baseline = this.getLineBounds(i,r)+ textSizeinDp;
				
				/*
	             * Draws a line in the background from the left of the rectangle to the right,
	             * at a vertical position one dip below the baseline, using the "paint" object
	             * for details.
	             */
				canvas.drawLine(0, baseline+1, getWidth(), baseline+1, mPaint);
				canvas.drawLine(15+20, 0, 15+20, getHeight()+baseline+1, mPaint_red);
				canvas.drawLine(25+20, 0, 25+20, getHeight()+baseline+1, mPaint_red);
			}
		}
		super.onDraw(canvas);
	}

}
