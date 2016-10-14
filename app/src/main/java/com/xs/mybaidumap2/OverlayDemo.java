package com.xs.mybaidumap2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xs.baidumap.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 演示覆盖物的用法
 */
public class OverlayDemo extends Activity implements OnGetGeoCoderResultListener {

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private InfoWindow mInfoWindow;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        initOverlay();
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                    // 反Geo搜索
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(marker.getPosition()));
                return true;
            }
        });

    }

    public void initOverlay() {


        // add marker overlay
        LatLng llA = new LatLng(22.963175, 114.400244);
        LatLng llB = new LatLng(22.962821, 114.409199);
        LatLng llC = new LatLng(22.969723, 114.405541);
        LatLng llD = new LatLng(22.966965, 114.401394);
        LatLng llE = new LatLng(22.966555, 114.406666);
        List<LatLng> mList = new ArrayList<>();
        mList.add(llA);
        mList.add(llB);
        mList.add(llC);
/*        mList.add(llD);
        mList.add(llE);*/

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latlng :
                mList) {
            MarkerOptions markerOption = new MarkerOptions().position(latlng).icon(bdA).draggable(true);
            markerOption.animateType(MarkerAnimateType.grow);
            mBaiduMap.addOverlay(markerOption);
            builder.include(latlng);
        }

      /*  MarkerOptions mylocat = new MarkerOptions().position(MyBaiduMap2Application.getmCurrentLatLng()).icon(bdD).draggable(true);
        // 生长动画
        mylocat.animateType(MarkerAnimateType.drop);
        mBaiduMap.addOverlay(mylocat);*/

        LatLngBounds bounds = builder.build();

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initOverlay();
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
        // 回收 bitmap 资源
        bdA.recycle();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        TextView textView = new TextView(getApplicationContext());
        textView.setText(reverseGeoCodeResult.getAddress());
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(textView), reverseGeoCodeResult.getLocation(), -47, new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                mBaiduMap.hideInfoWindow();
            }
        });
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

}
