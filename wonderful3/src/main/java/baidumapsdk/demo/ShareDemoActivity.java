package baidumapsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;


public class ShareDemoActivity extends Activity {
	
	private MapView mMapView = null;
	private MKSearch mSearch = null;   // 閹兼粎鍌ㄥΟ鈥虫健閿涘奔绡冮崣顖氬箵閹哄婀撮崶鐐侀崸妤冨缁斿濞囬悽锟�
	//娣囨繂鐡ㄩ幖婊呭偍缂佹挻鐏夐崷鏉挎絻
	private String currentAddr = null;
	//閹兼粎鍌ㄩ崺搴＄ 
	private String mCity = "閸栨ぞ鍚�";
	//閹兼粎鍌ㄩ崗鎶芥暛鐎涳拷
	private String searchKey = "妞佹劙顩�";
	//閸欏秴婀撮悶鍡欑椽鐠囨垹鍋ｉ崸鎰垼
	private GeoPoint mPoint = new GeoPoint((int)(40.056878*1E6),(int)(116.308141*1E6));
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 
         MyApplication app = (MyApplication)this.getApplication();
         if (app.mBMapManager == null) {
             app.mBMapManager = new BMapManager(getApplicationContext());
            
             app.mBMapManager.init(new MyApplication.MyGeneralListener());
         }
        setContentView(R.layout.activity_share_demo_activity);
        mMapView = (MapView)findViewById(R.id.bmapView);
		mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
		// 閸掓繂顫愰崠鏍ㄦ偝缁便垺膩閸ф绱濆▔銊ュ斀閹兼粎鍌ㄦ禍瀣╂閻╂垵鎯�
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){
        	
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            
            public void onGetPoiResult(MKPoiResult res, int type, int error) {
                // 闁挎瑨顕ら崣宄板讲閸欏倽锟藉儕KEvent娑擃厾娈戠�规矮绠�
                if (error != 0 || res == null) {
                    Toast.makeText(ShareDemoActivity.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_LONG).show();
                    return;
                }
                // 鐏忓棗婀撮崶鍓╅崝銊ュ煂缁楊兛绔存稉鐙縊I娑擃厼绺鹃悙锟�
                if (res.getCurrentNumPois() > 0) {
                    // 鐏忓敀oi缂佹挻鐏夐弰鍓с仛閸掓澘婀撮崶鍙ョ瑐
                    PoiShareOverlay poiOverlay = new PoiShareOverlay(ShareDemoActivity.this, mMapView);
                    poiOverlay.setData(res.getAllPoi());
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(poiOverlay);
                    mMapView.refresh();
                    //瑜版徍PoiType娑擄拷2閿涘牆鍙曟禍銈囧殠鐠侯垽绱氶幋锟�4閿涘牆婀撮柧浣哄殠鐠侯垽绱氶弮璁圭礉 poi閸ф劖鐖ｆ稉铏光敄
                    for( MKPoiInfo info : res.getAllPoi() ){
                    	if ( info.pt != null ){
                    		mMapView.getController().animateTo(info.pt);
                    		break;
                    	}
                    }
                } 
            }
            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                    int error) {
            }
            public void onGetTransitRouteResult(MKTransitRouteResult res,
                    int error) {
            }
            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                    int error) {
            }
           
            public void onGetAddrResult(MKAddrInfo res, int error) {
            	// 闁挎瑨顕ら崣宄板讲閸欏倽锟藉儕KEvent娑擃厾娈戠�规矮绠�
                if (error != 0 || res == null) {
                    Toast.makeText(ShareDemoActivity.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_LONG).show();
                    return;
                }
                AddrShareOverlay addrOverlay = new AddrShareOverlay(
                		getResources().getDrawable(R.drawable.icon_marka),mMapView , res);
                mMapView.getOverlays().clear();
                mMapView.getOverlays().add(addrOverlay);
                mMapView.refresh();
                
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }
            
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }
			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				//閸掑棔闊╅惌顓濊缂佹挻鐏�
				Intent it = new Intent(Intent.ACTION_SEND);  
				it.putExtra(Intent.EXTRA_TEXT, "閹劎娈戦張瀣几闁俺绻冮惂鎯у閸︽澘娴楽DK娑撳孩鍋嶉崚鍡曢煩娑擄拷娑擃亙缍呯純锟�: "+
						       currentAddr+
						       " -- "+result.url);  
				it.setType("text/plain");  
				startActivity(Intent.createChooser(it, "鐏忓棛鐓稉鎻掑瀻娴滎偄鍩�"));
				
			}
        });
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
    protected void onDestroy(){
    	mMapView.destroy();
    	mSearch.destory();
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
    
    private void initMapView() {
        mMapView.setLongClickable(true);
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
    }
   
	public void sharePoi(View view){
		//閸欐垼鎹oi閹兼粎鍌�
    	mSearch.poiSearchInCity(mCity, searchKey);
    	Toast.makeText(this,
    			"閸︼拷"+mCity+"閹兼粎鍌� "+searchKey,
    			Toast.LENGTH_SHORT).show();
    }
    
    public void shareAddr(View view){
    	//閸欐垼鎹ｉ崣宥呮勾閻炲棛绱惍浣筋嚞濮癸拷
    	mSearch.reverseGeocode(mPoint);
    	Toast.makeText(this,
    			String.format("閹兼粎鍌ㄦ担宥囩枂閿涳拷 %f閿涳拷%f", (mPoint.getLatitudeE6()*1E-6),(mPoint.getLongitudeE6()*1E-6)),
    			Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 娴ｈ法鏁oiOverlay 鐏炴洜銇歱oi閻愮櫢绱濋崷鈺琽i鐞氼偆鍋ｉ崙缁樻閸欐垼鎹ｉ惌顓濊鐠囬攱鐪�.
     *
     */
    private class PoiShareOverlay extends PoiOverlay {

        public PoiShareOverlay(Activity activity, MapView mapView) {
            super(activity, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            MKPoiInfo info = getPoi(i);
            currentAddr = info.address;  	
            mSearch.poiDetailShareURLSearch(info.uid);
            return true;
        }        
    }
    /**
     * 娴ｈ法鏁temizevOvelray鐏炴洜銇氶崣宥呮勾閻炲棛绱惍浣哄仯娴ｅ秶鐤嗛敍灞界秼鐠囥儳鍋ｇ悮顐ゅ仯閸戠粯妞傞崣鎴ｆ崳閻厺瑕嗙拠閿嬬湴.
     *
     */
  private class AddrShareOverlay extends ItemizedOverlay {

	  private MKAddrInfo addrInfo ;
	  public AddrShareOverlay(Drawable defaultMarker, MapView mapView , MKAddrInfo addrInfo) {
		super(defaultMarker, mapView);
		this.addrInfo = addrInfo;
		addItem(new OverlayItem(addrInfo.geoPt,addrInfo.strAddr,addrInfo.strAddr));
	}
	  
	@Override
	public boolean onTap(int index){
		currentAddr = addrInfo.strAddr;
	    mSearch.poiRGCShareURLSearch(addrInfo.geoPt, "閸掑棔闊╅崷鏉挎絻", addrInfo.strAddr);	
	    return true;
	}
	  
  }
}
