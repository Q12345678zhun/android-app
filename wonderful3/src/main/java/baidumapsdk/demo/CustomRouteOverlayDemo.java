package baidumapsdk.demo;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 姝emo鐢ㄦ潵灞曠ず濡備綍鐢ㄨ嚜宸辩殑鏁版嵁鏋勯�犱竴鏉¤矾绾垮湪鍦板浘涓婄粯鍒跺嚭鏉�
 *
 */
public class CustomRouteOverlayDemo  extends Activity{

	//鍦板浘鐩稿叧
	MapView mMapView = null;	// 鍦板浘View
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
         * 浣跨敤鍦板浘sdk鍓嶉渶鍏堝垵濮嬪寲BMapManager.
         * BMapManager鏄叏灞�鐨勶紝鍙负澶氫釜MapView鍏辩敤锛屽畠闇�瑕佸湴鍥炬ā鍧楀垱寤哄墠鍒涘缓锛�
         * 骞跺湪鍦板浘鍦板浘妯″潡閿�姣佸悗閿�姣侊紝鍙杩樻湁鍦板浘妯″潡鍦ㄤ娇鐢紝BMapManager灏变笉搴旇閿�姣�
         */
        MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * 濡傛灉BMapManager娌℃湁鍒濆鍖栧垯鍒濆鍖朆MapManager
             */
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }
		setContentView(R.layout.activity_customroute);
		CharSequence titleLable="璺嚎瑙勫垝鍔熻兘鈥斺�旇嚜璁捐矾绾跨ず渚�";
        setTitle(titleLable);
		//鍒濆鍖栧湴鍥�
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(13);
        
        /** 婕旂ず鑷畾涔夎矾绾夸娇鐢ㄦ柟娉�	
		 *  鍦ㄥ寳浜湴鍥句笂鐢讳竴涓寳鏂椾竷鏄�
		 *  鎯崇煡閬撴煇涓偣鐨勭櫨搴︾粡绾害鍧愭爣璇风偣鍑伙細http://api.map.baidu.com/lbsapi/getpoint/index.html	
		 */
		GeoPoint p1 = new GeoPoint((int)(39.9411 * 1E6),(int)(116.3714 * 1E6));
		GeoPoint p2 = new GeoPoint((int)(39.9498 * 1E6),(int)(116.3785 * 1E6));
		GeoPoint p3 = new GeoPoint((int)(39.9436 * 1E6),(int)(116.4029 * 1E6));
		GeoPoint p4 = new GeoPoint((int)(39.9329 * 1E6),(int)(116.4035 * 1E6));
		GeoPoint p5 = new GeoPoint((int)(39.9218 * 1E6),(int)(116.4115 * 1E6));
		GeoPoint p6 = new GeoPoint((int)(39.9144 * 1E6),(int)(116.4230 * 1E6));
		GeoPoint p7 = new GeoPoint((int)(39.9126 * 1E6),(int)(116.4387 * 1E6));
	    //璧风偣鍧愭爣
		GeoPoint start = p1;
		//缁堢偣鍧愭爣
		GeoPoint stop  = p7;
		//绗竴绔欙紝绔欑偣鍧愭爣涓簆3,缁忚繃p1,p2
		GeoPoint[] step1 = new GeoPoint[3];
		step1[0] = p1;
		step1[1] = p2 ;
		step1[2] = p3;
		//绗簩绔欙紝绔欑偣鍧愭爣涓簆5,缁忚繃p4
		GeoPoint[] step2 = new GeoPoint[2];
		step2[0] = p4;
		step2[1] = p5;
		//绗笁绔欙紝绔欑偣鍧愭爣涓簆7,缁忚繃p6
		GeoPoint[] step3 = new GeoPoint[2];
		step3[0] = p6;
		step3[1] = p7;
		//绔欑偣鏁版嵁淇濆瓨鍦ㄤ竴涓簩缁存暟鎹腑
		GeoPoint [][] routeData = new GeoPoint[3][];
		routeData[0] = step1;
		routeData[1] = step2;
		routeData[2] = step3;
		//鐢ㄧ珯鐐规暟鎹瀯寤轰竴涓狹KRoute
		MKRoute route = new MKRoute();
		route.customizeRoute(start, stop, routeData);	
		//灏嗗寘鍚珯鐐逛俊鎭殑MKRoute娣诲姞鍒癛outeOverlay涓�
		RouteOverlay routeOverlay = new RouteOverlay(CustomRouteOverlayDemo.this, mMapView);		
		routeOverlay.setData(route);
		//鍚戝湴鍥炬坊鍔犳瀯閫犲ソ鐨凴outeOverlay
		mMapView.getOverlays().add(routeOverlay);
		//鎵ц鍒锋柊浣跨敓鏁�
	    mMapView.refresh();
		
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
