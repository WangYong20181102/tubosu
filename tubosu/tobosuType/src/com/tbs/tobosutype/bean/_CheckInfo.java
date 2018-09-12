package com.tbs.tobosutype.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/10 17:27.
 */
public class _CheckInfo {

    /**
     * status : 200
     * msg : 操作成功
     * data : {"sort_type":[{"id":"1","name":"距离"},{"id":"2","name":"方案数"},{"id":"3","name":"效果图数"},{"id":"4","name":"浏览热度"}],"district_id":[{"id":"1769","name":"罗湖区"},{"id":"1770","name":"福田区"},{"id":"1771","name":"南山区"},{"id":"1772","name":"宝安区"},{"id":"1773","name":"龙岗区"},{"id":"1774","name":"盐田区"},{"id":"2998","name":"龙华区"},{"id":"2999","name":"光明新区"},{"id":"3000","name":"坪山新区"},{"id":"3001","name":"大鹏新区"},{"id":"3002","name":"龙岗周边"},{"id":"3003","name":"宝安周边"}],"more":[{"title":"家庭装修","sub_title":[{"id":"0","name":"类型不限"},{"id":"1","name":"别墅"},{"id":"2","name":"普通住宅"},{"id":"3","name":"旧房"},{"id":"4","name":"小户型"},{"id":"5","name":"公寓"}]},{"title":"商铺装修","sub_title":[{"id":"1","name":"KTV"},{"id":"2","name":"商铺"},{"id":"3","name":"餐厅"},{"id":"4","name":"美容院"},{"id":"5","name":"娱乐场所"},{"id":"6","name":"酒店"},{"id":"7","name":"办公室"},{"id":"8","name":"写字楼"},{"id":"9","name":"厂房"},{"id":"10","name":"学校"},{"id":"11","name":"医院"},{"id":"12","name":"网吧"},{"id":"13","name":"酒吧"},{"id":"14","name":"服装店"},{"id":"15","name":"其它工装"}]}]}
     */

    private int status;
    private String msg;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<SortTypeBean> sort_type;
        private List<DistrictIdBean> district_id;
        private List<MoreBean> more;

        public List<SortTypeBean> getSort_type() {
            return sort_type;
        }

        public void setSort_type(List<SortTypeBean> sort_type) {
            this.sort_type = sort_type;
        }

        public List<DistrictIdBean> getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(List<DistrictIdBean> district_id) {
            this.district_id = district_id;
        }

        public List<MoreBean> getMore() {
            return more;
        }

        public void setMore(List<MoreBean> more) {
            this.more = more;
        }

        public static class SortTypeBean {
            /**
             * id : 1
             * name : 距离
             */

            private String id;
            private String name;

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
        }

        public static class DistrictIdBean {
            /**
             * id : 1769
             * name : 罗湖区
             */

            private String id;
            private String name;

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
        }

        public static class MoreBean {
            /**
             * title : 家庭装修
             * sub_title : [{"id":"0","name":"类型不限"},{"id":"1","name":"别墅"},{"id":"2","name":"普通住宅"},{"id":"3","name":"旧房"},{"id":"4","name":"小户型"},{"id":"5","name":"公寓"}]
             */

            private String title;
            private List<SubTitleBean> sub_title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<SubTitleBean> getSub_title() {
                return sub_title;
            }

            public void setSub_title(List<SubTitleBean> sub_title) {
                this.sub_title = sub_title;
            }

            public static class SubTitleBean {
                /**
                 * id : 0
                 * name : 类型不限
                 */

                private String id;
                private String name;

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
            }
        }
    }
}
