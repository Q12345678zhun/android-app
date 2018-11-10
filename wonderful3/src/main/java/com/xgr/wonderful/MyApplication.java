package com.xgr.wonderful;

import java.io.File;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;



import cn.bmob.v3.BmobUser;

import com.baidu.ar.bean.DuMixARConfig;
import com.baidu.ar.util.Res;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xgr.wonderful.entity.QiangYu;
import com.xgr.wonderful.entity.User;
import com.xgr.wonderful.utils.ActivityManagerUtils;

import org.litepal.LitePalApplication;

public class MyApplication extends Application{

	public static String TAG;
	
	private static MyApplication myApplication = null;
	
	private QiangYu currentQiangYu = null;
	
	
	 private static MyApplication mInstance = null;
	 public boolean m_bKeyRight = true;
	 public BMapManager mBMapManager = null;
	
	public static MyApplication getInstance(){
		return myApplication;
	}
	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(myApplication, User.class);
		if(user!=null){
			return user;
		}
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TAG = this.getClass().getSimpleName();
		//鐢变簬Application绫绘湰韬凡缁忓崟渚嬶紝鎵�浠ョ洿鎺ユ寜浠ヤ笅澶勭悊鍗冲彲銆�
		myApplication = this;
		initImageLoader();

		Res.addResource(this);
		// 设置App Id
		DuMixARConfig.setAppId("12917");
		// 设置API Key
		DuMixARConfig.setAPIKey("fc6a2e79b170809cd865e7df7b2190fe");
		
	    mInstance = this;
	   initEngineManager(this);
	   LitePalApplication.initialize(this);


		
	}
	public static MyApplication getInstance1() {
		return mInstance;
	}
	
	
	private void initEngineManager(Context context) {
		// TODO Auto-generated method stub
		 if (mBMapManager == null) {
	            mBMapManager = new BMapManager(context);
	        }

	        if (!mBMapManager.init(new MyGeneralListener())) {
	            Toast.makeText(myApplication.getInstance().getApplicationContext(), 
	                    "BMapManager  鍒濆鍖栭敊璇�!", Toast.LENGTH_LONG).show();
	        }
		
	}
	

	
	
	
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(myApplication.getInstance().getApplicationContext(), "鎮ㄧ殑缃戠粶鍑洪敊鍟︼紒",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(myApplication.getInstance().getApplicationContext(), "杈撳叆姝ｇ‘鐨勬绱㈡潯浠讹紒",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

		@Override
		public void onGetPermissionState(int iError) {
			// TODO Auto-generated method stub
			
			 if (iError != 0) {
	                //鎺堟潈Key閿欒锛�
	                Toast.makeText(myApplication.getInstance1().getApplicationContext(), 
	                        "AndroidManifest.xml 鏂囦欢杈撳叆姝ｇ‘鐨勬巿鏉僈ey,骞舵鏌ユ偍鐨勭綉缁滆繛鎺ユ槸鍚︽甯革紒error: "+iError, Toast.LENGTH_LONG).show();
	                MyApplication.getInstance1().m_bKeyRight = false;
	            }
	            else{
	            	myApplication.getInstance1().m_bKeyRight = true;
	            	Toast.makeText(myApplication.getInstance1().getApplicationContext(), 
	                        "key璁よ瘉鎴愬姛", Toast.LENGTH_LONG).show();
	            }
	        }
	    
    }
	
	public QiangYu getCurrentQiangYu() {
		return currentQiangYu;
	}

	public void setCurrentQiangYu(QiangYu currentQiangYu) {
		this.currentQiangYu = currentQiangYu;
	}

	public void addActivity(Activity ac){
		ActivityManagerUtils.getInstance().addActivity(ac);
	}
	
	public void exit(){
		ActivityManagerUtils.getInstance().removeAllActivity();
	}
	
	public Activity getTopActivity(){
		return ActivityManagerUtils.getInstance().getTopActivity();
	}
	
	/**
	 * 鍒濆鍖杋mageLoader
	 */
	public void initImageLoader(){
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
										.memoryCache(new LruMemoryCache(5*1024*1024))
										.memoryCacheSize(10*1024*1024)
										.discCache(new UnlimitedDiscCache(cacheDir))
										.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
										.build();
		ImageLoader.getInstance().init(config);
	}
	
	public DisplayImageOptions getOptions(int drawableId){
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(drawableId)
		.showImageForEmptyUri(drawableId)
		.showImageOnFail(drawableId)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}

	
}

