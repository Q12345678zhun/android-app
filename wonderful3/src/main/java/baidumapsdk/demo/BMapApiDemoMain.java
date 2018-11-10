package baidumapsdk.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.VersionInfo;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;

public class BMapApiDemoMain extends Activity {
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		ListView mListView = (ListView)findViewById(R.id.listView); 
		// 婵烇綀顕ф慨婵磇stItem闁挎稑鐭侀鏇犵磾椤旇崵鐨戝ù鐘烘硾閹奸攱鎯旈敓锟�
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {  
            	onListItemClick(index);
            }  
        });  
    }

    void onListItemClick(int index) {
		Intent intent = null;
		intent = new Intent(BMapApiDemoMain.this, demos[index].demoClass);
		this.startActivity(intent);
    }
	
	private static final DemoInfo[] demos = {
			
			
      
        
        new DemoInfo(R.string.demo_title_poi, R.string.demo_desc_poi,
				     PoiSearchDemo.class),
       
        new DemoInfo(R.string.demo_title_route, R.string.demo_desc_route,
				     RoutePlanDemo.class),
        new DemoInfo(R.string.demo_title_bus, R.string.demo_desc_bus,
				     BusLineSearchDemo.class),
       
       
      
		
	};
	
	@Override
	protected void onResume() {
	    MyApplication app = (MyApplication)this.getApplication();
	    TextView text = (TextView)findViewById(R.id.text_Info);
		if (!app.m_bKeyRight) {
            text.setText(R.string.key_error);
            text.setTextColor(Color.RED);
		}
		else{
			text.setTextColor(Color.YELLOW);
			text.getText();
		}
		super.onResume();
	}

	
	protected void onDestroy() {
	    MyApplication app = (MyApplication)this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		super.onDestroy();
		System.exit(0);
	}
	
	private  class DemoListAdapter extends BaseAdapter {
		public DemoListAdapter() {
			super();
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			convertView = View.inflate(BMapApiDemoMain.this, R.layout.demo_info_item, null);
			TextView title = (TextView)convertView.findViewById(R.id.title);
			TextView desc = (TextView)convertView.findViewById(R.id.desc);
			if ( demos[index].demoClass == NaviDemo.class  
					|| demos[index].demoClass == ShareDemo.class
                   
					){
				title.setTextColor(Color.YELLOW);
				desc.setTextColor(Color.YELLOW);
			}
			title.setText(demos[index].title);
			desc.setText(demos[index].desc);
			return convertView;
		}
		@Override
		public int getCount() {
			return demos.length;
		}
		@Override
		public Object getItem(int index) {
			return  demos[index];
		}

		@Override
		public long getItemId(int id) {
			return id;
		}
	}
	
   private static class DemoInfo{
		private final int title;
		private final int desc;
		private final Class<? extends android.app.Activity> demoClass;

		public DemoInfo(int title , int desc,Class<? extends android.app.Activity> demoClass) {
			this.title = title;
			this.desc  = desc;
			this.demoClass = demoClass;
		}
	}
}