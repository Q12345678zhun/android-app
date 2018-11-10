package baidumapsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;

/**
 * 婕旂ず鍦板浘鍥惧眰鏄剧ず鐨勬帶鍒舵柟娉�
 */
public class LayersDemo extends Activity {

	/**
	 *  MapView 鏄湴鍥句富鎺т欢
	 */
	private MapView mMapView = null;
	/**
	 *  鐢∕apController瀹屾垚鍦板浘鎺у埗 
	 */
	private MapController mMapController = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
           
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }
       
        setContentView(R.layout.activity_layers);
        mMapView = (MapView)findViewById(R.id.bmapView);
       
        mMapController = mMapView.getController();
       
        mMapController.enableClick(true);
        
        mMapController.setZoom(12);
        
        mMapView.setBuiltInZoomControls(true);
      
        double cLat = 39.945 ;
        double cLon = 116.404 ;
        GeoPoint p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));
        mMapController.setCenter(p);
    } 
    
   
    public void setMapMode(View view){
    	 boolean checked = ((RadioButton) view).isChecked();
         switch(view.getId()) {
             case R.id.normal:
                 if (checked)
                	 mMapView.setSatellite(false);
                 break;
             case R.id.statellite:
                 if (checked)
                	 mMapView.setSatellite(true);
                 break;
         }	
    }
   
    public void setTraffic(View view){
    	mMapView.setTraffic(((CheckBox) view).isChecked());
    }
    @Override
    protected void onPause() {
    	
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	
        mMapView.destroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
}
