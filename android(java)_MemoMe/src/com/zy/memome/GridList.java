package com.zy.memome;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;
import com.zy.dbutil.MemoDbAdapter;
import com.zy.pagerutils.GridFragment;
import com.zy.pagerutils.GridItem;

public class GridList extends FragmentActivity implements OnItemLongClickListener{
	
	public PageIndicator mIndicator;
	private String TAG = "GridList"; 
	private ViewPager myPager;
	private PagerAdapter pm;
	private ArrayList<String> note_content = new ArrayList<String>();
	private ArrayList<String> note_time = new ArrayList<String>();
	private ArrayList<GridFragment> fragments = new ArrayList<GridFragment>();
	private ArrayList<GridItem> itemList = new ArrayList<GridItem>();
	 
	private Cursor memoCursor;
	private MemoDbAdapter mAdapter;
	private static final int ACTIVITY_CREATE = 0;
	public static final int ACTIVITY_EDIT=1;
	private static final int INSERT_ID = Menu.FIRST;
	
	private List<Long> selected = new ArrayList<Long>();
	private boolean isLongClick = false;
	private boolean longClickReleased = false;
	private Vibrator vibrator;
	private AdapterView<?> myViews = null;
	private PopupWindow pop;
	
	
	private int item_count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_list);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		mAdapter = new MemoDbAdapter(this);
		mAdapter.open();
		
		myPager = (ViewPager) this.findViewById(R.id.pager);
		mIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);
		renderGrid();
		Log.d(TAG,"redndered!");
		
		ActionBar ab = this.getActionBar();
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
	}

	private void initPop(){
		LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popup, null);
        pop = new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,false);
       
        pop.setOutsideTouchable(true);
	}
	private void renderGrid(){
		
		Log.d(TAG,"redndered!");
		
		fragments.clear();
		memoCursor = mAdapter.getAllNotes();
		item_count = memoCursor.getCount();
		
		startManagingCursor(memoCursor);
		String[] from = new String[]{
			MemoDbAdapter.BODY,MemoDbAdapter.TIME	
		};
		int[] to = new int[]{
			R.id.tv_gridsum, R.id.tv_sub_time	
		};
		
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.gridv_subitem, memoCursor, from, to);
		
		
		int content = memoCursor.getColumnIndex(MemoDbAdapter.BODY);
		int time = memoCursor.getColumnIndex(MemoDbAdapter.TIME);
		int pages = (item_count-1)/6+1;
		
		Log.d(TAG,"pages = "+pages);
		Log.d(TAG,"counts = "+item_count);
		
		memoCursor.moveToFirst();
		for(int i=0;i<pages;i++){
			ArrayList<GridItem> newT = new ArrayList<GridItem>();
			for(int j =0; j<6; j++){
				if(!memoCursor.isAfterLast()){
					GridItem temp = new GridItem(j+6*i,memoCursor.getString(content),memoCursor.getString(time));
					newT.add(temp);
					memoCursor.moveToNext();
				}
			}
		
			fragments.add(new GridFragment(newT,this,memoCursor));
			pm = new PagerAdapter(this.getSupportFragmentManager(), fragments);
			myPager.setAdapter(pm);
			mIndicator.setViewPager(myPager);
		}
	}
	
	private class PagerAdapter extends FragmentStatePagerAdapter {
		private List<GridFragment> fragments;
		 
		public PagerAdapter(FragmentManager fragmentManager, List<GridFragment> fragments) {
		   super(fragmentManager);
		   this.fragments = fragments;
		}
		 
		@Override
		public Fragment getItem(int position) {
		   return this.fragments.get(position);
		}
		 
		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}
	
	private void createMemo() {
		Intent intent = new Intent(this, NewMemo.class);
		startActivityForResult(intent, ACTIVITY_CREATE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0,INSERT_ID,0,"Create New Memo");
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		switch(item.getItemId()){
		case INSERT_ID:
			createMemo();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private OnItemClickListener myOnClickListener= new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long row_id) {
			ImageView iv = (ImageView) view.findViewById(R.id.iv_indicator);
			ImageView iv_off = (ImageView) view.findViewById(R.id.iv_indicator_unselected);
			
			if(!isLongClick){
				Cursor c = memoCursor;
				c.moveToPosition(position);
				Intent intent = new Intent(getApplicationContext(), NewMemo.class);
				intent.putExtra(MemoDbAdapter.ROWID, row_id);  
				intent.putExtra(MemoDbAdapter.BODY, 
						c.getString(c.getColumnIndexOrThrow(MemoDbAdapter.BODY)));
				intent.putExtra(MemoDbAdapter.CREATED, 
						c.getString(c.getColumnIndexOrThrow(MemoDbAdapter.CREATED)));
				intent.putExtra(MemoDbAdapter.TIME, 
						c.getString(c.getColumnIndexOrThrow(MemoDbAdapter.TIME)));
				startActivityForResult(intent, ACTIVITY_EDIT);
			}else if(longClickReleased){
				if(selected.contains(row_id)){ 
					selected.remove(row_id);
					iv.setVisibility(View.GONE);
					iv_off.setVisibility(View.VISIBLE);
				}
				else{
					selected.add(row_id);
					iv.setVisibility(View.VISIBLE);
					iv_off.setVisibility(View.GONE);
				}
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		
		Intent i = intent;
		long pageId = intent.getLongExtra("page_id",0);
		
		Log.d(TAG, "PAGE NUMBER = "+pageId);
		Log.d(TAG, "PAGE reminder = "+(pageId-1)/6);
		renderGrid();
		myPager.setCurrentItem((int) ((pageId-1)/6));
		Log.d(TAG, "onActivity Called");
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> views, View arg1, int arg2,
			long arg3) {
		if(!isLongClick){
			myViews = views;
			vibrator.vibrate(40);
			for(int i = 0; i<views.getChildCount(); i++){
				View v = views.getChildAt(i);
				TextView temp = (TextView) v.findViewById(R.id.tv_gridsum);
				if(v != arg1){
					((ImageView) v.findViewById(R.id.iv_indicator_unselected)).setVisibility(View.VISIBLE);
				}else{
					((ImageView) v.findViewById(R.id.iv_indicator)).setVisibility(View.VISIBLE);
					selected.add(arg3);
					longClickReleased = true;
				}
				
				temp.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.gridsub_editmode));
			}
			isLongClick = true;
		}
		return false;
	}
	
	private void deleteNote(){
		for(long id: selected){
			mAdapter.deleteDiary(id);
		}
		renderGrid();
	}
	 @Override 
	 public boolean onKeyDown(int keyCode,KeyEvent event) {  
	    // 是否触发按键为back键  
	    if (keyCode == KeyEvent.KEYCODE_BACK) {  
	        // 弹出退出确认框 
	    	if(isLongClick){
	    		longClickReleased = false;
		    	isLongClick = false;
		    	for(int i = 0; i<myViews.getChildCount(); i++){
					View v = myViews.getChildAt(i);
					TextView temp = (TextView) v.findViewById(R.id.tv_gridsum);
					((ImageView) v.findViewById(R.id.iv_indicator_unselected)).setVisibility(View.GONE);
					v.findViewById(R.id.iv_indicator).setVisibility(View.GONE);
					temp.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.gridsub_normal));
				}
		    	return true;
	    	}else{
	    		return super.onKeyDown(KeyEvent.KEYCODE_BACK, event);
	    	}
	     } else// 如果不是back键正常响应  
	         return super.onKeyDown(keyCode,event);  
	 }
}
