package com.zy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.zy.memome.R;

public class PageIndicatorImageView extends ImageView {
	public PageIndicatorImageView(Context context) {
		super(context);

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dot_normal);
		this.setImageBitmap(bitmap);
	}

}
