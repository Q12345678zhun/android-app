package baidumapsdk.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xgr.wonderful.R;

public class NaviDemo extends Activity {
	
	//澶╁畨闂ㄥ潗鏍�
	double mLat1 = 39.915291; 
   	double mLon1 = 116.403857; 
   	//鐧惧害澶у帵鍧愭爣
   	double mLat2 = 40.056858;   
   	double mLon2 = 116.308194;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi_demo);
		TextView text = (TextView)findViewById(R.id.navi_info);
		text.setText(String.format("璧风偣:(%f,%f)\n缁堢偣:(%f,%f)",mLat1,mLon1,mLat2,mLon2));
	}
   /**
    * 寮�濮嬪鑸�		
    * @param view
    */
   public void startNavi(View view){		
		int lat = (int) (mLat1 *1E6);
	   	int lon = (int) (mLon1 *1E6);   	
	   	GeoPoint pt1 = new GeoPoint(lat, lon);
		lat = (int) (mLat2 *1E6);
	   	lon = (int) (mLon2 *1E6);
	    GeoPoint pt2 = new GeoPoint(lat, lon);
	    // 鏋勫缓 瀵艰埅鍙傛暟
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.startName= "浠庤繖閲屽紑濮�";
        para.endPoint  = pt2;
        para.endName   = "鍒拌繖閲岀粨鏉�";
        
        try {
        	
			 BaiduMapNavigation.openBaiduMapNavi(para, this);
			 
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
			  builder.setMessage("鎮ㄥ皻鏈畨瑁呯櫨搴﹀湴鍥綼pp鎴朼pp鐗堟湰杩囦綆锛岀偣鍑荤‘璁ゅ畨瑁咃紵");
			  builder.setTitle("鎻愮ず");
			  builder.setPositiveButton("纭", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				 dialog.dismiss();
				 BaiduMapNavigation.GetLatestBaiduMapApp(NaviDemo.this);
			   }
			  });

			  builder.setNegativeButton("鍙栨秷", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });

			  builder.create().show();
			 }
		}

	public void startWebNavi(View view) {
		int lat = (int) (mLat1 * 1E6);
		int lon = (int) (mLon1 * 1E6);
		GeoPoint pt1 = new GeoPoint(lat, lon);
		lat = (int) (mLat2 * 1E6);
		lon = (int) (mLon2 * 1E6);
		GeoPoint pt2 = new GeoPoint(lat, lon);
		// 鏋勫缓 瀵艰埅鍙傛暟
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.endPoint = pt2;
		BaiduMapNavigation.openWebBaiduMapNavi(para, this);
	}
}
