package baidumapsdk.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
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

public class RoutePlanDemo extends Activity {

	//UI閻╃鍙�
	Button mBtnDrive = null;	// 妞规崘婧呴幖婊呭偍
	Button mBtnTransit = null;	// 閸忣兛姘﹂幖婊呭偍
	Button mBtnWalk = null;	// 濮濄儴顢戦幖婊呭偍
	Button mBtnCusRoute = null; //閼奉亜鐣炬稊澶庣熅缁撅拷
	Button mBtnCusIcon = null ; //閼奉亜鐣炬稊澶庢崳缂佸牏鍋ｉ崶鐐垼
	
	//濞村繗顫嶇捄顖滃殠閼哄倻鍋ｉ惄绋垮彠
	Button mBtnPre = null;//娑撳﹣绔存稉顏囧Ν閻愶拷
	Button mBtnNext = null;//娑撳绔存稉顏囧Ν閻愶拷
	int nodeIndex = -2;//閼哄倻鍋ｇ槐銏犵穿,娓氭稒绁荤憴鍫ｅΝ閻愯妞傛担璺ㄦ暏
	MKRoute route = null;//娣囨繂鐡ㄦす鎹愭簠/濮濄儴顢戠捄顖滃殠閺佺増宓侀惃鍕綁闁插骏绱濇笟娑欑セ鐟欏牐濡悙瑙勬娴ｈ法鏁�
	TransitOverlay transitOverlay = null;//娣囨繂鐡ㄩ崗顑挎唉鐠侯垳鍤庨崶鎯х湴閺佺増宓侀惃鍕綁闁插骏绱濇笟娑欑セ鐟欏牐濡悙瑙勬娴ｈ法鏁�
	RouteOverlay routeOverlay = null; 
	boolean useDefaultIcon = false;
	int searchType = -1;//鐠佹澘缍嶉幖婊呭偍閻ㄥ嫮琚崹瀣剁礉閸栧搫鍨庢す鎹愭簠/濮濄儴顢戦崪灞藉彆娴滐拷
	private PopupOverlay   pop  = null;//瀵懓鍤▔鈩冨満閸ユ儳鐪伴敍灞剧セ鐟欏牐濡悙瑙勬娴ｈ法鏁�
	private TextView  popupText = null;//濞夆剝鍦簐iew
	private View viewCache = null;
	
	//閸︽澘娴橀惄绋垮彠閿涘奔濞囬悽銊ф埛閹电笀apView閻ㄥ嚜yRouteMapView閻╊喚娈戦弰顖炲櫢閸愭獩ouch娴滃娆㈢�圭偟骞囧▔鈩冨満婢跺嫮鎮�
	//婵″倹鐏夋稉宥咁槱閻炲敆ouch娴滃娆㈤敍灞藉灟閺冪娀娓剁紒褎澹欓敍宀�娲块幒銉ゅ▏閻⑩垥apView閸楀啿褰�
	MapView mMapView = null;	// 閸︽澘娴榁iew
	//閹兼粎鍌ㄩ惄绋垮彠
	MKSearch mSearch = null;	// 閹兼粎鍌ㄥΟ鈥虫健閿涘奔绡冮崣顖氬箵閹哄婀撮崶鐐侀崸妤冨缁斿濞囬悽锟�
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }
		setContentView(R.layout.routeplan);
		CharSequence titleLable="鐠侯垳鍤庣憴鍕灊閸旂喕鍏�";
        setTitle(titleLable);
		//閸掓繂顫愰崠鏍ф勾閸ワ拷
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(false);
        mMapView.getController().setZoom(12);
        mMapView.getController().enableClick(true);

        //閸掓繂顫愰崠鏍ㄥ瘻闁匡拷
        mBtnDrive = (Button)findViewById(R.id.drive);
        mBtnTransit = (Button)findViewById(R.id.transit);
        mBtnWalk = (Button)findViewById(R.id.walk);
        mBtnPre = (Button)findViewById(R.id.pre);
        mBtnNext = (Button)findViewById(R.id.next);
        mBtnCusRoute = (Button)findViewById(R.id.custombutton);
        mBtnCusIcon = (Button)findViewById(R.id.customicon);
        mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
	    
        //閹稿鏁悙鐟板毊娴滃娆�
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				//閸欐垼鎹ｉ幖婊呭偍
				SearchButtonProcess(v);
			}
        };
        OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				//濞村繗顫嶇捄顖滃殠閼哄倻鍋�
				nodeClick(v);
			}
        };
        OnClickListener customClickListener = new OnClickListener(){
			public void onClick(View v) {
				//閼奉亣顔曠捄顖滃殠缂佹ê鍩楃粈杞扮伐
				intentToActivity();
				
			}
        };
        
        OnClickListener changeRouteIconListener = new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				changeRouteIcon();
			}
        	
        };
        
        mBtnDrive.setOnClickListener(clickListener); 
        mBtnTransit.setOnClickListener(clickListener); 
        mBtnWalk.setOnClickListener(clickListener);
        mBtnPre.setOnClickListener(nodeClickListener);
        mBtnNext.setOnClickListener(nodeClickListener);
        mBtnCusRoute.setOnClickListener(customClickListener);
        mBtnCusIcon.setOnClickListener(changeRouteIconListener);
        //閸掓稑缂� 瀵懓鍤▔鈩冨満閸ユ儳鐪�
        createPaopao();
       
        //閸︽澘娴橀悙鐟板毊娴滃娆㈡径鍕倞
        mMapView.regMapTouchListner(new MKMapTouchListener(){

			@Override
			public void onMapClick(GeoPoint point) {
			  //閸︺劍顒濇径鍕倞閸︽澘娴橀悙鐟板毊娴滃娆� 
			  //濞戝牓娈op
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
        // 閸掓繂顫愰崠鏍ㄦ偝缁便垺膩閸ф绱濆▔銊ュ斀娴滃娆㈤惄鎴濇儔
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				//鐠ч鍋ｉ幋鏍矒閻愯婀佸褌绠熼敍宀勬付鐟曚線锟藉瀚ㄩ崗铚傜秼閻ㄥ嫬鐓勭敮鍌氬灙鐞涖劍鍨ㄩ崷鏉挎絻閸掓銆�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//闁秴宸婚幍锟介張澶婃勾閸э拷
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				// 闁挎瑨顕ら崣宄板讲閸欏倽锟藉儕KEvent娑擃厾娈戠�规矮绠�
				if (error != 0 || res == null) {
					Toast.makeText(RoutePlanDemo.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_SHORT).show();
					return;
				}
			
				searchType = 0;
			    routeOverlay = new RouteOverlay(RoutePlanDemo.this, mMapView);
			    // 濮濄倕顦╂禒鍛潔缁�杞扮娑擃亝鏌熷鍫滅稊娑撹櫣銇氭笟锟�
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    //濞撳懘娅庨崗鏈电铂閸ユ儳鐪�
			    mMapView.getOverlays().clear();
			    //濞ｈ濮炵捄顖滃殠閸ユ儳鐪�
			    mMapView.getOverlays().add(routeOverlay);
			    //閹笛嗩攽閸掗攱鏌婃担璺ㄦ晸閺侊拷
			    mMapView.refresh();
			    // 娴ｈ法鏁oomToSpan()缂佽姤鏂侀崷鏉挎禈閿涘奔濞囩捄顖滃殠閼宠棄鐣崗銊︽▔缁�鍝勬躬閸︽澘娴樻稉锟�
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			    //缁夎濮╅崷鏉挎禈閸掓媽鎹ｉ悙锟�
			    mMapView.getController().animateTo(res.getStart().pt);
			    //鐏忓棜鐭剧痪鎸庢殶閹诡喕绻氱�涙绮伴崗銊ョ湰閸欐﹢鍣�
			    route = res.getPlan(0).getRoute(0);
			    //闁插秶鐤嗙捄顖滃殠閼哄倻鍋ｇ槐銏犵穿閿涘矁濡悙瑙勭セ鐟欏牊妞傛担璺ㄦ暏
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				//鐠ч鍋ｉ幋鏍矒閻愯婀佸褌绠熼敍宀勬付鐟曚線锟藉瀚ㄩ崗铚傜秼閻ㄥ嫬鐓勭敮鍌氬灙鐞涖劍鍨ㄩ崷鏉挎絻閸掓銆�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//闁秴宸婚幍锟介張澶婃勾閸э拷
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(RoutePlanDemo.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_SHORT).show();
					return;
				}
				
				searchType = 1;
				transitOverlay = new TransitOverlay (RoutePlanDemo.this, mMapView);
			    // 濮濄倕顦╂禒鍛潔缁�杞扮娑擃亝鏌熷鍫滅稊娑撹櫣銇氭笟锟�
			    transitOverlay.setData(res.getPlan(0));
			  //濞撳懘娅庨崗鏈电铂閸ユ儳鐪�
			    mMapView.getOverlays().clear();
			  //濞ｈ濮炵捄顖滃殠閸ユ儳鐪�
			    mMapView.getOverlays().add(transitOverlay);
			  //閹笛嗩攽閸掗攱鏌婃担璺ㄦ晸閺侊拷
			    mMapView.refresh();
			    // 娴ｈ法鏁oomToSpan()缂佽姤鏂侀崷鏉挎禈閿涘奔濞囩捄顖滃殠閼宠棄鐣崗銊︽▔缁�鍝勬躬閸︽澘娴樻稉锟�
			    mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
			  //缁夎濮╅崷鏉挎禈閸掓媽鎹ｉ悙锟�
			    mMapView.getController().animateTo(res.getStart().pt);
			  //闁插秶鐤嗙捄顖滃殠閼哄倻鍋ｇ槐銏犵穿閿涘矁濡悙瑙勭セ鐟欏牊妞傛担璺ㄦ暏
			    nodeIndex = 0;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				//鐠ч鍋ｉ幋鏍矒閻愯婀佸褌绠熼敍宀勬付鐟曚線锟藉瀚ㄩ崗铚傜秼閻ㄥ嫬鐓勭敮鍌氬灙鐞涖劍鍨ㄩ崷鏉挎絻閸掓銆�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					//闁秴宸婚幍锟介張澶婃勾閸э拷
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Toast.makeText(RoutePlanDemo.this, "閹惰鲸鐡戦敍灞炬弓閹垫儳鍩岀紒鎾寸亯", Toast.LENGTH_SHORT).show();
					return;
				}

				searchType = 2;
				routeOverlay = new RouteOverlay(RoutePlanDemo.this, mMapView);
			    // 濮濄倕顦╂禒鍛潔缁�杞扮娑擃亝鏌熷鍫滅稊娑撹櫣銇氭笟锟�
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				//濞撳懘娅庨崗鏈电铂閸ユ儳鐪�
			    mMapView.getOverlays().clear();
			  //濞ｈ濮炵捄顖滃殠閸ユ儳鐪�
			    mMapView.getOverlays().add(routeOverlay);
			  //閹笛嗩攽閸掗攱鏌婃担璺ㄦ晸閺侊拷
			    mMapView.refresh();
			    // 娴ｈ法鏁oomToSpan()缂佽姤鏂侀崷鏉挎禈閿涘奔濞囩捄顖滃殠閼宠棄鐣崗銊︽▔缁�鍝勬躬閸︽澘娴樻稉锟�
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			  //缁夎濮╅崷鏉挎禈閸掓媽鎹ｉ悙锟�
			    mMapView.getController().animateTo(res.getStart().pt);
			    //鐏忓棜鐭剧痪鎸庢殶閹诡喕绻氱�涙绮伴崗銊ョ湰閸欐﹢鍣�
			    route = res.getPlan(0).getRoute(0);
			    //闁插秶鐤嗙捄顖滃殠閼哄倻鍋ｇ槐銏犵穿閿涘矁濡悙瑙勭セ鐟欏牊妞傛担璺ㄦ暏
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
			    
			}
			public void onGetAddrResult(MKAddrInfo res, int error) {
			}
			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetPoiDetailSearchResult(int type, int iError) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
				
			}
        });
	}
	/**
	 * 閸欐垼鎹ｇ捄顖滃殠鐟欏嫬鍨濋幖婊呭偍缁�杞扮伐
	 * @param v
	 */
	void SearchButtonProcess(View v) {
		//闁插秶鐤嗗ù蹇氼潔閼哄倻鍋ｉ惃鍕熅缁炬寧鏆熼幑锟�
		route = null;
		routeOverlay = null;
		transitOverlay = null; 
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 婢跺嫮鎮婇幖婊呭偍閹稿鎸抽崫宥呯安
		EditText editSt = (EditText)findViewById(R.id.start);
		EditText editEn = (EditText)findViewById(R.id.end);
		
		// 鐎电鎹ｉ悙鍦矒閻愬湱娈憂ame鏉╂稖顢戠挧瀣拷纭风礉娑旂喎褰叉禒銉ф纯閹恒儱顕崸鎰垼鐠у锟界》绱濈挧瀣拷鐓庢綏閺嶅洤鍨亸鍡樼壌閹诡喖娼楅弽鍥箻鐞涘本鎮崇槐锟�
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = editSt.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = editEn.getText().toString();

		// 鐎圭偤妾担璺ㄦ暏娑擃叀顕�电鎹ｉ悙鍦矒閻愮懓鐓勭敮鍌濈箻鐞涘本顒滅涵顔炬畱鐠佹儳鐣�
		if (mBtnDrive.equals(v)) {
			mSearch.drivingSearch("閸栨ぞ鍚�", stNode, "閸栨ぞ鍚�", enNode);
		} else if (mBtnTransit.equals(v)) {
			mSearch.transitSearch("閸栨ぞ鍚�", stNode, enNode);
		} else if (mBtnWalk.equals(v)) {
			mSearch.walkingSearch("閸栨ぞ鍚�", stNode, "閸栨ぞ鍚�", enNode);
		} 
	}
	/**
	 * 閼哄倻鍋ｅù蹇氼潔缁�杞扮伐
	 * @param v
	 */
	public void nodeClick(View v){
		viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
		if (searchType == 0 || searchType == 2){
			//妞规崘婧呴妴浣诡劄鐞涘奔濞囬悽銊ф畱閺佺増宓佺紒鎾寸�惄绋挎倱閿涘苯娲滃銈囪閸ㄥ璐熸す鎹愭簠閹存牗顒炵悰宀嬬礉閼哄倻鍋ｅù蹇氼潔閺傝纭堕惄绋挎倱
			if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
				return;
			
			//娑撳﹣绔存稉顏囧Ν閻愶拷
			if (mBtnPre.equals(v) && nodeIndex > 0){
				//缁便垹绱╅崙锟�
				nodeIndex--;
				//缁夎濮╅崚鐗堝瘹鐎规氨鍌ㄥ鏇犳畱閸ф劖鐖�
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				//瀵懓鍤▔鈩冨満
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
			//娑撳绔存稉顏囧Ν閻愶拷
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
				//缁便垹绱╅崝锟�
				nodeIndex++;
				//缁夎濮╅崚鐗堝瘹鐎规氨鍌ㄥ鏇犳畱閸ф劖鐖�
				mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
				//瀵懓鍤▔鈩冨満
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						route.getStep(nodeIndex).getPoint(),
						5);
			}
		}
		if (searchType == 1){
			//閸忣兛姘﹂幑顫娴ｈ法鏁ら惃鍕殶閹诡喚绮ㄩ弸鍕瑢閸忔湹绮稉宥呮倱閿涘苯娲滃銈呭礋閻欘剙顦╅悶鍡氬Ν閻愯绁荤憴锟�
			if (nodeIndex < -1 || transitOverlay == null || nodeIndex >= transitOverlay.getAllItem().size())
				return;
			
			//娑撳﹣绔存稉顏囧Ν閻愶拷
			if (mBtnPre.equals(v) && nodeIndex > 1){
				//缁便垹绱╅崙锟�
				nodeIndex--;
				//缁夎濮╅崚鐗堝瘹鐎规氨鍌ㄥ鏇犳畱閸ф劖鐖�
				mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
				//瀵懓鍤▔鈩冨満
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(),
						5);
			}
			//娑撳绔存稉顏囧Ν閻愶拷
			if (mBtnNext.equals(v) && nodeIndex < (transitOverlay.getAllItem().size()-2)){
				//缁便垹绱╅崝锟�
				nodeIndex++;
				//缁夎濮╅崚鐗堝瘹鐎规氨鍌ㄥ鏇犳畱閸ф劖鐖�
				mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
				//瀵懓鍤▔鈩冨満
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(),
						5);
			}
		}
		
	}
	/**
	 * 閸掓稑缂撳鐟板毉濞夆剝鍦洪崶鎯х湴
	 */
	public void createPaopao(){
		
        //濞夆剝鍦洪悙鐟板毊閸濆秴绨查崶鐐剁殶
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
	}
	/**
	 * 鐠哄疇娴嗛懛顏囶啎鐠侯垳鍤嶢ctivity
	 */
	public void intentToActivity(){
		//鐠哄疇娴嗛崚鎷屽殰鐠佹崘鐭剧痪鎸庣川缁�绡竐mo
		Intent intent = new Intent(this, CustomRouteOverlayDemo.class);
    	startActivity(intent); 
	}
	
	/**
	 * 閸掑洦宕茬捄顖滃殠閸ョ偓鐖ｉ敍灞藉煕閺傛澘婀撮崶鍙ュ▏閸忓墎鏁撻弫锟�
	 * 濞夈劍鍓伴敍锟� 鐠ч绮撻悙鐟版禈閺嶅洣濞囬悽銊よ厬韫囧啫顕锟�.
	 */
	protected void changeRouteIcon() {
	    Button btn = (Button)findViewById(R.id.customicon);
	    if ( routeOverlay == null && transitOverlay == null){
	    	return ;
	    }
		if ( useDefaultIcon ){
	    	if ( routeOverlay != null){
	    	    routeOverlay.setStMarker(null);
	    	    routeOverlay.setEnMarker(null);
	        }
	        if ( transitOverlay != null){
	    	    transitOverlay.setStMarker(null);
	    	    transitOverlay.setEnMarker(null);
	        }
	        btn.setText("閼奉亜鐣炬稊澶庢崳缂佸牏鍋ｉ崶鐐垼");
	        Toast.makeText(this, 
	        		       "鐏忓棔濞囬悽銊ч兇缂佺喕鎹ｇ紒鍫㈠仯閸ョ偓鐖�", 
	        		       Toast.LENGTH_SHORT).show();
	    }
	    else{
		    if ( routeOverlay != null){
	    	    routeOverlay.setStMarker(getResources().getDrawable(R.drawable.icon_st));
	    	    routeOverlay.setEnMarker(getResources().getDrawable(R.drawable.icon_en));
	        }
	        if ( transitOverlay != null){
	    	    transitOverlay.setStMarker(getResources().getDrawable(R.drawable.icon_st));
	    	    transitOverlay.setEnMarker(getResources().getDrawable(R.drawable.icon_en));
	        }
	        btn.setText("缁崵绮虹挧椋庣矒閻愮懓娴橀弽锟�");
	        Toast.makeText(this, 
	        		       "鐏忓棔濞囬悽銊ㄥ殰鐎规矮绠熺挧椋庣矒閻愮懓娴橀弽锟�", 
	        		       Toast.LENGTH_SHORT).show();
	    }
	    useDefaultIcon = !useDefaultIcon;
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
