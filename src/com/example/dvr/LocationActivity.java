package com.example.dvr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.santalen.asynctask.GetInfoTask;
import com.santalen.http.HttpRequest;
import com.santalen.view.TestArrayAdapter;

import Data.BusData;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class LocationActivity extends Activity {
	
	MapView mMapView = null;  
	BaiduMap mBaiduMap;
	ProgressBar pro;//����ʱ������
	
	private Marker mMarkerA;//��ǰ�豸λ�ñ�־
	String driverNO;//��ǰ�豸
	BusData busdata;
	 private InfoWindow mInfoWindow;
	
	double lon,lat;	//��γ��

	Button btn_refresh;//ˢ�°�ť
	
//	�첽����λ�ñ�־
//	GetInfoTask abc;
	
	Spinner spi;
	private ArrayAdapter<String> mAdapter ;
	  private String [] mStringArray;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.activity_location);
		
		//�豸ѡ������������
		spi=(Spinner)findViewById(R.id.spinner1);
		mStringArray=getResources().getStringArray(R.array.spingarr);
	    //ʹ���Զ����ArrayAdapter
	    mAdapter =new TestArrayAdapter(LocationActivity.this,mStringArray);
	    spi.setAdapter(mAdapter);
	    //����Itemѡ���¼�
	    spi.setOnItemSelectedListener(new ItemSelectedListenerImpl());

		//��ȡ��ͼ�ؼ�����  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();
        pro=(ProgressBar)findViewById(R.id.progressBar1);
        //�첽���ر�־
//        abc=new GetInfoTask(mMapView, pro,mBaiduMap,mMarkerA);
//        abc.execute();
        
        btn_refresh=(Button)findViewById(R.id.button_refresh);
        btn_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changedriver(driverNO);
			}
		});
	}
	
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onPause();  
        }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	/**
     * ��λSDK��������
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view ���ٺ��ڴ����½��յ�λ��
            if (location == null || mMapView == null) {
                return;
            }
            
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);//���ö�λ���λ��
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    
    /**
     * �豸���������������
     * @author santalen
     *
     */
    private class ItemSelectedListenerImpl implements OnItemSelectedListener{
	   
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if(position==0){
				changedriver(mStringArray[1]);
				driverNO=mStringArray[1];
			}else{
				changedriver(mStringArray[position]);
				driverNO=mStringArray[position];
			}
			Toast.makeText(LocationActivity.this,"�л���:"+mStringArray[position], Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	    	
	    }
	private void changedriver(final String driverNO) {
		new Thread(){
			public void run(){
					String s=HttpRequest.get("http://162.211.181.117:6607/gatewayHttp?datatype=0004&userNO=123456&deviceNO="+driverNO+"&time=20160513165328");
					try {
						Map maps=HttpRequest.toMap(s);
						busdata=new BusData((String)maps.get("deviceNO"),
											(String)maps.get("longitude"), 
											(String)maps.get("latitude"),
											(String)maps.get("routeNo"),
											(String)maps.get("groundSpeed"));
						lat=Double.valueOf((String)maps.get("latitude"));
						lon=Double.valueOf((String)maps.get("longitude"));
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//����Maker�����  
			          LatLng point = new LatLng(lat,lon);  
			          if(mMarkerA==null){
			        	  BitmapDescriptor bitmap = BitmapDescriptorFactory  
					              .fromResource(R.drawable.icon_marka); 
			        	  MarkerOptions ooA = new MarkerOptions().position(point).icon(bitmap)
				                  .zIndex(9).draggable(true);
			        	  mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			          }else{
			        	  mMarkerA.setPosition(point);
				          mBaiduMap.hideInfoWindow();
			          }
			       //��������ʾ������Ϣ
			          mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			              public boolean onMarkerClick(final Marker marker) {
			                  TextView tv = new TextView(getApplicationContext());
			                  tv.setBackgroundResource(R.drawable.btn_login);
			                  if (marker == mMarkerA) {
			                	  tv.setText("�豸�ţ�"+busdata.getDeviceNO()+"\r\n"+
					                    		   "���ȣ�"+busdata.getLongitude()+"\r\n"+
					                    		   "γ�ȣ�"+busdata.getLatitude()+"\r\n"+
					                    		   "·�ߺţ�"+busdata.getRouteNo()+"\r\n"+
					                    		   "�ٶȣ�"+busdata.getGroundSpeed());
			                      LatLng ll = marker.getPosition();
			                      mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(tv), ll, -100, null);
			                      mBaiduMap.showInfoWindow(mInfoWindow);
			                  } 
								return false;
							}
						});
			          MapStatus.Builder builder = new MapStatus.Builder();
			          builder.target(point).zoom(18.0f);
			          mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));//��ͼ���������������
			      
				}
		}.start();
	}
}
