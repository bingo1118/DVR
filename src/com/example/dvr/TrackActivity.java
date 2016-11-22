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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.santalen.http.HttpRequest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TrackActivity extends Activity {
	
	MapView mMapView = null;  
	BaiduMap mBaiduMap;
	
	
	
	String num;
	double lon,lat;	
	// ��λ���
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
	

   
    
    boolean isFirstLoc = true; // �Ƿ��״ζ�λ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.activity_track);
		
		//��ȡ��ͼ�ؼ�����  
        mMapView = (MapView) findViewById(R.id.bmapView1);  
        mBaiduMap = mMapView.getMap();
        
        
		new Thread(){
			public void run(){
//				while(true){
//					try {
//						Thread.sleep(10000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					String s=HttpRequest.get("http://162.211.181.117:6607/gatewayHttp?datatype=0004&userNO=123456&deviceNO=00000000000000000001&time=20160513165328");
					try {
						Map maps=HttpRequest.toMap(s);
						num=(String)maps.get("deviceNO");
						lat=Double.valueOf((String)maps.get("latitude"));
						lon=Double.valueOf((String)maps.get("longitude"));
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//����Maker�����  
			          LatLng point = new LatLng(lat,lon);  
			          //����Markerͼ��  
			          BitmapDescriptor bitmap = BitmapDescriptorFactory  
			              .fromResource(R.drawable.icon_marka);  
			          //����MarkerOption�������ڵ�ͼ�����Marker  
			          OverlayOptions option1 = new MarkerOptions()
			              .position(point)  
			              .icon(bitmap);  
			          //�ڵ�ͼ�����Marker������ʾ  
			          mBaiduMap.addOverlay(option1);
			          MapStatus.Builder builder = new MapStatus.Builder();
			          builder.target(point).zoom(18.0f);
			          mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));//��ͼ���������������
			      
				}
//			}
		}.start();
        
        
        
        mCurrentMode = LocationMode.NORMAL;
       
        
      //��ȡ��ͼ�ؼ�����  
        mMapView = (MapView) findViewById(R.id.bmapView1);  
        mBaiduMap = mMapView.getMap();
        // ������λͼ��
        mBaiduMap.setMyLocationEnabled(true);
        // ��λ��ʼ��
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ��gps
        option.setCoorType("bd09ll"); // ������������
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        
        Button track=(Button)findViewById(R.id.textView2);
        track.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			 new Thread(){
				public void run(){
					String s=HttpRequest.sendGet("http://162.211.181.117:6607/gatewayHttp?datatype=0002&deviceNO=00000000000000000001&20150806080808&20160810080808");
			        
			        try {
			        	JSONObject jsonObject = new JSONObject(s);
			            JSONArray jsonarray=jsonObject.getJSONArray("listTrackData");
			            int jsonarray_size=jsonarray.length();
	  			          /**
			               * ��ӹ켣
			               */
			            //����������Դ
			              BitmapDescriptor custom1 = BitmapDescriptorFactory
			              				.fromResource(R.drawable.icon_road_red_arrow);
			              BitmapDescriptor custom2 = BitmapDescriptorFactory
			              				.fromResource(R.drawable.icon_road_green_arrow);
			              BitmapDescriptor custom3 = BitmapDescriptorFactory
			              				.fromResource(R.drawable.icon_road_blue_arrow);
			             //�����������
			              List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
			              customList.add(custom1);
			              customList.add(custom2);
			              customList.add(custom3);
			              List<LatLng> points = new ArrayList<LatLng>();
			              List<Integer> index = new ArrayList<Integer>();
			              for(int i=0;i<jsonarray_size;i++){
			              	LatLng pt123 = new LatLng(Double.valueOf(jsonarray.getJSONObject(i).getString("latitude ")),
			              			Double.valueOf(jsonarray.getJSONObject(i).getString("longitude")));
			              	points.add(pt123);//��Ԫ��
			              	if(i%2==0){
			              		index.add(1);//���øõ����������
			              	}else{
			              		index.add(2);//���øõ����������
			              	}
			                  
			              }
			              //�������
			              OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
			              //��ӵ���ͼ
			             
			              mBaiduMap.addOverlay(ooPolyline);
			              mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));//���õ�ͼ���ű���
			              
			              BitmapDescriptor bdA = BitmapDescriptorFactory
			                      .fromResource(R.drawable.icon_marka);
			              LatLng llA1 = new LatLng(0, 0);
			              MarkerOptions ooA1 = new MarkerOptions().position(llA1).icon(bdA)
	            	                .zIndex(9).draggable(true);
			              Marker mMarkerA=(Marker) (mBaiduMap.addOverlay(ooA1));
			              
			              LatLng llA = new LatLng(points.get(0).latitude, points.get(0).longitude);
	            		  MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
	            	                .zIndex(9).draggable(true);
	            	       mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			              
			              for(int i=0;i<points.size()-1;i++){
			            	  double x=points.get(i).latitude;
			            	  double y=points.get(i).longitude;
			            	  double lengh_y=(points.get(i+1).longitude-points.get(i).longitude)/1000;
			            	  double heigh_x=(points.get(i+1).latitude-points.get(i).latitude)/1000;
			            	  double h_l=heigh_x/lengh_y;
			            	  while(x<=points.get(i+1).latitude){
			            		  try {
									Thread.sleep(1);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			            		  LatLng ll = mMarkerA.getPosition();
			            		  x=ll.latitude+heigh_x;
			            		  y=ll.longitude+lengh_y;
		                            LatLng llNew = new LatLng(x,y);
		                            mMarkerA.setPosition(llNew);
		                            mBaiduMap.hideInfoWindow();
			            	  }
			              }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
	
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
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(18.0f);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));//��ͼ���������������
              /**
               * ��ӱ��  
               */
//              //����Maker�����  
//                LatLng point = new LatLng(23.048066,113.387208);  
//                //����Markerͼ��  
//                BitmapDescriptor bitmap = BitmapDescriptorFactory  
//                    .fromResource(R.drawable.icon_marka);  
//                //����MarkerOption�������ڵ�ͼ�����Marker  
//                OverlayOptions option1 = new MarkerOptions()  
//                    .position(point)  
//                    .icon(bitmap);  
//                //�ڵ�ͼ�����Marker������ʾ  
//                mBaiduMap.addOverlay(option1);
//                
//                /**
//                 * ��ӹ켣
//                 */
//              //����������Դ
//                BitmapDescriptor custom1 = BitmapDescriptorFactory
//                				.fromResource(R.drawable.icon_road_red_arrow);
//                BitmapDescriptor custom2 = BitmapDescriptorFactory
//                				.fromResource(R.drawable.icon_road_green_arrow);
//                BitmapDescriptor custom3 = BitmapDescriptorFactory
//                				.fromResource(R.drawable.icon_road_blue_arrow);
//               //�����������
//                List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
//                customList.add(custom1);
//                customList.add(custom2);
//                customList.add(custom3);
//                List<LatLng> points = new ArrayList<LatLng>();
//                List<Integer> index = new ArrayList<Integer>();
//                for(double i=0;i<10;i++){
//                	LatLng pt123 = new LatLng(location.getLatitude()+i*Math.random()/1000,
//                            location.getLongitude()-i*Math.random()/1000);
//                	points.add(pt123);//��Ԫ��
//                	if(i%2==0){
//                		index.add(1);//���øõ����������
//                	}else{
//                		index.add(2);//���øõ����������
//                	}
//                    
//                }
//                //�������
//                OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
//                //��ӵ���ͼ
//                mBaiduMap.addOverlay(ooPolyline);
                
//                /**
//                 * ������ָ���
//                 */
//              //������������ʾ�������  
//                LatLng llText = new LatLng(location.getLatitude(),
//                        location.getLongitude());  
//                //��������Option���������ڵ�ͼ���������  
//                OverlayOptions textOption = new TextOptions()  
//                    .bgColor(0xAAFFFF00)  
//                    .fontSize(30)  //���������С
//                    .fontColor(0xFFFF00FF)  
//                    .text("��������")  
//                    .rotate(0)  //���ֽǶ�
//                    .position(llText);  
//                //�ڵ�ͼ����Ӹ����ֶ�����ʾ  
//                mBaiduMap.addOverlay(textOption);
//                
//                /**
//                 * �콣�м�ͼ��
//                 */
//              //����Ground����ʾ����Χ  
//                LatLng southwest = new LatLng(location.getLatitude(),
//                        location.getLongitude());  
//                LatLng northeast = new LatLng(location.getLatitude()+0.01,
//                        location.getLongitude()+0.01);  
//                LatLngBounds bounds = new LatLngBounds.Builder()  
//                    .include(northeast)  
//                    .include(southwest)  
//                    .build();  
//                //����Ground��ʾ��ͼƬ  
//                BitmapDescriptor bdGround = BitmapDescriptorFactory  
//                    .fromResource(R.drawable.ground_overlay);  
//                //����Ground������ѡ��  
//                OverlayOptions ooGround = new GroundOverlayOptions()  
//                    .positionFromBounds(bounds)  
//                    .image(bdGround)  
//                    .transparency(0.8f);  
//                //�ڵ�ͼ�����Ground������  
//                mBaiduMap.addOverlay(ooGround);
//                
//                /**
//                 * 
//                 */
//            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
