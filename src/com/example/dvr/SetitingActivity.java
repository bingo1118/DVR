package com.example.dvr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SetitingActivity extends Activity{
	Button btn_change_account;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		btn_change_account=(Button)findViewById(R.id.change_account);
		btn_change_account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog=new AlertDialog.Builder(SetitingActivity.this);
				dialog.setTitle("提示：");
				dialog.setMessage("是否确定退出当前账号？");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LoginActivity.change();
						Intent intent=new Intent(SetitingActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
						
					}
				});
				dialog.show();
				
			}
		});
	}
	
}
