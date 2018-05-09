package com.tbs.tbs_mj.bean;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/10/27 17:59.
 */

public class _TopicDetail {

    /**
     * id : 7811
     * title : 防止多次点击
     * cover_url : http://cdn111.dev.tobosu.com/ke_file/2017-10-20/m_59e9ca97c7832.jpg
     * add_time : 2017-10-20
     * desc : 防止多次点击
     * share_url : http://m.dev.tobosu.com/anli/7811
     * detail_info : [{"sub_title":"gegegege","image_url":"https://st.hzcdn.com/simgs/f5d11be20949a972_4-2539/traditional-home-bar.jpg"}]
     */

    private String id;
    private String title;
    private String cover_url;
    private String add_time;
    private String desc;
    private String share_url;
    private List<DetailInfoBean> detail_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<DetailInfoBean> getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(List<DetailInfoBean> detail_info) {
        this.detail_info = detail_info;
    }

    public static class DetailInfoBean {
        /**
         * sub_title : gegegege
         * image_url : https://st.hzcdn.com/simgs/f5d11be20949a972_4-2539/traditional-home-bar.jpg
         */

        private String sub_title;
        private String image_url;

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
