package com.example.dvr;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AbstractMyActivityGroup{
    //���ص�Activity�����֣�LocalActivityManager����ͨ����Щ���������Ҷ�Ӧ��Activity�ġ�
    private static final String CONTENT_ACTIVITY_NAME_2 = "LocationActivity";
    private static final String CONTENT_ACTIVITY_NAME_0 = "SelectActivity";
    private static final String CONTENT_ACTIVITY_NAME_1 = "ReviewActivity";
    private static final String CONTENT_ACTIVITY_NAME_3 = "ViewActivity";
    private static final String CONTENT_ACTIVITY_NAME_4 = "SetitingActivity";
    
    
 // ����һ������������ʶ�Ƿ��˳�  
    private static boolean isExit = false;  
  
    private static Handler mHandler = new Handler() {  
  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            isExit = false;  
        }  
    };  
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_activity_group);
        super.onCreate(savedInstanceState);
      
        
        ((RadioButton)findViewById(R.id.radio_button2)).setChecked(true);
        setContainerView(CONTENT_ACTIVITY_NAME_2, LocationActivity.class);
    }
    
    /**
     * �ҵ��Զ���id�ļ���Activity��View
     */
    @Override
    protected ViewGroup getContainer() {
        return (ViewGroup) findViewById(R.id.container);
    }
    
    /**
     * ��ʼ����ť
     */
    @Override
    protected void initRadioBtns() {
        initRadioBtn(R.id.radio_button2);
        initRadioBtn(R.id.radio_button0);
        initRadioBtn(R.id.radio_button1);
        initRadioBtn(R.id.radio_button3);
        initRadioBtn(R.id.radio_button4);
    }
    
    /**
     * ������ť�����ʱ�����巢���ı仯
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
            
            case R.id.radio_button0:
                setContainerView(CONTENT_ACTIVITY_NAME_0, SelectActivity.class);
                break;
                
            case R.id.radio_button1:
                setContainerView(CONTENT_ACTIVITY_NAME_1, ReviewActivity.class);
                break;
                
            case R.id.radio_button2:
                setContainerView(CONTENT_ACTIVITY_NAME_2, LocationActivity.class);
                break;
                
            case R.id.radio_button3:
                setContainerView(CONTENT_ACTIVITY_NAME_3, ViewActivity.class);
                break;
                
            case R.id.radio_button4:
                setContainerView(CONTENT_ACTIVITY_NAME_4, SetitingActivity.class);
                break;
                
            default:
                break;
            }
        }
    }
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            exit();  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
      
    private void exit() {  
        if (!isExit) {  
            isExit = true;  
            Toast.makeText(getApplicationContext(), "�ٰ�һ�κ��˼��˳�����",  
                    Toast.LENGTH_SHORT).show();  
            // ����handler�ӳٷ��͸���״̬��Ϣ  
            mHandler.sendEmptyMessageDelayed(0, 2000);  
        } else {  
            
            this.finish();  
        }  
    }  
}