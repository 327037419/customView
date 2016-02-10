package com.lyz.basepagerstatefragment.widget;

import android.content.Context;
import android.widget.Toast;

public class UtilTost {

	
	private static Toast toast;

	public static void showToast(Context context, String msg){
		if(toast == null){
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		
		toast.setText(msg);
		toast.show();
	}
	
}
