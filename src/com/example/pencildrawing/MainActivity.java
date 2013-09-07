package com.example.pencildrawing;

import net.hci.graphic.utils.SobelUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	private ImageView imgView;
	Bitmap res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imgView = (ImageView) findViewById(R.id.imgView);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.test);
				res = SobelUtils.Sobel(bitmap);
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			imgView.setImageBitmap(res);
		}
	};


}
