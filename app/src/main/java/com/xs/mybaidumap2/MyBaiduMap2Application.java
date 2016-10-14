package com.xs.mybaidumap2;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;

/**
 * @author: Xs
 * @date: 2016-10-13 15:59
 * @describe <>
 */
public class MyBaiduMap2Application extends Application{


    // 定位
    public LocationClient mLocationClient;
    private MyLocationListener mMyLocListener;
    public BMapManager mBMapManager;
    private static LatLng mCurrentLatLng;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        initEngineManager(this);
        initLocation();
    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
        if (!mBMapManager.init(new BaiduMapGeneralListener())) {
            Toast.makeText(context, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class BaiduMapGeneralListener implements MKGeneralListener {
        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            Log.e("", "onGetPermissionState  key=" + iError);

        }
    }

    public void initLocation() {
        mMyLocListener = new MyLocationListener();
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mMyLocListener);
        LocationClientOption mLocClientOption = new LocationClientOption();
        mLocClientOption.setOpenGps(true);
        mLocClientOption.setCoorType("bd09ll");
        mLocClientOption.setScanSpan(1000);
        mLocationClient.setLocOption(mLocClientOption);
        mLocationClient.start();
    }

    public static LatLng getmCurrentLatLng() {
        return mCurrentLatLng;
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocationClient.stop();
            Log.e("info", "onReceiveLocation: "+bdLocation.getLongitude()+" "+bdLocation.getLatitude() );
            mCurrentLatLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        }
    }
}
