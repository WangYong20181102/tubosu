package com.tbs.tbs_mj.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/4/23 14:04.
 * 本地存在的城市json
 */
public class _LocalCity implements IPickerViewData {

    /**
     * province_id : 1
     * province_name : 北京市
     * city : [{"city_id":"1","city_name":"北京市","district":[{"district_id":"1","district_name":"东城区"},{"district_id":"2","district_name":"西城区"},{"district_id":"3","district_name":"崇文区"},{"district_id":"4","district_name":"宣武区"},{"district_id":"5","district_name":"朝阳区"},{"district_id":"6","district_name":"丰台区"},{"district_id":"7","district_name":"石景山区"},{"district_id":"8","district_name":"海淀区"},{"district_id":"9","district_name":"门头沟"},{"district_id":"10","district_name":"房山区"},{"district_id":"11","district_name":"通州区"},{"district_id":"12","district_name":"顺义区"},{"district_id":"13","district_name":"昌平区"},{"district_id":"14","district_name":"大兴区"},{"district_id":"15","district_name":"怀柔区"},{"district_id":"16","district_name":"平谷区"},{"district_id":"17","district_name":"密云县"},{"district_id":"18","district_name":"延庆县"},{"district_id":"3089","district_name":"燕郊区"},{"district_id":"31022","district_name":"密云区"}]}]
     */

    private String province_id;
    private String province_name;
    private List<CityBean> city;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return this.province_name;
    }

    public static class CityBean {
        /**
         * city_id : 1
         * city_name : 北京市
         * district : [{"district_id":"1","district_name":"东城区"},{"district_id":"2","district_name":"西城区"},{"district_id":"3","district_name":"崇文区"},{"district_id":"4","district_name":"宣武区"},{"district_id":"5","district_name":"朝阳区"},{"district_id":"6","district_name":"丰台区"},{"district_id":"7","district_name":"石景山区"},{"district_id":"8","district_name":"海淀区"},{"district_id":"9","district_name":"门头沟"},{"district_id":"10","district_name":"房山区"},{"district_id":"11","district_name":"通州区"},{"district_id":"12","district_name":"顺义区"},{"district_id":"13","district_name":"昌平区"},{"district_id":"14","district_name":"大兴区"},{"district_id":"15","district_name":"怀柔区"},{"district_id":"16","district_name":"平谷区"},{"district_id":"17","district_name":"密云县"},{"district_id":"18","district_name":"延庆县"},{"district_id":"3089","district_name":"燕郊区"},{"district_id":"31022","district_name":"密云区"}]
         */

        private String city_id;
        private String city_name;
        private List<DistrictBean> district;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<DistrictBean> getDistrict() {
            return district;
        }

        public void setDistrict(List<DistrictBean> district) {
            this.district = district;
        }

        public static class DistrictBean {
            /**
             * district_id : 1
             * district_name : 东城区
             */

            private String district_id;
            private String district_name;

            public String getDistrict_id() {
                return district_id;
            }

            public void setDistrict_id(String district_id) {
                this.district_id = district_id;
            }

            public String getDistrict_name() {
                return district_name;
            }

            public void setDistrict_name(String district_name) {
                this.district_name = district_name;
            }
        }
    }
}
