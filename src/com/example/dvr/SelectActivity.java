package com.example.dvr;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SelectActivity extends Activity {
	
	TextView start_date,start_time,stop_date,stop_time;
	String dateSet;  
    Calendar calendar;  
    int flag_date=0;
    int flag_time=0;
    private int mHour=10;
    private int mMinute=10;
    private int mSecond=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select);
		initview();
		
		Button btn=(Button)findViewById(R.id.textView3);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SelectActivity.this,TrackActivity.class);
				startActivity(intent);
				
			}
		});
	} 
	private void initview() {
		start_date=(TextView)findViewById(R.id.start_time_text2);
		calendar = Calendar.getInstance();  
		start_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(SelectActivity.this, "请选择起始日期", Toast.LENGTH_SHORT).show();
				flag_date=1;
				DatePickerDialog datePickerDialog = new DatePickerDialog(  
                        SelectActivity.this, DateSet, calendar  
                                .get(Calendar.YEAR), calendar  
                                .get(Calendar.MONTH), calendar  
                                .get(Calendar.DAY_OF_MONTH));  
                datePickerDialog.show();  
				
			}
		});
		
		stop_date=(TextView)findViewById(R.id.stop_time_text2);
		stop_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(SelectActivity.this, "请选择终止日期", Toast.LENGTH_SHORT).show();
				flag_date=2;
				DatePickerDialog datePickerDialog = new DatePickerDialog(  
                        SelectActivity.this, DateSet, calendar  
                                .get(Calendar.YEAR), calendar  
                                .get(Calendar.MONTH), calendar  
                                .get(Calendar.DAY_OF_MONTH));  
                datePickerDialog.show();  
				
			}
		});
		
		start_time=(TextView)findViewById(R.id.start_time_text3);
		start_time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(SelectActivity.this, "请选择起始时间", Toast.LENGTH_SHORT).show();
				flag_time=1;
				TimePickerDialog datePickerDialog = new TimePickerDialog(  
                        SelectActivity.this, mTimeSetListener,  mHour, mMinute, true);  
                datePickerDialog.show();  
				
			}
		});
		
		stop_time=(TextView)findViewById(R.id.stop_time_text3);
		stop_time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(SelectActivity.this, "请选择起始时间", Toast.LENGTH_SHORT).show();
				flag_time=2;
				TimePickerDialog datePickerDialog = new TimePickerDialog(  
                        SelectActivity.this, mTimeSetListener,  mHour, mMinute, true);  
                datePickerDialog.show();  
				
			}
		});
		
		
	}
	
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	         new TimePickerDialog.OnTimeSetListener()
	{
	     

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			    mHour = hourOfDay;
                mMinute = minute;
                mSecond=0;
                String str=mHour+":"+mMinute;
                switch (flag_time) {
        		case 1:
        			 start_time.setText(str);
        			break;
        		case 2:
        			stop_time.setText(str);
        			break;

        		default:
        			break;
        		} 
               
               
			}
	        
	};
	
	/** 
     * @description 日期设置匿名类 
     */  
    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {  
  
      
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			// 每次保存设置的日期  
            calendar.set(Calendar.YEAR, year);  
            calendar.set(Calendar.MONTH, monthOfYear);  
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);  
  
            String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;  
            System.out.println("set is " + str);  
  
          switch (flag_date) {
		case 1:
			start_date.setText(str); 
			break;
		case 2:
			stop_date.setText(str);
			break;

		default:
			break;
		} 
  
		}  
    };  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
