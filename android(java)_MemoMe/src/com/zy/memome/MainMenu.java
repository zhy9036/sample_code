package com.zy.memome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.debug.HorizontalDebug;

public class MainMenu extends Activity implements OnClickListener{
	private PopupWindow pop;
	private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button btn_new = (Button) findViewById(R.id.btn_create_new);
        Button btn_list = (Button) findViewById(R.id.btn_view_list);
        Button btn_quit = (Button) findViewById(R.id.btn_quit);
        Button btn_show = (Button) findViewById(R.id.btn_popup);
        Button btn_debug = (Button) findViewById(R.id.btn_debug_horizontal);
        
        btn_debug.setOnClickListener(this);
        btn_new.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        btn_quit.setOnClickListener(this);
        btn_show.setOnClickListener(this);
        
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.popup, null);
        pop = new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,false);
        pop.setOutsideTouchable(true);
       // pop.setFocusable(true);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("id = ", v.getId()+"");
		switch(v.getId()){
			case R.id.btn_create_new:
				Intent intent = new Intent(this, NewMemo.class);
				startActivity(intent);
				break;
			case R.id.btn_view_list:
				Intent intent2 = new Intent(this, GridList.class);
				startActivity(intent2);
				break;
				
			case R.id.btn_quit:
				finish();
				break;
				
			case R.id.btn_popup:
				//tv.setText("3 times");
				if(pop.isShowing()){
					pop.dismiss();
				}else{
					pop.showAsDropDown(v);
					//pop.setFocusable(true);
				}
				break;
				
			case R.id.btn_delete:
				Toast.makeText(this, "WORKED HAH!!", Toast.LENGTH_SHORT).show();
				tv.setText("2 times");
				break;
				
			case R.id.btn_debug_horizontal:
				Intent i = new Intent(this,HorizontalDebug.class);
				startActivity(i);
				break;
		}
				
	}
    
}
