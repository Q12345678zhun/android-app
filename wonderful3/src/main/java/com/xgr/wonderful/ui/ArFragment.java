package com.xgr.wonderful.ui;

import android.support.v4.app.Fragment;

import com.baidu.ardemo.ARActivity;
import com.xgr.wonderful.R;
import com.xgr.wonderful.ui.GongFragment.LocationCheckedListener;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import baidumapsdk.demo.BMapApiDemoMain;

public class ArFragment extends Fragment{
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.baidumap1, null);
			Button PresentLoc = (Button) view.findViewById(R.id.btn);  //�˴�ʹ��Button��xml�еİ�ť��ϵ
			PresentLoc.setOnClickListener(new LocationCheckedListener());  //��һ�����ڽ�button�ͼ���������
			return view;
			}



			class LocationCheckedListener implements OnClickListener {

				
			
			public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getActivity(),ARActivity.class);
			getActivity().startActivity(intent);
			}
			

}

}
