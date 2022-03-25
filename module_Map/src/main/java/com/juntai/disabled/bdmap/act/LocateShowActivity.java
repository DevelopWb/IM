package com.juntai.disabled.bdmap.act;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.bdmap.BaseRequestLocationActivity;
import com.juntai.disabled.bdmap.R;
import com.juntai.disabled.bdmap.utils.MapUtil;
import com.juntai.disabled.bdmap.utils.MyOrientationListener;
import com.juntai.disabled.bdmap.utils.NagivationUtils;

/**
 * @aouther tobato
 * @description 描述   位置展示
 * @date 2021-12-06 13:59
 */
public class LocateShowActivity extends BaseRequestLocationActivity implements View.OnClickListener {

    private MapView mBaiduMapView;
    private ImageView mLocateCurrentIv;
    private float direct = 0, locationRadius = 0;
    private LatLng mCurrentLocation = null;
    private BitmapDescriptor locationMarker = null;


    /**
     * Large Text
     */
    private TextView mAddrNameTv;
    /**
     * New Text
     */
    private TextView mAddrDesTv;
    private ImageView mNavigateIv;
    private BaiduMap mBaiduMap;
    private MyOrientationListener myOrientationListener;

    public static String LAT_KEY = "lat_key";
    public static String LNG_KEY = "lng_key";
    public static String ADDR_NAME_KEY = "addr_key";
    public static String ADDR_DES_KEY = "addr_des_key";
    private LatLng sharedLatLng;
    private String addrName;


    public static void startLocateActivity(Context mContext, double lat, double lng, String addrName,String addrDes) {
        Intent intent = new Intent(mContext, LocateShowActivity.class);
        intent.putExtra(LAT_KEY, lat);
        intent.putExtra(LNG_KEY, lng);
        intent.putExtra(ADDR_NAME_KEY, addrName);
        intent.putExtra(ADDR_DES_KEY, addrDes);
        mContext.startActivity(intent);
    }


    @Override
    public void onLocationReceived(BDLocation bdLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(direct).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        locationRadius = bdLocation.getRadius();
        mBaiduMap.setMyLocationData(locData);
        mCurrentLocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
    }

    @Override
    public boolean requestLocation() {
        locateOnce = false;
        return true;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_locate_show;
    }

    @Override
    public void onResume() {
        mBaiduMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mBaiduMapView.onPause();
        super.onPause();
    }

    @Override
    public void initView() {
        setTitleName("位置");
        mBaiduMapView = (MapView) findViewById(R.id.baidu_map_v);
        mBaiduMap = mBaiduMapView.getMap();
        mLocateCurrentIv = (ImageView) findViewById(R.id.locate_current_iv);
        mLocateCurrentIv.setOnClickListener(this);
        mAddrNameTv = (TextView) findViewById(R.id.addr_name_tv);
        mAddrDesTv = (TextView) findViewById(R.id.addr_des_tv);
        mNavigateIv = (ImageView) findViewById(R.id.navigate_iv);
        mNavigateIv.setOnClickListener(this);
        initLocateOritation();
        initUiSetting();
        if (getIntent() != null) {
            double lat = getIntent().getDoubleExtra(LAT_KEY, 0.0);
            double lng = getIntent().getDoubleExtra(LNG_KEY, 0.0);
            addrName = getIntent().getStringExtra(ADDR_NAME_KEY);
            String addrDes = getIntent().getStringExtra(ADDR_DES_KEY);
            mAddrNameTv.setText(addrName);
            mAddrDesTv.setText(addrDes);
            sharedLatLng = new LatLng(lat, lng);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(sharedLatLng, 18);    //设置地图中心点以及缩放级别
            mBaiduMap.animateMapStatus(u);
            locationMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.locate_marker);
            MarkerOptions options = new MarkerOptions().position(sharedLatLng).icon(locationMarker);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(options);
        }
    }

    private void initLocateOritation() {
        mCurrentLocation = new LatLng(0.0, 0.0);
        //获取方向
        myOrientationListener = new MyOrientationListener(mContext);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(locationRadius)
                        .direction(direct)// 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(mCurrentLocation.latitude)
                        .longitude(mCurrentLocation.longitude).build();
                mBaiduMap.setMyLocationData(locData);
                direct = x;
            }
        });
        myOrientationListener.start();
    }

    private void initUiSetting() {
        //实例化UiSettings类对象
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        //通过设置enable为true或false 选择是否显示指南针
        mUiSettings.setCompassEnabled(false);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(config);
        mBaiduMapView.showZoomControls(false);

    }

    @Override
    public void initData() {

    }


    @Override
    protected String getUpdateHttpUrl() {
        return null;
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.locate_current_iv) {
            //定位到当前位置
            MapUtil.mapMoveTo(mBaiduMap, mCurrentLocation);

        } else if (id == R.id.navigate_iv) {
            //开始导航
            NagivationUtils.navigationLogic(mContext, sharedLatLng, addrName);
        }
    }


    @Override
    protected String getDownloadTitleRightName() {
        return null;
    }

    @Override
    protected String getDownLoadPath() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMapView.onDestroy();
        mBaiduMapView = null;
        myOrientationListener.stop();
        locationMarker.recycle();
        locationMarker = null;
    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return true;
    }
}
