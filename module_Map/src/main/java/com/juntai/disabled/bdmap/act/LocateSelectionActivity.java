package com.juntai.disabled.bdmap.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.bdmap.BaseRequestLocationActivity;
import com.juntai.disabled.bdmap.R;
import com.juntai.disabled.bdmap.adapter.PlaceListAdapter;
import com.juntai.disabled.bdmap.bean.AddressBean;
import com.juntai.disabled.bdmap.utils.MapUtil;
import com.juntai.disabled.bdmap.utils.ScreenShotHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  地图中选择地址
 * @date 2020/7/8 10:16
 */
public class LocateSelectionActivity extends BaseRequestLocationActivity implements BaiduMap.OnMapClickListener, BaiduMap.SnapshotReadyCallback,
        OnGetGeoCoderResultListener {

    public static String RIGHT_CONTENT = "right_content";
    public static String LAT = "lat";
    public static String LNG = "lng";
    public static String ADDRNAME = "addrName";
    public static String ADDRDES = "addrDes";
    private String addrName = "";
    private String addrDes = "";
    private RecyclerView mAddressListRv;
    private MapView mapView;
    private Double lat = 0.0, lng = 0.0;
    private BaiduMap mBaiduMap = null;
    private BitmapDescriptor locationMarker = null;
    private ProgressBar progressBar = null;
    private PlaceListAdapter adapter = null;
    private GeoCoder mGeoCoder = null;
    private RelativeLayout mMapViewRl;

    @Override
    public int getLayoutView() {
        return R.layout.activity_location_seltion;
    }


    @Override
    public void initView() {
        setTitleName("地理位置");
        if (getIntent() != null) {
            String rightContent = getIntent().getStringExtra(RIGHT_CONTENT);
            getTitleRightTv().setText(TextUtils.isEmpty(rightContent) ? "确定" : rightContent);
        }
        getTitleRightTv().setVisibility(View.VISIBLE);

        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (lat != 0.0 && lng != 0.0 ) {
                    if ("发送".equals(getTextViewValue(getTitleRightTv()))) {
                        progressBar.setVisibility(View.VISIBLE);
                        //发送位置的时候   地图截屏
                        mBaiduMap.snapshot(LocateSelectionActivity.this);
                    }else {
                        intent.putExtra(LAT, lat);
                        intent.putExtra(LNG, lng);
                        intent.putExtra(ADDRNAME, addrName);
                        intent.putExtra(ADDRDES, addrDes);
                        setResult(BASE_REQUEST_RESULT, intent);
                        finish();
                    }

                } else {
                    ToastUtils.toast(mContext, "未选择位置或位置错误");
                }



            }
        });

        mapView = findViewById(R.id.mMapViewRy);
        mMapViewRl = findViewById(R.id.mapview_rl);
        mBaiduMap = mapView.getMap();
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        mapView.removeViewAt(1);
        locationMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.locate_marker);
        mAddressListRv = findViewById(R.id.address_list_rv);
        adapter = new PlaceListAdapter(R.layout.item_immap_list);
        adapter.openLoadAnimation();
        initRecyclerview(mAddressListRv, adapter, LinearLayoutManager.VERTICAL);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                List<AddressBean> addressBeanList = (List<AddressBean>) adapter.getData();
                for (int i = 0; i < addressBeanList.size(); i++) {
                    AddressBean addrBean = addressBeanList.get(i);
                    if (i == position) {
                        addrBean.setIschecked(true);
                        lat = addrBean.getPoiInfo().location.latitude;
                        lng = addrBean.getPoiInfo().location.longitude;
                        addrName = addrBean.getPoiInfo().getName();
                        addrDes = addrBean.getPoiInfo().getAddress();
                    } else {
                        addrBean.setIschecked(false);
                    }
                }
                onMapClick(new LatLng(lat, lng));
                MapUtil.mapMoveTo(mBaiduMap, new LatLng(lat, lng));
                adapter.notifyDataSetChanged();
            }
        });

        progressBar = findViewById(R.id.progressBarRy);
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void initData() {

    }

    private void initLogic() {
        if (lat != 0.0 & lng != 0.0) {
            MapUtil.mapMoveTo(mBaiduMap, new LatLng(lat, lng));
        }
        mBaiduMap.setOnMapClickListener(this);//先搜索再设置监听，有概率收不到回调
        onMapClick(new LatLng(lat, lng));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18);    //设置地图中心点以及缩放级别
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(500));
        //==============================================
        mBaiduMap.clear();
        // 构建MarkerOption，用于在地图上添加Marker
        MarkerOptions options = new MarkerOptions().position(latLng).icon(locationMarker);
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlay(options);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {
        onMapClick(mapPoi.getPosition());
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.toast(mContext, "无结果");
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        } else {
            List<PoiInfo> searchList = reverseGeoCodeResult.getPoiList();
            List<AddressBean> addressList = new ArrayList<>();
            if (searchList != null) {
                for (int i = 0; i < searchList.size(); i++) {
                    PoiInfo poiInfo = searchList.get(i);
                    if (0 == i) {
                        lat = poiInfo.getLocation().latitude;
                        lng = poiInfo.getLocation().longitude;
                        addrName = poiInfo.getName();
                        addrDes = poiInfo.getAddress();
                        addressList.add(new AddressBean(poiInfo, true));
                    } else {
                        addressList.add(new AddressBean(poiInfo, false));
                    }
                }
            }
            adapter.setNewData(addressList);
            mAddressListRv.scrollToPosition(0);
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    protected String getUpdateHttpUrl() {
        return null;
    }


    @Override
    public void onLocationReceived(BDLocation bdLocation) {
        lat = bdLocation.getLatitude();
        lng = bdLocation.getLongitude();
        initLogic();
    }

    @Override
    public boolean requestLocation() {
        return true;
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onResume();
        super.onPause();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
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
        locationMarker.recycle();
        locationMarker = null;
        mapView.onDestroy();
        mapView = null;
        mGeoCoder.destroy();
        super.onDestroy();
    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return true;
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        //截图的回调
        ScreenShotHelper.saveScreenShot(bitmap, mMapViewRl, mapView);
        getTitleRightTv().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(LAT, lat);
                intent.putExtra(LNG, lng);
                intent.putExtra(ADDRNAME, addrName);
                intent.putExtra(ADDRDES, addrDes);
                setResult(BASE_REQUEST_RESULT, intent);
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }
        },500);
    }
}
