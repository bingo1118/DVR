package com.example.dvr;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Window;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class ViewActivity extends Activity{
	
	private VideoView videoView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);
		
		 videoView = (VideoView) findViewById(R.id.videoView1);
	        /**
	         * VideoView������Ƶ���ŵĹ�����Խ��٣�������ԣ���ֻ��start��pause������Ϊ���ṩ����Ŀ��ƣ�
	         * ����ʵ����һ��MediaController����ͨ��setMediaController������������ΪVideoView�Ŀ�������
	         */
	        videoView.setMediaController(new MediaController(this));
	        Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/abc.mp4");
	        videoView.setVideoURI(videoUri);
	        videoView.start();
	}
	
}
