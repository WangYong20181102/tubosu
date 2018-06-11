package com.tbs.tbsbusiness.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/6/7 11:23.
 */
public class _MyStore {
    /**
     * id : 276836
     * name : 深圳市发财猫有限公司深圳市发财猫有限公司有限公司
     * simp_name : 发财猫
     * store_logo : http://cdn111.dev.tobosu.com/company_logo/2018-03-26/thumb_5ab8a3241684e.png
     * brands_logo : http://cdn111.dev.tobosu.com/company_logo/2018-03-27/thumb_5ab9baf13ac8a.png
     * address : {"province_id":"19","province_name":"广东省","city_id":"199","city_name":"深圳市","district_id":0,"district_name":"","address":"测试长度一测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长五十测试长度一测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长一百"}
     * service_area : [{"id":"1769","name":"罗湖区","is_selected":1},{"id":"1770","name":"福田区","is_selected":0},{"id":"1771","name":"南山区","is_selected":0},{"id":"1772","name":"宝安区","is_selected":0},{"id":"1773","name":"龙岗区","is_selected":0},{"id":"1774","name":"盐田区","is_selected":0},{"id":"2998","name":"龙华区","is_selected":0},{"id":"2999","name":"光明新区","is_selected":0},{"id":"3000","name":"坪山新区","is_selected":1},{"id":"3001","name":"大鹏新区","is_selected":0},{"id":"3002","name":"龙岗周边","is_selected":0},{"id":"3003","name":"宝安周边","is_selected":0}]
     * home_improvement : [{"id":"1","name":"别墅","is_selected":0},{"id":"2","name":"普通住宅","is_selected":1},{"id":"3","name":"旧房","is_selected":1},{"id":"4","name":"小户型","is_selected":1},{"id":"5","name":"公寓","is_selected":0}]
     * tool_improvement : [{"id":"1","name":"KTV","is_selected":1},{"id":"2","name":"商铺","is_selected":1},{"id":"3","name":"餐厅","is_selected":0},{"id":"4","name":"美容院","is_selected":0},{"id":"5","name":"娱乐场所","is_selected":0},{"id":"6","name":"酒店","is_selected":0},{"id":"7","name":"办公室","is_selected":0},{"id":"8","name":"写字楼","is_selected":0},{"id":"9","name":"厂房","is_selected":0},{"id":"10","name":"学校","is_selected":1},{"id":"11","name":"医院","is_selected":0},{"id":"12","name":"网吧","is_selected":0},{"id":"13","name":"酒吧","is_selected":0},{"id":"14","name":"服装店","is_selected":0},{"id":"15","name":"其它工装","is_selected":0}]
     * good_at_style : [{"id":"33","name":"简欧","is_selected":0},{"id":"34","name":"欧式","is_selected":0},{"id":"35","name":"田园","is_selected":1},{"id":"36","name":"简约","is_selected":1},{"id":"37","name":"中式","is_selected":0},{"id":"38","name":"现代简约","is_selected":0},{"id":"39","name":"美式","is_selected":0},{"id":"40","name":"新中式","is_selected":0},{"id":"41","name":"韩式","is_selected":0},{"id":"42","name":"现代","is_selected":0},{"id":"43","name":"新古典","is_selected":0},{"id":"44","name":"简单","is_selected":0},{"id":"45","name":"简中","is_selected":0},{"id":"46","name":"北欧","is_selected":0},{"id":"47","name":"混搭","is_selected":0},{"id":"48","name":"地中海","is_selected":0},{"id":"49","name":"欧式田园","is_selected":0},{"id":"50","name":"法式","is_selected":1},{"id":"51","name":"日式","is_selected":0},{"id":"52","name":"现代欧式","is_selected":0},{"id":"53","name":"中式古典","is_selected":0},{"id":"54","name":"经典","is_selected":0},{"id":"55","name":"古典","is_selected":0},{"id":"56","name":"现代中式","is_selected":0},{"id":"57","name":"后现代","is_selected":0},{"id":"58","name":"东南亚","is_selected":0},{"id":"950","name":"洛可可","is_selected":0},{"id":"958","name":"乡村","is_selected":0}]
     * grade : 8
     * business_license : {"id":"119622","img_url":"http://cdn111.dev.tobosu.com/zhihao_img/2018-03-27/thumb_5ab9bafc8ccf3.jpg","registration_number":"","effect_time":"","font_name":""}
     * front_desk_img : {"id":"119627","img_url":"http://cdn111.dev.tobosu.com/showpic_img/2018-03-27/thumb_5ab9bb084d48a.jpg"}
     * honor_img : [{"id":"119663","img_url":"http://cdn111.dev.tobosu.com/company_img/2018-04-16/thumb_5ad43d801f8e8.jpg"},{"id":"119664","img_url":"http://cdn111.dev.tobosu.com/company_img/2018-04-16/thumb_5ad43d84f2ac8.jpg"},{"id":"119665","img_url":"http://cdn111.dev.tobosu.com/company_img/2018-04-16/thumb_5ad43d8b583c0.jpg"}]
     */

    private String id;
    private String name;
    private String simp_name;
    private String store_logo;
    private String brands_logo;
    private AddressBean address;
    private String grade;
    private BusinessLicenseBean business_license;
    private FrontDeskImgBean front_desk_img;
    private List<ServiceAreaBean> service_area;
    private List<HomeImprovementBean> home_improvement;
    private List<ToolImprovementBean> tool_improvement;
    private List<GoodAtStyleBean> good_at_style;
    private List<HonorImgBean> honor_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimp_name() {
        return simp_name;
    }

    public void setSimp_name(String simp_name) {
        this.simp_name = simp_name;
    }

    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }

    public String getBrands_logo() {
        return brands_logo;
    }

    public void setBrands_logo(String brands_logo) {
        this.brands_logo = brands_logo;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BusinessLicenseBean getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(BusinessLicenseBean business_license) {
        this.business_license = business_license;
    }

    public FrontDeskImgBean getFront_desk_img() {
        return front_desk_img;
    }

    public void setFront_desk_img(FrontDeskImgBean front_desk_img) {
        this.front_desk_img = front_desk_img;
    }

    public List<ServiceAreaBean> getService_area() {
        return service_area;
    }

    public void setService_area(List<ServiceAreaBean> service_area) {
        this.service_area = service_area;
    }

    public List<HomeImprovementBean> getHome_improvement() {
        return home_improvement;
    }

    public void setHome_improvement(List<HomeImprovementBean> home_improvement) {
        this.home_improvement = home_improvement;
    }

    public List<ToolImprovementBean> getTool_improvement() {
        return tool_improvement;
    }

    public void setTool_improvement(List<ToolImprovementBean> tool_improvement) {
        this.tool_improvement = tool_improvement;
    }

    public List<GoodAtStyleBean> getGood_at_style() {
        return good_at_style;
    }

    public void setGood_at_style(List<GoodAtStyleBean> good_at_style) {
        this.good_at_style = good_at_style;
    }

    public List<HonorImgBean> getHonor_img() {
        return honor_img;
    }

    public void setHonor_img(List<HonorImgBean> honor_img) {
        this.honor_img = honor_img;
    }

    public static class AddressBean {
        /**
         * province_id : 19
         * province_name : 广东省
         * city_id : 199
         * city_name : 深圳市
         * district_id : 0
         * district_name :
         * address : 测试长度一测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长五十测试长度一测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长度二测试长一百
         */

        private String province_id;
        private String province_name;
        private String city_id;
        private String city_name;
        private int district_id;
        private String district_name;
        private String address;

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

        public int getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(int district_id) {
            this.district_id = district_id;
        }

        public String getDistrict_name() {
            return district_name;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class BusinessLicenseBean {
        /**
         * id : 119622
         * img_url : http://cdn111.dev.tobosu.com/zhihao_img/2018-03-27/thumb_5ab9bafc8ccf3.jpg
         * registration_number :
         * effect_time :
         * font_name :
         */

        private String id;
        private String img_url;
        private String registration_number;
        private String effect_time;
        private String font_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getRegistration_number() {
            return registration_number;
        }

        public void setRegistration_number(String registration_number) {
            this.registration_number = registration_number;
        }

        public String getEffect_time() {
            return effect_time;
        }

        public void setEffect_time(String effect_time) {
            this.effect_time = effect_time;
        }

        public String getFont_name() {
            return font_name;
        }

        public void setFont_name(String font_name) {
            this.font_name = font_name;
        }
    }

    public static class FrontDeskImgBean {
        /**
         * id : 119627
         * img_url : http://cdn111.dev.tobosu.com/showpic_img/2018-03-27/thumb_5ab9bb084d48a.jpg
         */

        private String id;
        private String img_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }

    public static class ServiceAreaBean {
        /**
         * id : 1769
         * name : 罗湖区
         * is_selected : 1
         */

        private String id;
        private String name;
        private int is_selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(int is_selected) {
            this.is_selected = is_selected;
        }
    }

    public static class HomeImprovementBean {
        /**
         * id : 1
         * name : 别墅
         * is_selected : 0
         */

        private String id;
        private String name;
        private int is_selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(int is_selected) {
            this.is_selected = is_selected;
        }
    }

    public static class ToolImprovementBean {
        /**
         * id : 1
         * name : KTV
         * is_selected : 1
         */

        private String id;
        private String name;
        private int is_selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(int is_selected) {
            this.is_selected = is_selected;
        }
    }

    public static class GoodAtStyleBean {
        /**
         * id : 33
         * name : 简欧
         * is_selected : 0
         */

        private String id;
        private String name;
        private int is_selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(int is_selected) {
            this.is_selected = is_selected;
        }
    }

    public static class HonorImgBean {
        /**
         * id : 119663
         * img_url : http://cdn111.dev.tobosu.com/company_img/2018-04-16/thumb_5ad43d801f8e8.jpg
         */

        private String id;
        private String img_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
