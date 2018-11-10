package com.xgr.wonderful.ui;

import com.dong.dongweather.WeatherActivity;
import com.xgr.wonderful.R;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import baidumapsdk.demo.BMapApiDemoMain;
import baidumapsdk.demo.BusLineSearchDemo;
import baidumapsdk.demo.PoiSearchDemo;
import baidumapsdk.demo.RoutePlanDemo;

public class GongFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.baidumap, null);
			Button PresentLoc = (Button) view.findViewById(R.id.order_add_btn);  //此处使得Button和xml中的按钮联系
			Button PresentLoc1 = (Button) view.findViewById(R.id.order_add_btn1); 
			Button PresentLoc2 = (Button) view.findViewById(R.id.order_add_btn2);
		    Button PresentLoc3 = (Button) view.findViewById(R.id.order_add_btn3);

		    PresentLoc.setOnClickListener(new LocationCheckedListener());
			PresentLoc1.setOnClickListener(new LocationCheckedListener1()); 
			PresentLoc2.setOnClickListener(new LocationCheckedListener2());
		    PresentLoc3.setOnClickListener(new LocationCheckedListener3());

		return view;
			}



			class LocationCheckedListener implements OnClickListener {

				
			
			public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), PoiSearchDemo.class);
			getActivity().startActivity(intent);
			}
			
			
}
			
			class LocationCheckedListener1 implements OnClickListener {

				
				
				public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), RoutePlanDemo.class);
				getActivity().startActivity(intent);
				}
				
				
	}
			
			class LocationCheckedListener2 implements OnClickListener {

				
				
				public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), BusLineSearchDemo.class);
				getActivity().startActivity(intent);
				}
				
				
	}

	class LocationCheckedListener3 implements OnClickListener {



		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), WeatherActivity.class);
			getActivity().startActivity(intent);
		}


	}

}