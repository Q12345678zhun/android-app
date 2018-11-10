package com.xgr.wonderful.ui;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import cn.bmob.v3.Bmob;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.xgr.wonderful.R;
import com.xgr.wonderful.ui.base.BaseActivity;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;
import com.xgr.wonderful.utils.UmengStat;

import android.os.Bundle;
import android.os.Handler;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-21
 * TODO 闂睆鐣岄潰锛屾牴鎹寚瀹氭椂闂磋繘琛岃烦杞�
 * 		鍦╝ctivity_splash.xml涓姞鍏ackground灞炴�у苟浼犲叆鍥剧墖璧勬簮ID鍗冲彲
 */
public class SplashActivity extends BaseActivity {

	private static final long DELAY_TIME = 2000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//Bmob SDK鍒濆鍖�--鍙渶瑕佽繖涓�娈典唬鐮佸嵆鍙畬鎴愬垵濮嬪寲
				//璇峰埌Bmob瀹樼綉(http://www.bmob.cn/)鐢宠ApplicationId,鍏蜂綋鍦板潃:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		Bmob.initialize(this, "42092fcf5d2eb9d1b674719d94ec3e71");
		
		LogUtils.i(TAG,TAG + " Launched 锛�");
		MobclickAgent.openActivityDurationTrack(UmengStat.IS_OPEN_ACTIVITY_AUTO_STAT);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		redirectByTime();		
		if(sputil.getValue("isPushOn", true)){
			PushAgent mPushAgent = PushAgent.getInstance(mContext);
			mPushAgent.enable();
			LogUtils.i(TAG,"device_token:"+UmengRegistrar.getRegistrationId(mContext));
		}else{
			PushAgent mPushAgent = PushAgent.getInstance(mContext);
			mPushAgent.disable();
		}
		
		/**AdManager.getInstance(mContext).init("674c5f2b163dacc5", "b435befacb6342702d1f6b7bf05016f9", false);
		OffersManager.getInstance(mContext);**/
	}
	
	/**
	 * 鏍规嵁鏃堕棿杩涜椤甸潰璺宠浆
	 */
	private void redirectByTime() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				redictToActivity(SplashActivity.this, MainActivity.class, null);
				finish();
			}
		}, DELAY_TIME);
	}

}
