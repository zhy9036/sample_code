package com.zy.pagerutils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.dbutil.MemoDbAdapter;
import com.zy.memome.GridList;
import com.zy.memome.NewMemo;
import com.zy.memome.R;



@SuppressLint("ValidFragment")
public class GridFragment extends android.support.v4.app.Fragment implements OnItemLongClickListener{
 
	 private GridView mGridView;
	 private GridAdapter mGridAdapter;
	 private ArrayList<GridItem> itemList;
	 private Activity activity;
	 private Cursor mCursor;
	 private boolean isLongClick = false;
	 private Vibrator vibrator;
	 
	 
	
	public GridFragment(ArrayList<GridItem> itemList, Activity activity, Cursor mCursor) {
	  	this.activity = activity;
	  	this.itemList = itemList;
	  	this.mCursor = mCursor;
	  	vibrator = (Vibrator) this.activity.getSystemService("vibrator");
	 }
 	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		  View view;
		  view = inflater.inflate(R.layout.grid, container, false);
		  mGridView = (GridView) view.findViewById(R.id.gridView);
		  return view;
	 }
 
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
		 
		  if (activity != null) {
			  
			  mGridAdapter = new GridAdapter(activity);
			  if (mGridView != null) {
				   mGridView.setAdapter(mGridAdapter);
			  }
			  mGridView.setOnItemClickListener(new OnItemClickListener() {
				   @Override
				   public void onItemClick(AdapterView parent, View view,int position, long id) {
				    	//onGridItemClick((GridView) parent, view, position, id);
					   
					   ImageView iv = (ImageView) view.findViewById(R.id.iv_indicator);
					   ImageView iv_off = (ImageView) view.findViewById(R.id.iv_indicator_unselected);
					   
					   if(!isLongClick){
						   int mId = itemList.get(position).id;
						   
						   mCursor.moveToPosition(mId);
						   Cursor c = mCursor;
						   Intent intent = new Intent(activity, NewMemo.class);
						   intent.putExtra(MemoDbAdapter.ROWID, mId);
						   intent.putExtra(MemoDbAdapter.BODY, 
									c.getString(c.getColumnIndexOrThrow(MemoDbAdapter.BODY)));
						   intent.putExtra(MemoDbAdapter.CREATED, 
									c.getString(c.getColumnIndexOrThrow(MemoDbAdapter.CREATED)));
						   intent.putExtra(MemoDbAdapter.TIME, 
									c.getString(c.getColumnIndexOrThrow(MemoDbAdapter.TIME)));
						   intent.putExtra(MemoDbAdapter.ROWID, itemList.get(position).id);
						   startActivityForResult(intent, GridList.ACTIVITY_EDIT);
					   }else if(false){//longClickReleased){
						   iv.setVisibility(View.GONE);
						   iv_off.setVisibility(View.VISIBLE);
					   }
				    	/*
				
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
				    	 * 
				    	 */
				   }
			  });
		  }
	 }
 
	 public void onGridItemClick(GridView g, View v, int position, long id) {
		  Toast.makeText(activity,"Position Clicked: - " + itemList.get(position).id + " & " + "Text is: - "
		      + position, Toast.LENGTH_SHORT).show();
		  Log.e("TAG", "POSITION CLICKED " + itemList.get(position).id);
	 }
	 
	 private class GridAdapter extends BaseAdapter{
		 private LayoutInflater myInflater;
		 private Context ctx;
		 
		 public class ViewHolder {
			public TextView body;
			public TextView time;
			public ImageView selected;
			public ImageView non_selected;
		}
		 
		 public GridAdapter(Context ctx){
			 this.ctx = ctx;
			 myInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemList.size();
		}

		@Override
		public Object getItem(int index) {
			// TODO Auto-generated method stub
			return itemList.get(index);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return itemList.get(position).id;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup root) {
			
			View view = convertView;
			ViewHolder viewHolder = new ViewHolder();
			if(view == null){
				view = myInflater.inflate(R.layout.gridv_subitem, root,false);
				viewHolder.body = (TextView) view.findViewById(R.id.tv_gridsum);
				viewHolder.time = (TextView) view.findViewById(R.id.tv_sub_time);
				
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.body.setText(itemList.get(index).content);
			viewHolder.time.setText(itemList.get(index).time);
			return view;
		}
		 
	 }
	 /*
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
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
	*/

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
