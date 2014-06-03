package com.zy.memome;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zy.dbutil.MemoDbAdapter;
import com.zy.view.LetterStyledEditText;

public class NewMemo extends Activity{
	private EditText mBodyText;
	private Long mRowId;
	private MemoDbAdapter mAdapter;
	private boolean saved = false;
	private View v;
	private TextView date;
	private TextView day;
	private TextView year;
	private String body;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_new_memo);
	      mAdapter = new MemoDbAdapter(this);
	      mAdapter.open();
	      initActionBar();
	      initMemo();
	}
	@Override
	protected void onStop(){
		if(!saved){
			saveMemo();
			
		}
		super.onStop();
		
	}
	
	private void initMemo(){
		mBodyText = (EditText) this.findViewById(R.id.et_main_body);
		

		mRowId = null;
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			body = extras.getString(MemoDbAdapter.BODY);
			mRowId = (long) extras.getInt(MemoDbAdapter.ROWID);
			String created = extras.getString(MemoDbAdapter.CREATED);
			String time = extras.getString(MemoDbAdapter.TIME);
			Log.d("created = ", created+"");
			String[] timings = created.split(", ");
			if (body != null) {
				mBodyText.setText(body);
				day.setText(time+", "+timings[0]);
				date.setText(timings[1]);
				year.setText(timings[2]);
			}
		}else{
			// New memo
			Calendar calendar = Calendar.getInstance();
			DateFormat dateF = DateFormat.getDateInstance(DateFormat.FULL);
			String created = dateF.format(new Date());
			Log.d("created = ", created+"");
			String[] timings = created.split(", ");
			String mins = (calendar.get(Calendar.MINUTE)<10)?"0"+calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE)+"";
			day.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+mins+", "+timings[0]);
			date.setText(timings[1]);
			year.setText(timings[2]);
		}
	}
	  
	  @SuppressLint("NewApi")
	private void initActionBar(){
		  ActionBar ab = this.getActionBar();
		  ab.setDisplayShowCustomEnabled(true);
		  ab.setDisplayShowTitleEnabled(false);
		  ab.setIcon(R.drawable.notes);
		  LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  v = inflator.inflate(R.layout.action_bar, null);
		  date = (TextView) v.findViewById(R.id.tv_date);
		  day = (TextView) v.findViewById(R.id.tv_day);
		  year = (TextView) v.findViewById(R.id.tv_year);
		  ab.setCustomView(v);
		  ab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.action_bar));
		  ab.setDisplayHomeAsUpEnabled(true);
	  }
	  
	 private void saveMemo(){
		String body_new = mBodyText.getText().toString().trim();
		if (mRowId != null) {
			if(!body_new.equals(body)) mAdapter.undateMemo(mRowId+1, body_new);
			
			//mRowId+1 because list iterates from 0, while cursor iterates from 1
			
			
			Log.d("New Memo","content: "+body_new +" id === "+ mRowId);
		} else{

			if(body_new.length()>0){
				mRowId = mAdapter.createMemo(body_new);
			}
		}
		Intent mIntent = new Intent();
		mIntent.putExtra("page_id", mRowId);
		setResult(RESULT_OK, mIntent);
	 }
	 
	 @Override 
	 public boolean onKeyDown(int keyCode,KeyEvent event) {  
	    // 是否触发按键为back键  
	    if (keyCode == KeyEvent.KEYCODE_BACK) {  
	        saveMemo();
	        saved = true;
	        finish();
	        return true;  
	     } else return super.onKeyDown(keyCode,event);  
	 }
	 
	 @Override 
	 public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			saveMemo();
			finish();
			break;
		}
		 return super.onOptionsItemSelected(item);
		 
	 }
}
