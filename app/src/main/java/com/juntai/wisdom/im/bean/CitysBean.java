package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.bean.BaseAddrBean;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-19 15:02
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-19 15:02
 */
public class CitysBean extends BaseAddrBean {

    /**
     * status : 1
     * info : OK
     * infocode : 10000
     * count : 1
     * suggestion : {"keywords":[],"cities":[]}
     * districts : [{"citycode":"010","adcode":"110000","name":"北京市","center":"116.407387,39.904179","level":"province","districts":[{"citycode":"010","adcode":"110100","name":"北京城区","center":"116.405285,39.904989","level":"city","districts":[]}]}]
     */

    private String info;
    private String infocode;
    private String count;
    private List<DistrictsBean> districts;


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public List<DistrictsBean> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DistrictsBean> districts) {
        this.districts = districts;
    }

    public static class DistrictsBean {
        /**
         * citycode : 010
         * adcode : 110100
         * name : 北京城区
         * center : 116.405285,39.904989
         * level : city
         * districts : []
         */

        private Object citycode;
        private String adcode;
        private String name;
        private String center;
        private String level;
        private List<DistrictsBean> districts;

        public Object getCitycode() {
            return citycode;
        }

        public void setCitycode(Object citycode) {
            this.citycode = citycode;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCenter() {
            return center;
        }

        public void setCenter(String center) {
            this.center = center;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public List<DistrictsBean> getDistricts() {
            return districts;
        }

        public void setDistricts(List<DistrictsBean> districts) {
            this.districts = districts;
        }
    }
}
