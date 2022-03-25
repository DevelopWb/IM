package com.juntai.disabled.bdmap.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.juntai.disabled.bdmap.R;

import java.util.Map;

/**
 * author:wong
 * Date: 2019/5/26
 * Description:
 */
public class MapInfoWindowUtil{
    //无按钮infowindow,只有头像和信息
    public static void showIwWithoutBtn(Context context, String imgUrl, Map<String, String> infos, LatLng position, BaiduMap map, int margin) {
        MapUtil.mapMoveTo(map, position);
        View mIWView = View.inflate(context, R.layout.important_iw, null);//将一个布局文件转换成一个view对象
        StringBuilder infoStr = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            infoStr.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        ((TextView) mIWView.findViewById(R.id.important_infos)).setText(infoStr.toString());
        Glide.with(context).asBitmap()
                .load(imgUrl)
                .error(R.drawable.nopicture)
                .into((ImageView) mIWView.findViewById(R.id.head_image));
        InfoWindow mInfoWindow = new InfoWindow(mIWView, position, -margin-margin/3);
        map.showInfoWindow(mInfoWindow);
    }

    public static void showIwOfPeople(){

    }
}
