package com.xgr.wonderful.ui;

import net.youmi.android.offers.OffersManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xgr.wonderful.MyApplication;
import com.xgr.wonderful.R;
import com.xgr.wonderful.utils.ActivityUtil;
import com.xgr.wonderful.utils.Constant;
import com.xgr.wonderful.utils.LogUtils;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

	public static final String TAG = "MainActivity";
	private NaviFragment naviFragment;
	private ImageView leftMenu;
	private ImageView rightMenu;
	private SlidingMenu mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_frame);
		leftMenu = (ImageView) findViewById(R.id.topbar_menu_left);
		rightMenu = (ImageView) findViewById(R.id.topbar_menu_right);
		leftMenu.setOnClickListener(this);
		rightMenu.setOnClickListener(this);
		initFragment();
		// ��ʾ��ʾ�Ի���
		// showDialog();
	}

	Dialog dialog;

	private void showDialog() {
		dialog = new AlertDialog.Builder(this).setTitle("��ʾ")
				.setMessage(R.string.dialog_tips)
				.setPositiveButton("ȷʵ", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();

					}
				}).create();
		dialog.show();
	}

	private void initFragment() {
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_navi); // ��������slidingmenu��fragment�ƶ�layout
		naviFragment = new NaviFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_navi, naviFragment).commit();
		// ����slidingmenu������
		mSlidingMenu.setMode(SlidingMenu.LEFT);// ����slidingmeni���Ĳ����
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// ֻ���ڱ��ϲſ��Դ�
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// ƫ����
		mSlidingMenu.setFadeEnabled(true);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_navi);

		Bundle mBundle = null;
		// �����򿪼����¼�
		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
			}
		});
		// �����رռ����¼�
		mSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.topbar_menu_left:
			mSlidingMenu.toggle();
			break;
		case R.id.topbar_menu_right:
			// ��ǰ�û���¼
			BmobUser currentUser = BmobUser.getCurrentUser(MainActivity.this);
			if (currentUser != null) {
				// �����û�ʹ��Ӧ��,�������û���Ψһ��ʶ����������Ϊ�������ݵ��ֶ�
				String name = currentUser.getUsername();
				String email = currentUser.getEmail();
				LogUtils.i(TAG, "username:" + name + ",email:" + email);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditActivity.class);
				startActivity(intent);
			} else {
				// �����û�����Ϊ��ʱ�� �ɴ��û�ע����桭
				Toast.makeText(MainActivity.this, "���ȵ�¼��", Toast.LENGTH_SHORT)
						.show();
				// redictToActivity(mContext, RegisterAndLoginActivity.class,
				// null);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,
						RegisterAndLoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OffersManager.getInstance(MainActivity.this).onAppExit();
	}

	private static long firstTime;

	/**
	 * ���������η��ؼ����˳�
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (firstTime + 2000 > System.currentTimeMillis()) {
			MyApplication.getInstance().exit();
			super.onBackPressed();
		} else {
			ActivityUtil.show(MainActivity.this, "�ٰ�һ���˳�����");
		}
		firstTime = System.currentTimeMillis();
	}
}