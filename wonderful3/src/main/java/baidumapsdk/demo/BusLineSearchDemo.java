package baidumapsdk.demo;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;

/**
 * 姝emo鐢ㄦ潵灞曠ず濡備綍杩涜鍏氦绾胯矾璇︽儏妫�绱紝骞朵娇鐢≧outeOverlay鍦ㄥ湴鍥句笂缁樺埗
 * 鍚屾椂灞曠ず濡備綍娴忚璺嚎鑺傜偣骞跺脊鍑烘场娉�
 *
 */
public class BusLineSearchDemo extends Activity {
	//UI鐩稿叧
	Button mBtnSearch = null;
	Button mBtnNextLine = null;
	//娴忚璺嚎鑺傜偣鐩稿叧
	Button mBtnPre = null;//涓婁竴涓妭鐐�
	Button mBtnNext = null;//涓嬩竴涓妭鐐�
	int nodeIndex = -2;//鑺傜偣绱㈠紩,渚涙祻瑙堣妭鐐规椂浣跨敤
	MKRoute route = null;//淇濆瓨椹捐溅/姝ヨ璺嚎鏁版嵁鐨勫彉閲忥紝渚涙祻瑙堣妭鐐规椂浣跨敤
	private PopupOverlay   pop  = null;//寮瑰嚭娉℃场鍥惧眰锛屾祻瑙堣妭鐐规椂浣跨敤
	private TextView  popupText = null;//娉℃场view
	private View viewCache = null;
	private List<String> busLineIDList = null;
	int busLineIndex = 0;
	
	//鍦板浘鐩稿叧锛屼娇鐢ㄧ户鎵縈apView鐨凪yBusLineMapView鐩殑鏄噸鍐檛ouch浜嬩欢瀹炵幇娉℃场澶勭悊
	//濡傛灉涓嶅鐞唗ouch浜嬩欢锛屽垯鏃犻渶缁ф壙锛岀洿鎺ヤ娇鐢∕apView鍗冲彲
    MapView mMapView = null;	// 鍦板浘View	
	//鎼滅储鐩稿叧
	MKSearch mSearch = null;	// 鎼滅储妯″潡锛屼篃鍙幓鎺夊湴鍥炬ā鍧楃嫭绔嬩娇鐢�
	
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
		setContentView(R.layout.buslinesearch);
		CharSequence titleLable="鍏氦绾胯矾鏌ヨ鍔熻兘";
        setTitle(titleLable);
        //鍦板浘鍒濆鍖�
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
        busLineIDList = new ArrayList<String>();
      //鍒涘缓 寮瑰嚭娉℃场鍥惧眰
        createPaopao();
        
        //鍦板浘鐐瑰嚮浜嬩欢澶勭悊
        mMapView.regMapTouchListner(new MKMapTouchListener(){

			@Override
			public void onMapClick(GeoPoint point) {
			  //鍦ㄦ澶勭悊鍦板浘鐐瑰嚮浜嬩欢 
			  //娑堥殣pop
			  if ( pop != null ){
				  pop.hidePop();
			  }
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
				
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				
			}
        	
        });
        
        // 鍒濆鍖栨悳绱㈡ā鍧楋紝娉ㄥ唽浜嬩欢鐩戝惉
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){

            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 閿欒鍙峰彲鍙傝�僊KEvent涓殑瀹氫箟
				if (error != 0 || res == null) {
					Toast.makeText(BusLineSearchDemo.this, "鎶辨瓑锛屾湭鎵惧埌缁撴灉", Toast.LENGTH_LONG).show();
					return;
		        }
				
				// 鎵惧埌鍏氦璺嚎poi node
                MKPoiInfo curPoi = null;
                int totalPoiNum  = res.getCurrentNumPois();
                //閬嶅巻鎵�鏈塸oi锛屾壘鍒扮被鍨嬩负鍏氦绾胯矾鐨刾oi
                busLineIDList.clear();
				for( int idx = 0; idx < totalPoiNum; idx++ ) {
                    if ( 2 == res.getPoi(idx).ePoiType ) {
                        // poi绫诲瀷锛�0锛氭櫘閫氱偣锛�1锛氬叕浜ょ珯锛�2锛氬叕浜ょ嚎璺紝3锛氬湴閾佺珯锛�4锛氬湴閾佺嚎璺�
                        curPoi = res.getPoi(idx);
                        //浣跨敤poi鐨剈id鍙戣捣鍏氦璇︽儏妫�绱�
                        busLineIDList.add(curPoi.uid);
                        System.out.println(curPoi.uid);
                        
                    }
				}
				SearchNextBusline();
				
				// 娌℃湁鎵惧埌鍏氦淇℃伅
                if (curPoi == null) {
                    Toast.makeText(BusLineSearchDemo.this, "鎶辨瓑锛屾湭鎵惧埌缁撴灉", Toast.LENGTH_LONG).show();
                    return;
                }
				route = null;
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
			/**
			 * 鑾峰彇鍏氦璺嚎缁撴灉锛屽睍绀哄叕浜ょ嚎璺�
			 */
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				if (iError != 0 || result == null) {
					Toast.makeText(BusLineSearchDemo.this, "鎶辨瓑锛屾湭鎵惧埌缁撴灉", Toast.LENGTH_LONG).show();
					return;
		        }

				RouteOverlay routeOverlay = new RouteOverlay(BusLineSearchDemo.this, mMapView);
			    // 姝ゅ浠呭睍绀轰竴涓柟妗堜綔涓虹ず渚�
			    routeOverlay.setData(result.getBusRoute());
			    //娓呴櫎鍏朵粬鍥惧眰
			    mMapView.getOverlays().clear();
			    //娣诲姞璺嚎鍥惧眰
			    mMapView.getOverlays().add(routeOverlay);
			    //鍒锋柊鍦板浘浣跨敓鏁�
			    mMapView.refresh();
			    //绉诲姩鍦板浘鍒拌捣鐐�
			    mMapView.getController().animateTo(result.getBusRoute().getStart());
			  //灏嗚矾绾挎暟鎹繚瀛樼粰鍏ㄥ眬鍙橀噺
			    route = result.getBusRoute();
			    //閲嶇疆璺嚎鑺傜偣绱㈠紩锛岃妭鐐规祻瑙堟椂浣跨敤
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
				Toast.makeText(BusLineSearchDemo.this, 
						       result.getBusName(), 
						       Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
				
			}

        });
        
        // 璁惧畾鎼滅储鎸夐挳鐨勫搷搴�
        mBtnSearch = (Button)findViewById(R.id.search);
        mBtnNextLine = (Button)findViewById(R.id.nextline);
        mBtnPre = (Button)findViewById(R.id.pre);
        mBtnNext = (Button)findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
        
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				//鍙戣捣鎼滅储
				SearchButtonProcess(v);
			}
        };
        OnClickListener nextLineClickListener = new OnClickListener(){
			public void onClick(View v) {
				//鎼滅储涓嬩竴鏉″叕浜ょ嚎
				SearchNextBusline();
			}
        };
        OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				//娴忚璺嚎鑺傜偣
				nodeClick(v);
			}
        };
        mBtnSearch.setOnClickListener(clickListener); 
        mBtnNextLine.setOnClickListener(nextLineClickListener);
        mBtnPre.setOnClickListener(nodeClickListener);
        mBtnNext.setOnClickListener(nodeClickListener);
	}
	/**
	 * 鍙戣捣妫�绱�
	 * @param v
	 */
	void SearchButtonProcess(View v) {
		busLineIDList.clear();
		busLineIndex = 0;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		if (mBtnSearch.equals(v)) {
			EditText editCity = (EditText)findViewById(R.id.city);
			EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
			//鍙戣捣poi妫�绱紝浠庡緱鍒版墍鏈塸oi涓壘鍒板叕浜ょ嚎璺被鍨嬬殑poi锛屽啀浣跨敤璇oi鐨剈id杩涜鍏氦璇︽儏鎼滅储
			mSearch.poiSearchInCity(editCity.getText().toString(), editSearchKey.getText().toString());
		}
		
	}
	void SearchNextBusline(){
		 if ( busLineIndex >= busLineIDList.size()){
			 busLineIndex =0;
		 }
		 if ( busLineIndex >=0 && busLineIndex < busLineIDList.size() && busLineIDList.size() >0){
			 mSearch.busLineSearch(((EditText)findViewById(R.id.city)).getText().toString(), busLineIDList.get(busLineIndex));
			 busLineIndex ++;
		 }
		 
	}
	/**
	 * 鍒涘缓寮瑰嚭娉℃场鍥惧眰
	 */
	public void createPaopao(){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
        //娉℃场鐐瑰嚮鍝嶅簲鍥炶皟
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
	}
	/**
	 * 鑺傜偣娴忚绀轰緥
	 * @param v
	 */
	public void nodeClick(View v){
	
		if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
			return;
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
		//涓婁竴涓妭鐐�
		if (mBtnPre.equals(v) && nodeIndex > 0){
			//绱㈠紩鍑�
			nodeIndex--;
			//绉诲姩鍒版寚瀹氱储寮曠殑鍧愭爣
			mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
			//寮瑰嚭娉℃场
			popupText.setText(route.getStep(nodeIndex).getContent());
			popupText.setBackgroundResource(R.drawable.popup);
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
					route.getStep(nodeIndex).getPoint(),
					5);	
		}
		//涓嬩竴涓妭鐐�
		if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
			//绱㈠紩鍔�
			nodeIndex++;
			//绉诲姩鍒版寚瀹氱储寮曠殑鍧愭爣
			mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
			//寮瑰嚭娉℃场
			popupText.setText(route.getStep(nodeIndex).getContent());
			popupText.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup));
			pop.showPopup(BMapUtil.getBitmapFromView(popupText),
					route.getStep(nodeIndex).getPoint(),
					5);	
		}
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

}
