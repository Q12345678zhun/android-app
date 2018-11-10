package baidumapsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;


public class PoiSearchDemo extends Activity {
	
	private MapView mMapView = null;
	private MKSearch mSearch = null;   // 閹兼粎鍌ㄥΟ鈥虫健閿涘奔绡冮崣顖氬箵閹哄婀撮崶鐐侀崸妤冨缁斿濞囬悽锟�
	
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
    private int load_Index;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 
         MyApplication app = (MyApplication)this.getApplication();
         if (app.mBMapManager == null) {
             app.mBMapManager = new BMapManager(getApplicationContext());
            
             app.mBMapManager.init(new MyApplication.MyGeneralListener());
         }
         setContentView(R.layout.activity_poisearch);
        mMapView = (MapView)findViewById(R.id.bmapView);
		mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
		
		
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){
            
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
                if (error != 0) {
                    Toast.makeText(PoiSearchDemo.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PoiSearchDemo.this, "閹存劕濮涢敍灞剧叀閻顕涢幆鍛淬�夐棃锟�", Toast.LENGTH_SHORT).show();
                }
            }
            /**
             * 閸︺劍顒濇径鍕倞poi閹兼粎鍌ㄧ紒鎾寸亯
             */
            public void onGetPoiResult(MKPoiResult res, int type, int error) {
                // 闁挎瑨顕ら崣宄板讲閸欏倽锟藉儕KEvent娑擃厾娈戠�规矮绠�
                if (error != 0 || res == null) {
                    Toast.makeText(PoiSearchDemo.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_LONG).show();
                    return;
                }
                // 鐏忓棗婀撮崶鍓╅崝銊ュ煂缁楊兛绔存稉鐙縊I娑擃厼绺鹃悙锟�
                if (res.getCurrentNumPois() > 0) {
                    // 鐏忓敀oi缂佹挻鐏夐弰鍓с仛閸掓澘婀撮崶鍙ョ瑐
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(PoiSearchDemo.this, mMapView, mSearch);
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
                } else if (res.getCityListNum() > 0) {
                	//瑜版捁绶崗銉ュ彠闁款喖鐡ч崷銊︽拱鐢倹鐥呴張澶嬪閸掑府绱濇担鍡楁躬閸忔湹绮崺搴＄閹垫儳鍩岄弮璁圭礉鏉╂柨娲栭崠鍛儓鐠囥儱鍙ч柨顔肩摟娣団剝浼呴惃鍕厔鐢倸鍨悰锟�
                    String strInfo = "閸︼拷";
                    for (int i = 0; i < res.getCityListNum(); i++) {
                        strInfo += res.getCityListInfo(i).city;
                        strInfo += ",";
                    }
                    strInfo += "閹垫儳鍩岀紒鎾寸亯";
                    Toast.makeText(PoiSearchDemo.this, strInfo, Toast.LENGTH_LONG).show();
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
            }
            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }
            /**
             * 閺囧瓨鏌婂楦款唴閸掓銆�
             */
            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            	if ( res == null || res.getAllSuggestions() == null){
            		return ;
            	}
            	sugAdapter.clear();
            	for ( MKSuggestionInfo info : res.getAllSuggestions()){
            		if ( info.key != null)
            		    sugAdapter.add(info.key);
            	}
            	sugAdapter.notifyDataSetChanged();
                
            }
			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
				
			}
        });
        
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        
        /**
         * 瑜版捁绶崗銉ュ彠闁款喖鐡ч崣妯哄閺冭绱濋崝銊︼拷浣规纯閺傛澘缂撶拋顔煎灙鐞涳拷
         */
        keyWorldsView.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 String city =  ((EditText)findViewById(R.id.city)).getText().toString();
				 /**
				  * 娴ｈ法鏁ゅ楦款唴閹兼粎鍌ㄩ張宥呭閼惧嘲褰囧楦款唴閸掓銆冮敍宀�绮ㄩ弸婊冩躬onSuggestionResult()娑擃厽娲块弬锟�
				  */
                 mSearch.suggestionSearch(cs.toString(), city);				
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
    /**
     * 瑜板崬鎼烽幖婊呭偍閹稿鎸抽悙鐟板毊娴滃娆�
     * @param v
     */
    public void searchButtonProcess(View v) {
          EditText editCity = (EditText)findViewById(R.id.city);
          EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
          mSearch.poiSearchInCity(editCity.getText().toString(), 
                  editSearchKey.getText().toString());
    }
   public void goToNextPage(View v) {
        //閹兼粎鍌ㄦ稉瀣╃缂佸埦oi
        int flag = mSearch.goToPoiPage(++load_Index);
        if (flag != 0) {
            Toast.makeText(PoiSearchDemo.this, "閸忓牊鎮崇槐銏犵磻婵绱濋悞璺烘倵閸愬秵鎮崇槐顫瑓娑擄拷缂佸嫭鏆熼幑锟�", Toast.LENGTH_SHORT).show();
        }
    }
}
