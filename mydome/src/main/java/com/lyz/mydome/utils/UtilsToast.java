package com.lyz.mydome.utils;

import android.content.Context;

public class UtilsToast {

	
	private static android.widget.Toast toast;

	public static void showToast(Context context, String msg){
		if(toast == null){
			toast = android.widget.Toast.makeText(context, "", android.widget.Toast.LENGTH_SHORT);
		}
		
		toast.setText(msg);
		toast.show();
	}
	
}
