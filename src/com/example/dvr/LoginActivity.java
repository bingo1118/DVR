package com.example.dvr;



import org.json.JSONException;
import org.json.JSONObject;

import com.santalen.http.HttpRequest;
import com.santalen.view.TestArrayAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
	Button btn_login;
	EditText account,psw,ip_port; 
	private CheckBox rem_pw, auto_login; 
	private static SharedPreferences sp;
	private ProgressBar pro;
	String userNameValue,passwordValue;
	
	
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case 1:
				Toast.makeText(LoginActivity.this, "��¼�ɹ�", Toast.LENGTH_LONG).show();
				//��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ  
                if(rem_pw.isChecked())  
                {  
                 //��ס�û��������롢  
                  Editor editor = sp.edit();  
                  editor.putString("USER_NAME", userNameValue);  
                  editor.putString("PASSWORD",passwordValue);  
                  editor.commit();  
                }  
				Intent intent=new Intent();
				intent.setClass(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				break;
			case 2:
				Toast.makeText(LoginActivity.this, "�������,����������", Toast.LENGTH_LONG).show();
				pro.setVisibility(View.INVISIBLE);
				break;
			case 3:
				Toast.makeText(LoginActivity.this, "�˻�ͣ�ã�������˺�", Toast.LENGTH_LONG).show();
				pro.setVisibility(View.INVISIBLE);
				break;
			case 4:
				Toast.makeText(LoginActivity.this, "�˻�������", Toast.LENGTH_LONG).show();
				pro.setVisibility(View.INVISIBLE);

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		init();
		rem_pw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (rem_pw.isChecked()) {   
                    sp.edit().putBoolean("ISCHECK", true).commit();  
                      
                }else {  
                    sp.edit().putBoolean("ISCHECK", false).commit();  
                      
                }  
				
			}
		});
		auto_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (rem_pw.isChecked()) {   
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
                      
                }else {  
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
                      
                }  
				
			}
		});
		
		
	}
	
	private void init() {
		btn_login=(Button)findViewById(R.id.login_in);
		btn_login.setOnClickListener(this);
		account=(EditText)findViewById(R.id.accountEdittext);
		psw=(EditText)findViewById(R.id.pwdEdittext);
		ip_port=(EditText)findViewById(R.id.ipEdittext);
		pro=(ProgressBar)findViewById(R.id.progressBar1);
		//���ʵ������  
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
        rem_pw = (CheckBox) findViewById(R.id.checkBox_remenber);  
        auto_login = (CheckBox) findViewById(R.id.checkBox_autologin); 
      //�жϼ�ס�����ѡ���״̬  
        if(sp.getBoolean("ISCHECK", false))  
          {  
            //����Ĭ���Ǽ�¼����״̬  
            rem_pw.setChecked(true);  
            account.setText(sp.getString("USER_NAME", ""));  
            psw.setText(sp.getString("PASSWORD", ""));  
            //�ж��Զ���½��ѡ��״̬  
            if(sp.getBoolean("AUTO_ISCHECK", false))  
            {  
                   //����Ĭ�����Զ���¼״̬  
                   auto_login.setChecked(true);  
                  //��ת����  
                  Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
                  LoginActivity.this.startActivity(intent);  
            }  
          }  
     
	}
	
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_in:
			final String acc=account.getText().toString();
			final String psw1=psw.getText().toString();
			final String ipPort=ip_port.getText().toString();
			userNameValue=acc;
			passwordValue=psw1;
			if(acc==null||acc.isEmpty()||psw1==null||psw1.isEmpty()){
				Toast.makeText(LoginActivity.this, "�˺Ż������벻��Ϊ��", Toast.LENGTH_LONG).show();
				break;
			}
			pro.setVisibility(View.VISIBLE);
			new Thread(){
				public void run(){
					String oo="http://"+ipPort+"/external/"+psw1+"/"+acc+".login";
					String s=HttpRequest.get(oo);
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(s);
			            String result=jsonObject.getString("status");
			            Message msg=new Message();
			            msg.what=Integer.valueOf(result);
			            handler.sendMessage(msg);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			break;

		default:
			break;
		}
	}
	@Override
	protected void onPause(){
	    super.onPause();
	    pro.setVisibility(View.INVISIBLE);
	    finish();//��¼��Ϻ����ٵ�¼����
	}
	/**
	 * �л��˺�
	 */
	public static void change() {
		Editor editor = sp.edit();  
        editor.putBoolean("AUTO_ISCHECK", false);  
        editor.commit();  
	}
	/**
	 * ������ص���ȷ����ʾ��
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
                AlertDialog.Builder alertbBuilder=new AlertDialog.Builder(LoginActivity.this);
                alertbBuilder.setTitle("��ʾ").setMessage("ȷ���˳���").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                //�������Activity
                        	LoginActivity.this.finish();
                        }
                }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                
                        }
                }).create();
                alertbBuilder.show();
                
        }
        return true;
    };
}
