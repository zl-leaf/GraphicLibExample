package com.example.pencildrawing;

import net.hci.graphic.utils.SobelUtils;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends Activity implements
		CameraPreview.OnCameraStatusListener {
	public static final int TAKE_PHOTO = 1;
	
	
	private CameraPreview mCameraPreview;
	private boolean isTaking = false; // 拍照中
	
	private ImageView imgView;
	private Button mTakePhotoBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		// 设置横屏
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);

		mCameraPreview = (CameraPreview) findViewById(R.id.preview);
		mCameraPreview.setOnCameraStatusListener(this);
		
		imgView = (ImageView) findViewById(R.id.imgView);
		mTakePhotoBtn = (Button) findViewById(R.id.take_photo_btn);
		mTakePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCameraPreview.setVisibility(View.VISIBLE);
				(findViewById(R.id.imgPreView_ll)).setVisibility(View.GONE);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && !isTaking) {
			isTaking = true;
			mCameraPreview.takePicture();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onCameraStopped(byte[] data) {
		isTaking = false;
		mCameraPreview.setVisibility(View.GONE);
		(findViewById(R.id.imgPreView_ll)).setVisibility(View.VISIBLE);

		final Bitmap photo = BitmapFactory.decodeByteArray(data, 0, data.length);
//		MainActivity.photo = bitmap;
//		setResult(RESULT_OK);
//		finish();
		
		if (photo != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap res;
					res = SobelUtils.Sobel(photo);
					photo.recycle();
					Message msg = new Message();
					msg.what = TAKE_PHOTO;
					msg.obj = res;
					handler.sendMessage(msg);
				}
			}).start();
		}
	}

	@Override
	public void onAutoFocus(boolean success) {

	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			imgView.setImageBitmap((Bitmap)msg.obj);
		}
	};

}
