package com.zhuolang.fu.microdoctorclient.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuolang.fu.microdoctorclient.R;


public class CustomWaitDialog extends Dialog {
	private Context context = null;
	private static CustomWaitDialog customProgressDialog = null;

	public CustomWaitDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomWaitDialog(Context context, int theme) {
		super(context, theme);
	}

	public static void show(Context context,String message){
		customProgressDialog = new CustomWaitDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.setContentView(R.layout.progressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		//set message
		TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
		tvMsg.setText(message);

		customProgressDialog.show();
	}

	public static void miss(){
		if (customProgressDialog!=null){
			customProgressDialog.dismiss();
		}
	}


	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}
	
}
