package com.zy.view;

import java.util.Hashtable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;


public class DrawerHScrollView extends HorizontalScrollView { 
	private static final String TAG = "DrawerHScrollView"; 
	
	private IDrawerPresenter drawerPresenter = null; 
	private int currentPage = 0; 
	private int totalPages = 1; 
	private static Hashtable<Integer, Integer> positionLeftTopOfPages = new Hashtable<Integer, Integer>(); 
	
	public DrawerHScrollView(Context context) { 
		super(context); 
	} 
	
	public DrawerHScrollView(Context context, AttributeSet attrs) { 
		super(context, attrs); 
	} 

	public DrawerHScrollView(Context context, AttributeSet attrs, int defStyle) { 
		super(context, attrs, defStyle); 
	} 

	public void reset(){ 
		currentPage = 0; 
		totalPages = 1; 
		drawerPresenter = null; 
		if(positionLeftTopOfPages != null){ 
			positionLeftTopOfPages.clear(); 
		} 
	} 

	public void setParameters(int totalPages, int currentPage, int scrollDisX) { 
		Log.d(TAG, "~~~~~setParameters totalPages:"+totalPages +",currentPage:"+ currentPage +",scrollDisX:"+scrollDisX); 
		this.totalPages = totalPages; 
		this.currentPage = currentPage; 
		positionLeftTopOfPages.clear(); 
		for (int i = 0;i<totalPages;i++){ 
			int posx = (scrollDisX) * i; 
			positionLeftTopOfPages.put(i, posx); 
			Log.d(TAG, "~~~~~setParameters i:"+i +",posx:"+posx); 
		} 
		smoothScrollTo(0, 0); 
	} 

	public void setPresenter(IDrawerPresenter drawerPresenter ) { 
		this.drawerPresenter = drawerPresenter; 
	} 

	@Override 
	public void fling(int velocityX) { 
		Log.d(TAG, "-->fling velocityX:"+velocityX); 
		boolean change_flag = false; 
		if (velocityX > 0 && (currentPage < totalPages - 1)){ 
			currentPage++; 
			change_flag = true; 
		} else if (velocityX < 0 && (currentPage > 0)){ 
			currentPage--; 
			change_flag = true; 
		} 
			if (change_flag){ 
				int postionTo = (Integer)positionLeftTopOfPages.get(new Integer(currentPage)).intValue(); 
				Log.d(TAG, "------smoothScrollTo posx:"+postionTo); 
				smoothScrollTo(postionTo, 0); 
				drawerPresenter.dispatchEvent(totalPages, currentPage); 
			} 
		super.fling(velocityX); 
	} 
} 