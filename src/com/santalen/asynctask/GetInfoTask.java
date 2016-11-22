package com.santalen.asynctask;

import java.util.Map;

import org.json.JSONException;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.dvr.R;
import com.santalen.http.HttpRequest;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.widget.ProgressBar;

public class GetInfoTask extends AsyncTask<Integer, Integer, String>{
	
	MapView mMapView = null;  
	ProgressBar pro;
	String num;
	double lon,lat;	
	BaiduMap mBaiduMap;
	Marker mMarker;
	
	public GetInfoTask(MapView mMapView, ProgressBar pro,BaiduMap mBaiduMap,Marker mMarker) {  
        super();  
        this.mMapView = mMapView;  
        this.pro = pro;  
        this.mBaiduMap=mBaiduMap;
        this.mMarker=mMarker;
    }  

	@Override
	protected String doInBackground(Integer... params) {
//		new Thread(){
//			public void run(){
					String s=HttpRequest.get("http://162.211.181.117:6607/gatewayHttp?datatype=0004&userNO=123456&deviceNO=00000000000000000001&time=20160513165328");
					try {
						Map maps=HttpRequest.toMap(s);
						num=(String)maps.get("deviceNO");
						lat=Double.valueOf((String)maps.get("latitude"));
						lon=Double.valueOf((String)maps.get("longitude"));
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//定义Maker坐标点  
			          LatLng point = new LatLng(lat,lon);  
			          //构建Marker图标  
			          BitmapDescriptor bitmap = BitmapDescriptorFactory  
			              .fromResource(R.drawable.icon_marka);  
			          //构建MarkerOption，用于在地图上添加Marker  
			          OverlayOptions option1 = new MarkerOptions()
			              .position(point)  
			              .icon(bitmap);  
			          //在地图上添加Marker，并显示  
//			          mBaiduMap.addOverlay(option1);
			          MarkerOptions ooA = new MarkerOptions().position(point).icon(bitmap)
			                  .zIndex(9).draggable(true);
			          mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
			          MapStatus.Builder builder = new MapStatus.Builder();
			          builder.target(point).zoom(18.0f);
			          mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));//视图调到光标所在区域
			      
//				}
//		}.start();
		return null;
	}
	@Override  
    protected void onPreExecute() {  
        pro.setVisibility(ProgressBar.VISIBLE);; 
    }  
	@Override  
    protected void onPostExecute(String result) {  
        pro.setVisibility(ProgressBar.GONE);;  
    }  

}
