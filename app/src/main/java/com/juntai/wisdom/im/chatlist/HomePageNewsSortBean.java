package com.juntai.wisdom.im.chatlist;

import android.support.v4.util.ArrayMap;

import com.juntai.wisdom.im.utils.CalendarUtil;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-15 9:48
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-15 9:48
 */
public class HomePageNewsSortBean  implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;
        try {
            ArrayMap<String, Object> map1 = (ArrayMap<String, Object>) o1, map2 = (ArrayMap<String, Object>) o2;
            String date01 = "";
            String date02 = "";
            for (Map.Entry<String, Object> entry01 : map1.entrySet()) {
                date01 = entry01.getKey(); //传过来的这个ArrayMap只有一个键，所以遍历的时候，必定有且仅有"时间"这个键
            }
            for (Map.Entry<String, Object> entry02 : map2.entrySet()) {
                date02 = entry02.getKey();
            }

            Date map1_time = CalendarUtil.parseData(date01);
            Date map2_time = CalendarUtil.parseData(date02);

            if (map1_time.after(map2_time)) {
                result = -1;
            } else if (map1_time.before(map2_time)) {
                result = 1;
            } else if (map1_time.equals(map2_time)) {
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
