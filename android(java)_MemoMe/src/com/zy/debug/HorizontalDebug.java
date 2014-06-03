package com.zy.debug;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.GridView;

import com.zy.memome.R;

public class HorizontalDebug extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_horizontal_debug);
		GridView gv = (GridView) this.findViewById(R.id.gv_debug);
		Button d = (Button) this.findViewById(R.id.btn_gv_debug);
		gv.addView(d);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.horizontal_debug, menu);
		return true;
	}

}
