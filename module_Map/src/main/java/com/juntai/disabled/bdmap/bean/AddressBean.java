package com.juntai.disabled.bdmap.bean;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-12-05 11:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-05 11:07
 */
public class AddressBean {
    private PoiInfo poiInfo;
    private boolean ischecked;

    public AddressBean(PoiInfo poiInfo, boolean ischecked) {
        this.poiInfo = poiInfo;
        this.ischecked = ischecked;
    }


    public PoiInfo getPoiInfo() {
        return poiInfo;
    }

    public void setPoiInfo(PoiInfo poiInfo) {
        this.poiInfo = poiInfo;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

}
