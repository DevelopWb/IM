package com.juntai.disabled.basecomponent.bean;


import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述  托养跟踪列表
 * @CreateDate: 2020/4/27 11:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/27 11:04
 */
public class CareChildListNewestBean extends BaseResult {

    /**
     * error : null
     * returnValue : null
     * list : null
     * type : null
     * data : {"id":697,"longitude":118.452535,"latitude":35.095819,"name":"王加洗 九曲街道 王家斜坊","caseDate":"2018-10-13
     * 11:17:04","type":"居家托养-整理家务","place":"中国山东省临沂市河东区","photoOne":"/case_image/32b7966344564e0ca4b885d100b0621f
     * .jpeg","photoTwo":"/case_image/2a0e72fca0c24702a0b36fc774e9ecc1.jpeg",
     * "photoThree":"/case_image/e1d502a55f67494c8a3d39aa7e9a2b5e.jpeg","childCase":[{"id":3199,"name":"王加洗 九曲街道
     * 王家斜坊","idNo":null,"streetAddress":null,"communityAddress":null,
     * "personImg":"/case_image/0f431105b88a4128a24a05dd8ffca38f.jpeg","year":null,"place":null,"longitude":118
     * .452535,"latitude":35.095819,"caseDate":"时间:2019-09-12 11:02:32"},{"id":3198,"name":"王加洗 九曲街道 王家斜坊",
     * "idNo":null,"streetAddress":null,"communityAddress":null,
     * "personImg":"/case_image/9fb4383b46e44f92b0e6ad22607d3bdd.jpeg","year":null,"place":null,"longitude":118
     * .452535,"latitude":35.095819,"caseDate":"时间:2019-08-11 15:09:53"},{"id":3197,"name":"王加洗 九曲街道 王家斜坊",
     * "idNo":null,"streetAddress":null,"communityAddress":null,
     * "personImg":"/case_image/c58056e5a26d4633ad8657ba7c49f1c0.jpeg","year":null,"place":null,"longitude":118
     * .452535,"latitude":35.095819,"caseDate":"时间:2019-07-06 10:43:35"},{"id":2070,"name":"王加洗 走失后又找回来了",
     * "idNo":null,"streetAddress":null,"communityAddress":null,
     * "personImg":"/case_image/a662cb1ad5864da8b16ecba662e2e748.jpeg","year":null,"place":null,"longitude":118
     * .452535,"latitude":35.095819,"caseDate":"时间:2019-06-23 17:13:11"},{"id":2072,"name":"王加洗 九曲街道 王家斜坊",
     * "idNo":null,"streetAddress":null,"communityAddress":null,
     * "personImg":"/case_image/1646cc56ed844e33b2093380bd15d79a.jpeg","year":null,"place":null,"longitude":118
     * .452535,"latitude":35.095819,"caseDate":"时间:2019-05-10 08:49:38"},{"id":2075,"name":"王加洗 九曲街道 走失了又找回来了",
     * "idNo":null,"streetAddress":null,"communityAddress":null,
     * "personImg":"/case_image/7e86053a6bde42ba828bac3860c3c113.jpeg","year":null,"place":null,"longitude":118
     * .452535,"latitude":35.095819,"caseDate":"时间:2019-04-13 10:28:19"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 697
         * longitude : 118.452535
         * latitude : 35.095819
         * name : 王加洗 九曲街道 王家斜坊
         * caseDate : 2018-10-13 11:17:04
         * type : 居家托养-整理家务
         * place : 中国山东省临沂市河东区
         * photoOne : /case_image/32b7966344564e0ca4b885d100b0621f.jpeg
         * photoTwo : /case_image/2a0e72fca0c24702a0b36fc774e9ecc1.jpeg
         * photoThree : /case_image/e1d502a55f67494c8a3d39aa7e9a2b5e.jpeg
         * childCase : [{"id":3199,"name":"王加洗 九曲街道 王家斜坊","idNo":null,"streetAddress":null,"communityAddress":null,
         * "personImg":"/case_image/0f431105b88a4128a24a05dd8ffca38f.jpeg","year":null,"place":null,"longitude":118
         * .452535,"latitude":35.095819,"caseDate":"时间:2019-09-12 11:02:32"},{"id":3198,"name":"王加洗 九曲街道 王家斜坊",
         * "idNo":null,"streetAddress":null,"communityAddress":null,
         * "personImg":"/case_image/9fb4383b46e44f92b0e6ad22607d3bdd.jpeg","year":null,"place":null,"longitude":118
         * .452535,"latitude":35.095819,"caseDate":"时间:2019-08-11 15:09:53"},{"id":3197,"name":"王加洗 九曲街道 王家斜坊",
         * "idNo":null,"streetAddress":null,"communityAddress":null,
         * "personImg":"/case_image/c58056e5a26d4633ad8657ba7c49f1c0.jpeg","year":null,"place":null,"longitude":118
         * .452535,"latitude":35.095819,"caseDate":"时间:2019-07-06 10:43:35"},{"id":2070,"name":"王加洗 走失后又找回来了",
         * "idNo":null,"streetAddress":null,"communityAddress":null,
         * "personImg":"/case_image/a662cb1ad5864da8b16ecba662e2e748.jpeg","year":null,"place":null,"longitude":118
         * .452535,"latitude":35.095819,"caseDate":"时间:2019-06-23 17:13:11"},{"id":2072,"name":"王加洗 九曲街道 王家斜坊",
         * "idNo":null,"streetAddress":null,"communityAddress":null,
         * "personImg":"/case_image/1646cc56ed844e33b2093380bd15d79a.jpeg","year":null,"place":null,"longitude":118
         * .452535,"latitude":35.095819,"caseDate":"时间:2019-05-10 08:49:38"},{"id":2075,"name":"王加洗 九曲街道 走失了又找回来了",
         * "idNo":null,"streetAddress":null,"communityAddress":null,
         * "personImg":"/case_image/7e86053a6bde42ba828bac3860c3c113.jpeg","year":null,"place":null,"longitude":118
         * .452535,"latitude":35.095819,"caseDate":"时间:2019-04-13 10:28:19"}]
         */

        private int id;
        private double longitude;
        private double latitude;
        private String name;
        private String caseDate;
        private String type;
        private String place;
        private String photoOne;
        private String photoTwo;
        private String photoThree;
        private List<ChildCaseBean> childCase;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCaseDate() {
            return caseDate;
        }

        public void setCaseDate(String caseDate) {
            this.caseDate = caseDate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPhotoOne() {
            return photoOne;
        }

        public void setPhotoOne(String photoOne) {
            this.photoOne = photoOne;
        }

        public String getPhotoTwo() {
            return photoTwo;
        }

        public void setPhotoTwo(String photoTwo) {
            this.photoTwo = photoTwo;
        }

        public String getPhotoThree() {
            return photoThree;
        }

        public void setPhotoThree(String photoThree) {
            this.photoThree = photoThree;
        }

        public List<ChildCaseBean> getChildCase() {
            return childCase;
        }

        public void setChildCase(List<ChildCaseBean> childCase) {
            this.childCase = childCase;
        }

        public static class ChildCaseBean {
            /**
             * id : 3199
             * name : 王加洗 九曲街道 王家斜坊
             * idNo : null
             * streetAddress : null
             * communityAddress : null
             * personImg : /case_image/0f431105b88a4128a24a05dd8ffca38f.jpeg
             * year : null
             * place : null
             * longitude : 118.452535
             * latitude : 35.095819
             * caseDate : 时间:2019-09-12 11:02:32
             */

            private int id;
            private String name;
            private Object idNo;
            private Object streetAddress;
            private Object communityAddress;
            private String personImg;
            private String caseFId;
            private Object year;
            private Object place;
            private double longitude;
            private double latitude;
            private String caseDate;

            public String getCaseFId() {
                return caseFId == null ? "" : caseFId;
            }

            public void setCaseFId(String caseFId) {
                this.caseFId = caseFId == null ? "" : caseFId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getIdNo() {
                return idNo;
            }

            public void setIdNo(Object idNo) {
                this.idNo = idNo;
            }

            public Object getStreetAddress() {
                return streetAddress;
            }

            public void setStreetAddress(Object streetAddress) {
                this.streetAddress = streetAddress;
            }

            public Object getCommunityAddress() {
                return communityAddress;
            }

            public void setCommunityAddress(Object communityAddress) {
                this.communityAddress = communityAddress;
            }

            public String getPersonImg() {
                return personImg;
            }

            public void setPersonImg(String personImg) {
                this.personImg = personImg;
            }

            public Object getYear() {
                return year;
            }

            public void setYear(Object year) {
                this.year = year;
            }

            public Object getPlace() {
                return place;
            }

            public void setPlace(Object place) {
                this.place = place;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getCaseDate() {
                return caseDate;
            }

            public void setCaseDate(String caseDate) {
                this.caseDate = caseDate;
            }
        }
    }
}
